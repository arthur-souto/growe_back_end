package br.com.growe.growe_backend.dtos.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record CycleAverageResponse(
        UUID cycleId,
        String cycleName,
        Instant cycleStartDate,
        BigDecimal avgScore
) {}


