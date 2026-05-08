package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.Assessment;
import br.com.growe.growe_backend.rules.AssessmentType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AssessmentResponse(
    UUID id,
    UUID cycleId,
    String cycleName,
    ResumeMemberTaskResponse evaluator,
    ResumeMemberTaskResponse evaluated,
    BigDecimal avgScore,
    String comment,
    AssessmentType assessmentType,
    UUID taskId,
    List<AssessmentAnswerResponse> answers,
    Instant createdAt,
    Instant updatedAt
) {

  public static AssessmentResponse toResponse(Assessment assessment) {
    List<AssessmentAnswerResponse> answerResponses = assessment.getAnswers().stream()
        .map(AssessmentAnswerResponse::toResponse)
        .toList();

    BigDecimal avg = answerResponses.isEmpty() ? null :
        answerResponses.stream()
            .map(AssessmentAnswerResponse::score)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(answerResponses.size()), 1, RoundingMode.HALF_UP);

    return new AssessmentResponse(
        assessment.getId(),
        assessment.getCycle().getId(),
        assessment.getCycle().getName(),
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
        avg,
        assessment.getComment(),
        assessment.getAssessmentType(),
        assessment.getTask() != null ? assessment.getTask().getId() : null,
        answerResponses,
        assessment.getCreatedAt(),
        assessment.getUpdatedAt()
    );
  }
}
