package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.User;
import br.com.growe.growe_backend.rules.Role;

import java.time.Instant;
import java.util.UUID;

public record UserDetailsResponse(
    UUID id,
    String fullName,
    String email,
    Role role,
    boolean active,
    String profileImage,
    Instant lastLoginAt,
    Instant createdAt,
    Instant updatedAt
) {

  public static UserDetailsResponse toResponse(User user) {
    return new UserDetailsResponse(
        user.getId(),
        user.getFullName(),
        user.getEmail(),
        user.getRole(),
        user.isActive(),
        user.getProfileImage(),
        user.getLastLoginAt(),
        user.getCreatedAt(),
        user.getUpdatedAt()
    );
  }
}
