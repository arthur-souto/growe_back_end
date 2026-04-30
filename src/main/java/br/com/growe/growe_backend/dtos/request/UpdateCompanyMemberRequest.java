package br.com.growe.growe_backend.dtos.request;

import br.com.growe.growe_backend.rules.CompanyRole;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateCompanyMemberRequest(
    @NotNull(message = "Role is required")
    CompanyRole role
) {
}
