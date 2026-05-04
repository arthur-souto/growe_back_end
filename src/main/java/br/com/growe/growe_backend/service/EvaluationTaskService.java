package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.domain.EvaluationTask;
import br.com.growe.growe_backend.dtos.request.CreateEvaluationTaskRequest;
import br.com.growe.growe_backend.dtos.response.EvaluationTaskResponse;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.exceptions.AccessDeniedException;
import br.com.growe.growe_backend.exceptions.ConflictException;
import br.com.growe.growe_backend.repository.EvaluationTaskRepository;
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
public class EvaluationTaskService {

  private final CompanyUtils companyUtils;
  private final CompanyMemberUtils companyMemberUtils;
  private final CycleUtils cycleUtils;
  private final EvaluationTaskRepository evaluationTaskRepository;

  @Transactional
  public IdResponse createTask(
      UserPrincipal userPrincipal,
      UUID cycleId,
      String slug,
      CreateEvaluationTaskRequest req

  ) {

    final var user = userPrincipal.user();
    final var company = companyUtils.findCompanyBySlug(slug);
    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(user.getId(), company.getId());
    final var evaluator = companyMemberUtils.findCompanyMemberById(req.evaluatorId());
    final var evaluated = companyMemberUtils.findCompanyMemberById(req.evaluatedId());
    final var cycle = cycleUtils.findEvaluationCycleById(cycleId);
    final var canRoles = List.of(CompanyRole.ADMIN, CompanyRole.OWNER, CompanyRole.RH);

    if(!canRoles.contains(member.getRole())) {
      throw new AccessDeniedException("You don't have access");
    }

    if(!cycle.isActive()) {
      throw new ConflictException("Cycle is closed");
    }

    final var task = EvaluationTask
        .builder()
        .cycle(cycle)
        .createdBy(member)
        .evaluator(evaluator)
        .evaluated(evaluated)
        .assessmentType(req.assessmentType())
        .deadline(req.deadline())
        .build();


    return new IdResponse(
        evaluationTaskRepository.save(task).getId()
    );
  }

  @Transactional(readOnly = true)
  public Page<EvaluationTaskResponse> findAllByCycle(
      UUID cycleId,
      Pageable pageable,
      UserPrincipal userPrincipal
  ) {

    final var user = userPrincipal.user();
    final var cycle = cycleUtils.findEvaluationCycleById(cycleId);

    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(
        user.getId(), cycle.getCompany().getId());

    return getTasksByRole(member, cycleId, pageable);
  }

  private Page<EvaluationTaskResponse> getTasksByRole(CompanyMember member, UUID cycleId, Pageable pageable) {
    if(PermissionsService.hasAdministrativeAccessBoolean(member)) {
      return evaluationTaskRepository
          .findAllByCycle_Id(cycleId, pageable)
          .map(EvaluationTaskResponse::toResponse);
    }

    return evaluationTaskRepository.findAllByUserAndCycle(
        member.getId(),
        cycleId,
        pageable
    ).map(EvaluationTaskResponse::toResponse);
  }






}
