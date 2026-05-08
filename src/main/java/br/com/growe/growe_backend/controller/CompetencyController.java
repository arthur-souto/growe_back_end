package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.AddCompetencyToCycleRequest;
import br.com.growe.growe_backend.dtos.request.CreateCompetencyRequest;
import br.com.growe.growe_backend.dtos.response.CompetencyResponse;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.service.CompetencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/competencies")
@RequiredArgsConstructor
public class CompetencyController {

  private final CompetencyService competencyService;

  @PostMapping("/{slug}")
  @ResponseStatus(HttpStatus.CREATED)
  public IdResponse createCompetency(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable String slug,
      @RequestBody @Valid CreateCompetencyRequest req
  ) {
    return competencyService.createCompetency(userPrincipal, slug, req);
  }

  @GetMapping("/{slug}")
  public Page<CompetencyResponse> findAllByCompany(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable String slug,
      Pageable pageable
  ) {
    return competencyService.findAllByCompany(userPrincipal, slug, pageable);
  }

  @DeleteMapping("/{slug}/{competencyId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCompetency(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable String slug,
      @PathVariable UUID competencyId
  ) {
    competencyService.deleteCompetency(userPrincipal, slug, competencyId);
  }

  @PostMapping("/cycle/{cycleId}")
  @ResponseStatus(HttpStatus.CREATED)
  public IdResponse addToCycle(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable UUID cycleId,
      @RequestBody @Valid AddCompetencyToCycleRequest req
  ) {
    return competencyService.addToCycle(userPrincipal, cycleId, req);
  }

  @DeleteMapping("/cycle/{cycleId}/{competencyId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeFromCycle(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable UUID cycleId,
      @PathVariable UUID competencyId
  ) {
    competencyService.removeFromCycle(userPrincipal, cycleId, competencyId);
  }

  @GetMapping("/cycle/{cycleId}")
  public Page<CompetencyResponse> findAllByCycle(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable UUID cycleId,
      Pageable pageable
  ) {
    return competencyService.findAllByCycle(userPrincipal, cycleId, pageable);
  }
}
