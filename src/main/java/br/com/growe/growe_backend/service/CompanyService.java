package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.domain.CompanyMembers;
import br.com.growe.growe_backend.dtos.request.CreateCompanyRequest;
import br.com.growe.growe_backend.dtos.response.CreateCompanyResponse;
import br.com.growe.growe_backend.repository.CompanyMembersRepository;
import br.com.growe.growe_backend.repository.CompanyRepository;
import br.com.growe.growe_backend.rules.CompanyRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;


@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;

  private final CompanyMembersRepository companyMembersRepository;

  private static final int TRIAL_PERIOD_MONTHS = 3;


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

    final var owner = CompanyMembers
        .builder()
        .user(principal.user())
        .role(CompanyRole.OWNER)
        .company(savedCompany)
        .createdAt(Instant.now())
            .build();

    final var savedCompanyMember = companyMembersRepository.save(owner);

    return CreateCompanyResponse.toResponse(savedCompany, savedCompanyMember);
  }

  private String generateSlug(String name) {
    return Normalizer.normalize(name, Normalizer.Form.NFD)
        .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
        .toLowerCase()
        .trim()
        .replaceAll("[^a-z0-9\\s-]", "")
        .replaceAll("\\s+", "-");
  }

}
