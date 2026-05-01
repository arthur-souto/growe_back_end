package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.domain.Company;
import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.dtos.request.CreateCompanyRequest;
import br.com.growe.growe_backend.dtos.request.UpdateCompanyRequest;
import br.com.growe.growe_backend.dtos.response.CompanyDetailsResponse;
import br.com.growe.growe_backend.dtos.response.CreateCompanyResponse;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.dtos.response.ResumeCompanyResponse;
import br.com.growe.growe_backend.exceptions.ResourceNotFoundException;
import br.com.growe.growe_backend.repository.CompanyMembersRepository;
import br.com.growe.growe_backend.repository.CompanyRepository;
import br.com.growe.growe_backend.rules.CompanyRole;
import br.com.growe.growe_backend.utils.CompanyMemberUtils;
import br.com.growe.growe_backend.utils.CompanyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final CompanyMembersRepository companyMembersRepository;
  private final CompanyUtils companyUtils;
  private final CompanyMemberUtils companyMemberUtils;
  private static final int TRIAL_PERIOD_MONTHS = 3;
  private static final List<CompanyRole> ACCESS_ROLES = List.of(CompanyRole.ADMIN, CompanyRole.OWNER, CompanyRole.MANAGER, CompanyRole.RH);

  private String generateSlug(String name) {

    return Normalizer.normalize(name, Normalizer.Form.NFD)
        .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
        .toLowerCase()
        .trim()
        .replaceAll("[^a-z0-9\\s-]", "")
        .replaceAll("\\s+", "-");
  }

  @Transactional
  public CreateCompanyResponse createCompany(CreateCompanyRequest req, UserPrincipal principal) {

    if(companyRepository.existsByCnpj(req.cnpj())) {
      throw new IllegalArgumentException("CNPJ already exists");
    }

    final var company = CreateCompanyRequest.toEntity(req);

    company.setSlug(generateSlug(company.getName()));
    company.setTrialEndsAt(LocalDate.now()
        .plusMonths(TRIAL_PERIOD_MONTHS)
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC)
    );

    final var savedCompany = companyRepository.save(company);

    final var owner = CompanyMember
        .builder()
        .user(principal.user())
        .role(CompanyRole.OWNER)
        .company(savedCompany)
        .createdAt(Instant.now())
            .build();

    final var savedCompanyMember = companyMembersRepository.save(owner);

    return CreateCompanyResponse.toResponse(savedCompany, savedCompanyMember);
  }

  public IdResponse updateCompany(String slug, UserPrincipal userPrincipal, UpdateCompanyRequest req) {

    final var user = userPrincipal.user();
    final var company = companyUtils.findCompanyBySlug(slug);
    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(
        user.getId(),
        company.getId()
    );

    PermissionsService.validateIsOwner(member);

    final var slugUpdated = generateSlug(req.name());

    UpdateCompanyRequest.updateCompany(company, req);
    company.setSlug(slugUpdated);

    final var id = companyRepository.save(company).getId();

    return new IdResponse(id);
  }

  @Transactional(readOnly = true)
  public Page<ResumeCompanyResponse> findCompanies(UserPrincipal userPrincipal, Pageable pageable) {

    return companyRepository
        .findCompanies(userPrincipal.user().getId(), ACCESS_ROLES, true, pageable)
        .map(ResumeCompanyResponse::toResponse);
  }

  @Transactional
  public void deleteCompany(String slug, UserPrincipal userPrincipal) {

    final var user = userPrincipal.user();
    final var company = companyUtils.findCompanyBySlug(slug);

    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(
        user.getId(),
        company.getId()
    );

    PermissionsService.validateIsOwner(member);

    companyRepository.delete(company);
  }

  @Transactional(readOnly = true)
  public CompanyDetailsResponse findBySlug(String slug, UserPrincipal userPrincipal) {

    final var user = userPrincipal.user();
    final var company = companyUtils.findCompanyBySlug(slug);

    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(
        user.getId(),
        company.getId()
    );

    PermissionsService.validateHighPermission(member);

    return CompanyDetailsResponse.fromEntity(company);
  }
}
