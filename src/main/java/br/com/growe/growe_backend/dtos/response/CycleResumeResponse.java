package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.rules.CompanyRole;

import java.time.Instant;
import java.util.UUID;

public record CycleResumeResponse(
    UUID id,
    String creatorName,
    String creatorProfile,
    CompanyRole creatorRole,
    String name,
    String description,
    String color,
    int tasksCount,
    boolean isActive,
    Instant startDate,
    Instant endDate,
    Instant createdAt
) {
}
