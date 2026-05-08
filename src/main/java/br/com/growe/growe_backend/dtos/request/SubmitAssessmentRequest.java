package br.com.growe.growe_backend.dtos.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record SubmitAssessmentRequest(

    @NotNull(message = "Score is required")
    @DecimalMin(value = "0.0", message = "Score must be at least 0")
    @DecimalMax(value = "5.0", message = "Score must be at most 5")
    BigDecimal score,

    @NotBlank(message = "Comment is required")
    @Size(max = 500, message = "Comment must be at most 500 characters")
    String comment

) {}
