package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.CreateEvaluationTaskRequest;
import br.com.growe.growe_backend.dtos.response.EvaluationTaskResponse;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.service.EvaluationTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class EvaluationTaskController {

  private final EvaluationTaskService evaluationTaskService;

  @PostMapping("create-task/{slug}/cycle/{cycleId}")
  public IdResponse createTask(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody @Valid CreateEvaluationTaskRequest req,
      @PathVariable UUID cycleId,
      @PathVariable String slug
      ) {

    return evaluationTaskService.createTask(userPrincipal, cycleId, slug, req );
  }

  @GetMapping("/{cycleId}")
  public Page<EvaluationTaskResponse> findAllByCycle(
      @PathVariable UUID cycleId,
      Pageable pageable,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    return evaluationTaskService.findAllByCycle(cycleId, pageable, userPrincipal);
  }

}
