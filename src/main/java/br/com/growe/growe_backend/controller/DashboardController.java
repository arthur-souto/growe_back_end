package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.domain.Company;
import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.dtos.response.PersonalDashboardResponse;
import br.com.growe.growe_backend.dtos.response.TeamMemberSummaryResponse;
import br.com.growe.growe_backend.service.DashboardService;
import br.com.growe.growe_backend.service.PermissionsService;
import br.com.growe.growe_backend.utils.CompanyMemberUtils;
import br.com.growe.growe_backend.utils.CompanyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies/{slug}/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final PermissionsService permissionsService;
    private final DashboardService dashboardService;
    private final CompanyMemberUtils companyMemberUtils;
    private final CompanyUtils companyUtils;

    @GetMapping
    public PersonalDashboardResponse getMyDashboard(
            @PathVariable String slug,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        return dashboardService.getMyDashboard(
              userPrincipal,slug
        );
    }

    // GET /{memberId} — any member's dashboard (admin only)
    // Spring resolves literal "team" before {memberId}, so /team is safe
    @GetMapping("/{memberId}")
    public PersonalDashboardResponse getMemberDashboard(
            @PathVariable String slug,
            @PathVariable UUID memberId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        return dashboardService.getMemberDashboard(
                slug,
                memberId,
                userPrincipal
        );
    }

    // GET /team — team overview (admin only)
    @GetMapping("/team")
    public List<TeamMemberSummaryResponse> getTeamDashboard(
            @PathVariable String slug,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return dashboardService.getTeamDashboard(
                slug,
                userPrincipal
        );
    }
}
