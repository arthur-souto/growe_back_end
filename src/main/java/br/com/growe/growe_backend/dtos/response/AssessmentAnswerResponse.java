package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.AssessmentAnswer;

import java.math.BigDecimal;
import java.util.UUID;

public record AssessmentAnswerResponse(
    UUID competencyId,
    String competencyName,
    BigDecimal score,
    String comment
) {
  public static AssessmentAnswerResponse toResponse(AssessmentAnswer answer) {
    return new AssessmentAnswerResponse(
        answer.getCompetency().getId(),
        answer.getCompetency().getName(),
        answer.getScore(),
        answer.getComment()
    );
  }
}
