package br.com.growe.growe_backend.dtos.request;

import br.com.growe.growe_backend.domain.Company;
import br.com.growe.growe_backend.rules.SizeRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateCompanyRequest(
    @NotBlank(message = "Company name is required")
    @Size(min = 1, max = 200, message = "Company name must be between 1 and 200 characters")
    String name,
    SizeRange sizeRange,
    @Size(max = 500, message = "Company image URL must not exceed 500 characters")
    String companyImage
) {

  public static void updateCompany(Company company, UpdateCompanyRequest req) {
    company.setName(req.name());

    if(req.sizeRange() != null) {
      company.setSizeRange(req.sizeRange());
    }

    if(req.companyImage() != null) {
      company.setCompanyImage(req.companyImage());
    }
  }
}
