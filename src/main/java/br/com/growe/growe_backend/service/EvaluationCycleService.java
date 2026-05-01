package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.domain.EvaluationCycle;
import br.com.growe.growe_backend.dtos.request.CreateEvaluationCycleRequest;
import br.com.growe.growe_backend.dtos.response.CycleResumeResponse;
import br.com.growe.growe_backend.dtos.response.CycleStatusSummary;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.exceptions.AccessDeniedException;
import br.com.growe.growe_backend.repository.EvaluationCycleRepository;
import br.com.growe.growe_backend.rules.CompanyRole;
import br.com.growe.growe_backend.utils.CompanyMemberUtils;
import br.com.growe.growe_backend.utils.CompanyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationCycleService {

  private final EvaluationCycleRepository evaluationCycleRepository;
  private final CompanyUtils companyUtils;
  private final CompanyMemberUtils companyMemberUtils;


  @Transactional
  public IdResponse createCycle(
      UserPrincipal userPrincipal,
      CreateEvaluationCycleRequest req,
      String slug) {

    final var user = userPrincipal.user();
    final var company = companyUtils.findCompanyBySlug(slug);
    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(user.getId(), company.getId());

    if(member.getRole() != CompanyRole.ADMIN && member.getRole() != CompanyRole.OWNER) {
      throw new AccessDeniedException("You don´t have permissions");
    }

    final var cycle = EvaluationCycle
        .builder()
        .name(req.name())
        .startDate(req.startDate())
        .description(req.description())
        .color(req.color())
        .endDate(req.endDate())
        .createdBy(member)
        .company(company)
        .isActive(false)
        .build();

    return new IdResponse(
        evaluationCycleRepository.save(cycle).getId()
    );
  }

  @Transactional(readOnly = true)
  public Page<CycleResumeResponse> findAllCyclesBySlug(
      String slug,
      UserPrincipal userPrincipal,
      Pageable pageable
  ) {

    final var user = userPrincipal.user();
    final var company = companyUtils.findCompanyBySlug(slug);

    companyMemberUtils.findCompanyMemberByUserAndCompany(user.getId(), company.getId());

    return evaluationCycleRepository
        .findAllByCompany_slug(slug, pageable)
        .map(c -> new CycleResumeResponse(
            c.getId(),
            c.getCreatedBy().getUser().getFullName(),
            c.getCreatedBy().getUser().getProfileImage(),
            c.getCreatedBy().getRole(),
            c.getName(),
            c.getDescription(),
            c.getColor(),
            c.isActive(),
            c.getStartDate(),
            c.getEndDate(),
            c.getCreatedAt()
        ));
  }

  @Transactional
  public CycleStatusSummary refreshStatusesBySlug(String slug) {

    Instant now = Instant.now();

    int activated = evaluationCycleRepository.activateCyclesBySlug(now, slug);
    int closed = evaluationCycleRepository.closeCyclesBySlug(now, slug);

    return new CycleStatusSummary(activated, closed);
  }

  @Transactional
  public CycleStatusSummary refreshStatuses() {

    Instant now = Instant.now();

    int activated = evaluationCycleRepository.activateCycles(now);
    int closed = evaluationCycleRepository.closeCycles(now);

    log.info("Cycle status refresh — activated: {}, closed: {}", activated, closed);

    return new CycleStatusSummary(activated, closed);
  }
}
