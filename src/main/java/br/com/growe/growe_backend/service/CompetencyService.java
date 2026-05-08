package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.domain.Competency;
import br.com.growe.growe_backend.domain.CycleCompetency;
import br.com.growe.growe_backend.dtos.request.AddCompetencyToCycleRequest;
import br.com.growe.growe_backend.dtos.request.CreateCompetencyRequest;
import br.com.growe.growe_backend.dtos.response.CompetencyResponse;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.exceptions.ConflictException;
import br.com.growe.growe_backend.exceptions.ResourceNotFoundException;
import br.com.growe.growe_backend.repository.CompetencyRepository;
import br.com.growe.growe_backend.repository.CycleCompetencyRepository;
import br.com.growe.growe_backend.rules.CompanyRole;
import br.com.growe.growe_backend.utils.CompanyMemberUtils;
import br.com.growe.growe_backend.utils.CompanyUtils;
import br.com.growe.growe_backend.utils.CycleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompetencyService {

  private final CompetencyRepository competencyRepository;
  private final CycleCompetencyRepository cycleCompetencyRepository;
  private final CompanyUtils companyUtils;
  private final CompanyMemberUtils companyMemberUtils;
  private final CycleUtils cycleUtils;

  private static final List<CompanyRole> ADMIN_ROLES = List.of(
      CompanyRole.ADMIN, CompanyRole.OWNER, CompanyRole.RH);

  @Transactional
  public IdResponse createCompetency(UserPrincipal userPrincipal, String slug, CreateCompetencyRequest req) {

    final var user = userPrincipal.user();
    final var company = companyUtils.findCompanyBySlug(slug);

    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(user.getId(), company.getId());

    if (!ADMIN_ROLES.contains(member.getRole())) {
      throw new br.com.growe.growe_backend.exceptions.AccessDeniedException("You don't have permissions");
    }

    if (competencyRepository.existsByCompany_IdAndName(company.getId(), req.name())) {
      throw new ConflictException("Competency with this name already exists");
    }

    final var competency = Competency.builder()
        .company(company)
        .createdBy(member)
        .name(req.name())
        .description(req.description())
        .build();

    return new IdResponse(competencyRepository.save(competency).getId());
  }

  @Transactional(readOnly = true)
  public Page<CompetencyResponse> findAllByCompany(UserPrincipal userPrincipal, String slug, Pageable pageable) {

    final var user = userPrincipal.user();
    final var company = companyUtils.findCompanyBySlug(slug);

    companyMemberUtils.findCompanyMemberByUserAndCompany(user.getId(), company.getId());

    return competencyRepository.findAllByCompany_Id(company.getId(), pageable)
        .map(CompetencyResponse::toResponse);
  }

  @Transactional
  public void deleteCompetency(UserPrincipal userPrincipal, String slug, UUID competencyId) {

    final var user = userPrincipal.user();
    final var company = companyUtils.findCompanyBySlug(slug);

    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(user.getId(), company.getId());

    if (!ADMIN_ROLES.contains(member.getRole())) {
      throw new br.com.growe.growe_backend.exceptions.AccessDeniedException("You don't have permissions");
    }

    final var competency = competencyRepository.findById(competencyId)
        .orElseThrow(() -> new ResourceNotFoundException("Competency", competencyId.toString()));

    competencyRepository.delete(competency);
  }

  @Transactional
  public IdResponse addToCycle(UserPrincipal userPrincipal, UUID cycleId, AddCompetencyToCycleRequest req) {

    final var user = userPrincipal.user();
    final var cycle = cycleUtils.findEvaluationCycleById(cycleId);

    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(
        user.getId(), cycle.getCompany().getId());

    if (!ADMIN_ROLES.contains(member.getRole())) {
      throw new br.com.growe.growe_backend.exceptions.AccessDeniedException("You don't have permissions");
    }

    if (cycleCompetencyRepository.existsByCycle_IdAndCompetency_Id(cycleId, req.competencyId())) {
      throw new ConflictException("Competency already linked to this cycle");
    }

    final var competency = competencyRepository.findById(req.competencyId())
        .orElseThrow(() -> new ResourceNotFoundException("Competency", req.competencyId().toString()));

    final var cycleCompetency = CycleCompetency.builder()
        .cycle(cycle)
        .competency(competency)
        .build();

    return new IdResponse(cycleCompetencyRepository.save(cycleCompetency).getId());
  }

  @Transactional
  public void removeFromCycle(UserPrincipal userPrincipal, UUID cycleId, UUID competencyId) {

    final var user = userPrincipal.user();
    final var cycle = cycleUtils.findEvaluationCycleById(cycleId);

    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(
        user.getId(), cycle.getCompany().getId());

    if (!ADMIN_ROLES.contains(member.getRole())) {
      throw new br.com.growe.growe_backend.exceptions.AccessDeniedException("You don't have permissions");
    }

    cycleCompetencyRepository.deleteByCycle_IdAndCompetency_Id(cycleId, competencyId);
  }

  @Transactional(readOnly = true)
  public Page<CompetencyResponse> findAllByCycle(UserPrincipal userPrincipal, UUID cycleId, Pageable pageable) {

    final var user = userPrincipal.user();
    final var cycle = cycleUtils.findEvaluationCycleById(cycleId);

    companyMemberUtils.findCompanyMemberByUserAndCompany(user.getId(), cycle.getCompany().getId());

    return cycleCompetencyRepository.findAllByCycle_Id(cycleId, pageable)
        .map(cc -> CompetencyResponse.toResponse(cc.getCompetency()));
  }
}
