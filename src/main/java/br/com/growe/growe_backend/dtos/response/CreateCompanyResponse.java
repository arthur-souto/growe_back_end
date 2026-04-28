package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.Company;
import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.rules.Plan;
import br.com.growe.growe_backend.rules.SizeRange;

import java.time.Instant;

public record CreateCompanyResponse(
    String slug,
    Plan plan,
    SizeRange sizeRange,
    Instant trialEndsAt,
    String ownerEmail,
    String ownerName,
    Instant createdAt
) {

  public static CreateCompanyResponse toResponse(
     Company company,
     CompanyMember companyMembers
  ) {
    return new CreateCompanyResponse(
        company.getSlug(),
        company.getPlan(),
        company.getSizeRange(),
        company.getTrialEndsAt(),
        companyMembers.getUser().getEmail(),
        companyMembers.getUser().getFullName(),
        company.getCreatedAt()
    );
  }
}
