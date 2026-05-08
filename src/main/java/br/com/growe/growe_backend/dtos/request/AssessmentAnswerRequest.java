package br.com.growe.growe_backend.dtos.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record AssessmentAnswerRequest(

    @NotNull(message = "Competency ID is required")
    UUID competencyId,

    @NotNull(message = "Score is required")
    @DecimalMin(value = "1.0", message = "Score must be at least 1")
    @DecimalMax(value = "5.0", message = "Score must be at most 5")
    BigDecimal score,

    @Size(max = 500)
    String comment

) {}
