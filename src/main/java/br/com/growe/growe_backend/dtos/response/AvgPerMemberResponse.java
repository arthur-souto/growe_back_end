package br.com.growe.growe_backend.dtos.response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public record AvgPerMemberResponse(
        UUID id,
        BigDecimal avg
) {
    public AvgPerMemberResponse(UUID memberId, Double avg) {
        this (
            memberId,
                avg != null
                        ? BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO

        );
    }
}
