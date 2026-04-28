package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.Company;
import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.rules.Plan;
import br.com.growe.growe_backend.rules.SizeRange;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record CompanyDetailsResponse(
    UUID id,
    String name,
    String slug,
    String cnpj,
    SizeRange sizeRange,
    Plan plan,
    String companyImage,
    Instant trialEndsAt,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    List<ResumeMemberResponse> members
) {

  public static CompanyDetailsResponse fromEntity(Company company) {
    return
        CompanyDetailsResponse
            .builder()
            .id(company.getId())
            .name(company.getName())
            .slug(company.getSlug())
            .cnpj(company.getCnpj())
            .sizeRange(company.getSizeRange())
            .plan(company.getPlan())
            .companyImage(company.getCompanyImage())
            .trialEndsAt(company.getTrialEndsAt())
            .isActive(company.isActive())
            .createdAt(company.getCreatedAt())
            .updatedAt(company.getUpdatedAt())
            .members(company.getMembers().stream().map(m -> ResumeMemberResponse.fromEntity(m.getUser(), m)).toList())
            .build();
  }
}
