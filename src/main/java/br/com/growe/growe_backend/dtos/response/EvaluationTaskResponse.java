package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.EvaluationTask;
import br.com.growe.growe_backend.rules.AssessmentType;
import br.com.growe.growe_backend.rules.TaskStatus;

import java.time.Instant;
import java.util.UUID;

public record EvaluationTaskResponse(
    UUID id,
    UUID cycleId,
    ResumeMemberTaskResponse creatorName,
    ResumeMemberTaskResponse evaluatorName,
    ResumeMemberTaskResponse evaluatedName,
    AssessmentType assessmentType,
    TaskStatus status,
    Instant deadline,
    Instant completedAt,
    Instant createdAt
) {

  public static EvaluationTaskResponse toResponse(EvaluationTask task) {
    return new EvaluationTaskResponse(
        task.getId(),
        task.getCycle().getId(),
        new ResumeMemberTaskResponse(
            task.getCreatedBy().getId(),
            task.getCreatedBy().getUser().getFullName(),
            task.getCreatedBy().getUser().getEmail()
        ),
        new ResumeMemberTaskResponse(
            task.getEvaluator().getId(),
            task.getEvaluator().getUser().getFullName(),
            task.getEvaluator().getUser().getEmail()
        ),
        new ResumeMemberTaskResponse(
            task.getEvaluated().getId(),
            task.getEvaluated().getUser().getFullName(),
            task.getEvaluated().getUser().getEmail()
        ),
        task.getAssessmentType(),
        task.getStatus(),
        task.getDeadline(),
        task.getCompletedAt(),
        task.getCreatedAt()
    );
  }
}
