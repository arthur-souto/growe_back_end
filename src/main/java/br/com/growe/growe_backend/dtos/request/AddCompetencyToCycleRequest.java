package br.com.growe.growe_backend.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddCompetencyToCycleRequest(

    @NotNull(message = "Competency ID is required")
    UUID competencyId

) {}
