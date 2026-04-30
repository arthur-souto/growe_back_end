package br.com.growe.growe_backend.service;


import br.com.growe.growe_backend.client.GroqClient;
import br.com.growe.growe_backend.dtos.request.ParsedMember;
import br.com.growe.growe_backend.exceptions.BusinessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LLMParserService {

  private final GroqClient groqClient;
  private final ObjectMapper objectMapper;

  public List<ParsedMember> extractMembers(String rawText) {
    String prompt = """
            Extract all members from the text below.
            Return ONLY a raw JSON array, no markdown, no explanation:
            [{"fullName":"...","email":"...","password":"...","role":"EMPLOYEE or RH or MANAGER"}]
            
            Text:
            %s
            """.formatted(rawText);

    String response = groqClient.complete(prompt);

    try {
      return objectMapper.readValue(
          response,
          new TypeReference<>() {
          }
      );
    } catch (Exception e) {
      throw new BusinessException("Failed to parse LLM response: " + response, HttpStatus.BAD_REQUEST, "ERROR_PARSER");
    }
  }
}