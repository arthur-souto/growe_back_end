package br.com.growe.growe_backend.client;

import br.com.growe.growe_backend.exceptions.BusinessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GroqClient {

  private static final Logger log = LoggerFactory.getLogger(GroqClient.class);

  @Value("${groq.api.url}")
  private String apiUrl;

  @Value("${groq.api.model}")
  private String model;

  private static final int MAX_RETRIES_CALL = 3;
  private static final int START_ATTEMPT_VALUE = 0;
  private static final int CHOICE_INDEX = 0;
  private static final long SLEEP_TEMP = 2000L;


  private final RestTemplate groqRestTemplate;
  private final ObjectMapper objectMapper;

  public String complete(String prompt) {
    int maxRetries = MAX_RETRIES_CALL;
    int attempt = START_ATTEMPT_VALUE;

    while (attempt < maxRetries) {
      try {
        Map<String, Object> body = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "user", "content", prompt)
            )
        );

        ResponseEntity<Map> response = groqRestTemplate.postForEntity(
            apiUrl + "/chat/completions",
            new HttpEntity<>(body),
            Map.class
        );

        if (response.getBody() == null) {
          throw new BusinessException(
              "Empty response from Groq",
              HttpStatus.BAD_GATEWAY,
              "ERROR_LLM"
          );
        }

        log.info("Groq request completed successfully on attempt {}", attempt + 1);
        return extractContent(response);

      } catch (HttpClientErrorException e) {
        if (e.getStatusCode().value() == HttpStatus.TOO_MANY_REQUESTS.value()) {
          attempt++;
          log.warn("Groq rate limit hit, attempt {}/{}", attempt, maxRetries);
          try {
            Thread.sleep(SLEEP_TEMP * attempt); // 2s, 4s, 6s
          } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new BusinessException(
                "Request interrupted",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "ERROR_INTERRUPTED"
            );
          }
        } else {
          log.error("Groq error status {}: {}", e.getStatusCode(), e.getMessage());
          throw new BusinessException(
              "Groq error: " + e.getMessage(),
              HttpStatus.BAD_GATEWAY,
              "ERROR_LLM"
          );
        }
      }
    }

    throw new BusinessException(
        "Groq rate limit exceeded after " + maxRetries + " retries",
        HttpStatus.TOO_MANY_REQUESTS,
        "ERROR_RATE_LIMIT"
    );
  }

  private String extractContent(ResponseEntity<Map> response) {

    List<Map<String, Object>> choices = objectMapper.convertValue(
        response.getBody().get("choices"),
        new TypeReference<>() {
        }
    );

    if (choices == null || choices.isEmpty()) {
      throw new BusinessException(
          "No choices returned from Groq",
          HttpStatus.BAD_GATEWAY,
          "ERROR_LLM"
      );
    }

    Map<String, Object> message = objectMapper.convertValue(
        choices.get(CHOICE_INDEX).get("message"),
        new TypeReference<>() {
        }
    );

    if (message == null) {
      throw new BusinessException(
          "No message in Groq response",
          HttpStatus.BAD_GATEWAY,
          "ERROR_LLM"
      );
    }

    String content = (String) message.get("content");

    if (content == null || content.isBlank()) {
      throw new BusinessException(
          "Empty content in Groq response",
          HttpStatus.BAD_GATEWAY,
          "ERROR_LLM"
      );
    }

    return content;
  }
}