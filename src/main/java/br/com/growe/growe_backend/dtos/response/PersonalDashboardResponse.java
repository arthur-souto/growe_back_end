package br.com.growe.growe_backend.dtos.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PersonalDashboardResponse(
        UUID memberId,
        String memberName,
        BigDecimal overallAvgScore,           // null if no assessments
        List<CycleAverageResponse> cycleAverages,   // ASC by startDate
        List<DashboardCommentResponse> recentComments // last 5, DESC by createdAt
) {
}
