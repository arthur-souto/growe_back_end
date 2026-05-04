package br.com.growe.growe_backend.dtos.response;

import java.util.UUID;

public record ResumeMemberTaskResponse(
    UUID id,
    String fullName,
    String email
) {
}
