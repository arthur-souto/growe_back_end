package br.com.growe.growe_backend.exceptions;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BusinessException {
  public AccessDeniedException(String message) {
    super(message, HttpStatus.FORBIDDEN, "ACCESS_DENIED");
  }

  public AccessDeniedException(String resourceType, String field, String value) {
    super(
        String.format("ACCESS_DENIED", resourceType, field, value),
        HttpStatus.FORBIDDEN,
        "ACCESS_DENIED"
    );
  }
}
