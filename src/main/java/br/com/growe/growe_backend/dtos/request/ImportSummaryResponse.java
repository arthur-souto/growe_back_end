package br.com.growe.growe_backend.dtos.request;

import java.util.List;

public record ImportSummaryResponse(int created, int skipped, List<String> erros) {
}
