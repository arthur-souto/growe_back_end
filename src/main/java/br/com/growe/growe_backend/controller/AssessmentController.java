package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.ImproveCommentRequest;
import br.com.growe.growe_backend.dtos.request.SubmitAssessmentRequest;
import br.com.growe.growe_backend.dtos.response.AssessmentResponse;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.dtos.response.ImproveCommentResponse;
import br.com.growe.growe_backend.service.AssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assessments")
@RequiredArgsConstructor
public class AssessmentController {

  private final AssessmentService assessmentService;

  @PostMapping("/improve/comment")
  public ImproveCommentResponse improveComment(
          @RequestBody @Valid ImproveCommentRequest req
  ) {
    return assessmentService.improveComment(req);
  }

  @PostMapping("/submit/{taskId}")
  public IdResponse submitAssessment(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable UUID taskId,
      @RequestBody @Valid SubmitAssessmentRequest req
  ) {
    return assessmentService.submitAssessment(userPrincipal, taskId, req);
  }

  @GetMapping("/cycle/{cycleId}")
  public Page<AssessmentResponse> findAllByCycle(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable UUID cycleId,
      Pageable pageable
  ) {
    return assessmentService.findAllByCycle(userPrincipal, cycleId, pageable);
  }

  @GetMapping("/evaluated/{evaluatedId}")
  public Page<AssessmentResponse> findAllByEvaluated(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @PathVariable UUID evaluatedId,
      Pageable pageable
  ) {
    return assessmentService.findAllByEvaluated(userPrincipal, evaluatedId, pageable);
  }

  @GetMapping("/evaluator/{evaluatorId}")
  public Page<AssessmentResponse> findAllByEvaluator(
          @AuthenticationPrincipal UserPrincipal userPrincipal,
          @PathVariable UUID evaluatorId,
          Pageable pageable
  ) {
    return assessmentService.findAllByEvaluator(userPrincipal, evaluatorId, pageable);
  }
}
