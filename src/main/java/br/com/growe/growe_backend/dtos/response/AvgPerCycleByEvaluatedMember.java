package br.com.growe.growe_backend.dtos.response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

public record AvgPerCycleByEvaluatedMember(
        UUID cycleId,
        String cycleName,
        Instant cycleStartDate,
        BigDecimal avgScore
) {
    public AvgPerCycleByEvaluatedMember(UUID cycleId, String cycleName, Instant cycleStartDate, Double avgScore) {
        this(
                cycleId,
                cycleName,
                cycleStartDate,
                avgScore != null
                        ? BigDecimal.valueOf(avgScore).setScale(2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO
        );
    }
}