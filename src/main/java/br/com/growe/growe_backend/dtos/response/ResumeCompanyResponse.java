package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.Company;
import br.com.growe.growe_backend.rules.Plan;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ResumeCompanyResponse(
    UUID id,
    String name,
    String slug,
    String cnpj,
    String sizeRange,
    Plan plan,
    String companyImage,
    boolean isActive,
    List<ResumeMemberResponse> users
) {

  public static ResumeCompanyResponse toResponse(Company company) {
    return ResumeCompanyResponse
        .builder()
        .id(company.getId())
        .name(company.getName())
        .slug(company.getSlug())
        .cnpj(company.getCnpj())
        .sizeRange(company.getSizeRange().getLabel())
        .plan(company.getPlan())
        .companyImage(company.getCompanyImage())
        .isActive(company.isActive())
        .users(company.getMembers().stream().map(ResumeMemberResponse::fromEntity).toList())
        .build();

  }

}
