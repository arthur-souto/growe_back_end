package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.rules.AssessmentType;

import java.time.Instant;

public record DashboardCommentResponse(
        String evaluatorName,
        AssessmentType assessmentType,
        String cycleName,
        String competencyName,   // null when source is Assessment.comment
        String comment,
        Instant createdAt        // Assessment.createdAt in both cases
) {
}
