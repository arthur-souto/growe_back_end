package br.com.growe.growe_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GroqConfig {

  @Value("${groq.api.key}")
  private String apiKey;

  @Value("${groq.api.url}")
  private String apiUrl;

  @Bean
  public RestTemplate groqRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add((request, body, execution) -> {
      request.getHeaders().set("Authorization", "Bearer " + apiKey);
      request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
      return execution.execute(request, body);
    });
    return restTemplate;
  }
}
