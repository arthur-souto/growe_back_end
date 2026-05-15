package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.rules.CompanyRole;
import java.math.BigDecimal;
import java.util.UUID;

public record TeamMemberSummaryResponse(
        UUID memberId,
        String memberName,
        CompanyRole role,
        BigDecimal overallAvgScore  // null if no assessments
) {
}
