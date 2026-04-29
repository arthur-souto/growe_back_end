package br.com.growe.growe_backend.dtos.request;

import br.com.growe.growe_backend.domain.User;
import br.com.growe.growe_backend.rules.CompanyRole;
import br.com.growe.growe_backend.rules.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCompanyMemberRequest(

    @NotBlank(message = "Full name is required")
    String fullName,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password is required")
    String password,

    @NotNull()
    CompanyRole role
) {

}
