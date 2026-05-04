package br.com.growe.growe_backend.dtos.request;

import br.com.growe.growe_backend.rules.AssessmentType;
import br.com.growe.growe_backend.validator.ValidAssessmentParticipants;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@ValidAssessmentParticipants
public record CreateEvaluationTaskRequest(

    @NotNull(message = "Cycle ID is required")
    UUID cycleId,

    @NotNull(message = "Evaluator ID is required")
    UUID evaluatorId,

    @NotNull(message = "Evaluated ID is required")
    UUID evaluatedId,

    @NotNull(message = "Assessment type is required")
    AssessmentType assessmentType,

    @Future(message = "Deadline must be in the future")
    @NotNull(message = "Deadline is required")
    Instant deadline

) {}
