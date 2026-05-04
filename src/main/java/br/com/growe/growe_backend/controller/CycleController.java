package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.CreateEvaluationCycleRequest;
import br.com.growe.growe_backend.dtos.response.CycleResumeResponse;
import br.com.growe.growe_backend.dtos.response.CycleStatusSummary;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.service.EvaluationCycleService;
import br.com.growe.growe_backend.service.PermissionsService;
import br.com.growe.growe_backend.utils.CompanyMemberUtils;
import br.com.growe.growe_backend.utils.CompanyUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cycles")
@RequiredArgsConstructor
public class CycleController {

  private final CompanyUtils companyUtils;
  private final CompanyMemberUtils companyMemberUtils;
  private final EvaluationCycleService evaluationCycleService;

  @PostMapping("/{slug}/refresh")
  @ResponseStatus(HttpStatus.OK)
  public CycleStatusSummary refreshCycles(
      @PathVariable String slug,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    final var company = companyUtils.findCompanyBySlug(slug);

    final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(userPrincipal.user().getId(), company.getId());

    PermissionsService.hasAdministrativeAccess(member);

    return evaluationCycleService.refreshStatusesBySlug(slug);
  }

  @PostMapping("/create/{slug}")
  public IdResponse createCycle(
      @RequestBody @Valid CreateEvaluationCycleRequest request,
      @PathVariable String slug,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    return evaluationCycleService.createCycle(userPrincipal, request, slug);
  }

  @GetMapping("/{slug}")
  public Page<CycleResumeResponse> findAllCyclesBySlug(
      @PathVariable String slug,
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      Pageable pageable
  ) {

    return evaluationCycleService.findAllCyclesBySlug(slug, userPrincipal, pageable);
  }
}
