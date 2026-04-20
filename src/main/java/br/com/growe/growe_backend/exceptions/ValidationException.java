package br.com.growe.growe_backend.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class ValidationException extends BusinessException {

  private final Map<String, List<String>> errors;

  public ValidationException(Map<String, List<String>> errors) {
    super("Validation failed", HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    this.errors = errors;
  }

  public Map<String, List<String>> getErrors() {
    return errors;
  }
}