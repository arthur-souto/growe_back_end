package br.com.growe.growe_backend.dtos.request;

import br.com.growe.growe_backend.validator.EndDateAfterStartDate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@EndDateAfterStartDate
public record CreateEvaluationCycleRequest(
    @NotBlank(message = "Evaluation cycle name is required")
    String name,

    @NotBlank(message = "Description is required")
    String description,

    @NotBlank(message = "Color is required")
    String color,

    @FutureOrPresent(message = "Start date must be in the present or future")
    @NotNull(message = "Start date is required")
    Instant startDate,

    @Future(message = "End date must be in the future")
    @NotNull(message = "End date is required")
    Instant endDate
) {}
