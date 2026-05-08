package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.Competency;

import java.time.Instant;
import java.util.UUID;

public record CompetencyResponse(
    UUID id,
    String name,
    String description,
    Instant createdAt
) {
  public static CompetencyResponse toResponse(Competency competency) {
    return new CompetencyResponse(
        competency.getId(),
        competency.getName(),
        competency.getDescription(),
        competency.getCreatedAt()
    );
  }
}
