package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.User;

import java.time.Instant;

public record SignInResponse(
    String id,
    String fullName,
    String email,
    String role,
    String profileImage,
    String lastLoginAt
) {
  public static SignInResponse from(User user) {
    return new SignInResponse(
        user.getId().toString(),
        user.getFullName(),
        user.getEmail(),
        user.getRole().name(),
        user.getProfileImage() != null ? user.getProfileImage() : "",
        formatInstant(user.getLastLoginAt())
    );
  }

  private static String formatInstant(Instant instant) {
    return instant != null ? instant.toString() : "";
  }
}
