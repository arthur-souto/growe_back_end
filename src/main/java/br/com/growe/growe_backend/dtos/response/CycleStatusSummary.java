package br.com.growe.growe_backend.dtos.response;

public record CycleStatusSummary(
    int activated,
    int closed
) {
}
