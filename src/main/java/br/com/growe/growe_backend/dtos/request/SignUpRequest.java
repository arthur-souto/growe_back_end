package br.com.growe.growe_backend.dtos.request;

import br.com.growe.growe_backend.domain.User;
import br.com.growe.growe_backend.rules.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(

    @NotBlank(message = "Full name is required")
    String fullName,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password is required")
    String password,
    String profileImage
) {

  public static User toEntity(SignUpRequest req) {
    return User
        .builder()
        .fullName(req.fullName)
        .email(req.email)
        .password(req.password)
        .profileImage(
            req.profileImage != null ? req.profileImage : null
        )
        .active(true)
        .role(Role.MANAGER)
        .build();
  }
}
