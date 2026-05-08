package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.Assessment;
import br.com.growe.growe_backend.rules.AssessmentType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AssessmentResponse(
    UUID id,
    UUID cycleId,
    ResumeMemberTaskResponse evaluator,
    ResumeMemberTaskResponse evaluated,
    BigDecimal score,
    String comment,
    AssessmentType assessmentType,
    UUID taskId,
    Instant createdAt,
    Instant updatedAt
) {

  public static AssessmentResponse toResponse(Assessment assessment) {
    return new AssessmentResponse(
        assessment.getId(),
        assessment.getCycle().getId(),
        new ResumeMemberTaskResponse(
            assessment.getEvaluator().getId(),
            assessment.getEvaluator().getUser().getFullName(),
            assessment.getEvaluator().getUser().getEmail()
        ),
        new ResumeMemberTaskResponse(
            assessment.getEvaluated().getId(),
            assessment.getEvaluated().getUser().getFullName(),
            assessment.getEvaluated().getUser().getEmail()
        ),
        assessment.getScore(),
        assessment.getComment(),
        assessment.getAssessmentType(),
        assessment.getTask() != null ? assessment.getTask().getId() : null,
        assessment.getCreatedAt(),
        assessment.getUpdatedAt()
    );
  }
}
