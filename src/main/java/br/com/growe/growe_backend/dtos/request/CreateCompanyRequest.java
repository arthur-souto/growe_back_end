package br.com.growe.growe_backend.dtos.request;

import br.com.growe.growe_backend.domain.Company;
import br.com.growe.growe_backend.rules.Plan;
import br.com.growe.growe_backend.rules.SizeRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCompanyRequest(

    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name must be at most 200 characters")
    String name,

    @NotBlank(message = "CNPJ is required")
    @Pattern(regexp = "\\d{14}", message = "CNPJ must contain exactly 14 digits")
    String cnpj,

    @NotNull(message = "Size range is required")
    SizeRange sizeRange,

    @NotNull(message = "Plan is required")
    Plan plan,

    String companyImage
) {
  public static Company toEntity(CreateCompanyRequest req) {
    return Company
        .builder()
        .name(req.name)
        .cnpj(req.cnpj)
        .sizeRange(req.sizeRange)
        .plan(req.plan)
        .companyImage(
            req.companyImage != null ? req.companyImage : null
        )
        .isActive(true)
        .build();
  }
}