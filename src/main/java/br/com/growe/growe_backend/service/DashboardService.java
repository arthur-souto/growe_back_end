package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.domain.User;
import br.com.growe.growe_backend.dtos.response.*;
import br.com.growe.growe_backend.repository.AssessmentAnswerRepository;
import br.com.growe.growe_backend.repository.AssessmentRepository;
import br.com.growe.growe_backend.repository.CompanyMembersRepository;
import br.com.growe.growe_backend.utils.CompanyMemberUtils;
import br.com.growe.growe_backend.utils.CompanyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AssessmentAnswerRepository assessmentAnswerRepository;
    private final CompanyMemberUtils companyMemberUtils;
    private final CompanyUtils companyUtils;
    private final AssessmentRepository assessmentRepository;
    private final CompanyMembersRepository companyMembersRepository;
    private final PermissionsService permissionsService;

    @Transactional(readOnly = true)
    public PersonalDashboardResponse getMyDashboard(UserPrincipal userPrincipal, String slug) {

        final var company = companyUtils.findCompanyBySlug(slug);
        final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(
                userPrincipal.user().getId(),
                company.getId()
        );

        return this.getPersonalDashboard(member);

    }

    @Transactional(readOnly = true)
    public PersonalDashboardResponse getMemberDashboard(String slug, UUID memberId, UserPrincipal userPrincipal) {

        final var company = companyUtils.findCompanyBySlug(slug);
        final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(
                userPrincipal.user().getId(),
                company.getId()
        );

        permissionsService.hasAdministrativeAccess(member);

        final var target = companyMemberUtils.findCompanyMemberById(memberId);

        return this.getPersonalDashboard(target);
    }

    private PersonalDashboardResponse getPersonalDashboard(
            CompanyMember member
    ) {

        UUID memberId = member.getId();
        BigDecimal overall = assessmentAnswerRepository
                .findOverallAvgByEvaluatedMemberId(memberId)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);


        List<CycleAverageResponse> cycleAvg = assessmentAnswerRepository
                .findAvgPerCycleByEvaluatedMemberId(memberId)
                .stream()
                .map(avg -> new CycleAverageResponse(
                        avg.cycleId(),
                        avg.cycleName(),
                        avg.cycleStartDate(),
                        avg.avgScore()
                ))
                .toList();

        List<DashboardCommentResponse> assessmentComments = assessmentRepository
                .findAssessmentCommentsForMember(memberId, PageRequest.of(0, 5)).stream()
                .map(a -> new DashboardCommentResponse(
                        a.getEvaluator().getUser().getFullName(),
                        a.getAssessmentType(),
                        a.getCycle().getName(),
                        null,
                        a.getComment(),
                        a.getCreatedAt()
                ))
                .toList();


        List<DashboardCommentResponse> answerComments = assessmentAnswerRepository
                .findAnswerCommentsForMember(
                        memberId,
                        PageRequest.of(0, 5)
                        ).stream()
                .map(aa -> new DashboardCommentResponse(
                        aa.getAssessment().getEvaluator().getUser().getFullName(),
                        aa.getAssessment().getAssessmentType(),
                        aa.getAssessment().getCycle().getName(),
                        aa.getCompetency().getName(),
                        aa.getComment(),
                        aa.getAssessment().getCreatedAt()
                )).toList();

        List<DashboardCommentResponse> recent = Stream.concat(
                assessmentComments.stream(),
                answerComments.stream()
        ).sorted(
                Comparator.comparing(
                        DashboardCommentResponse::createdAt).reversed()
                )
                .limit(5).toList();

        return new PersonalDashboardResponse(
                memberId,
                member.getUser().getFullName(),
                overall,
                cycleAvg,
                recent
        );
    }

    @Transactional(readOnly = true)
    public List<TeamMemberSummaryResponse> getTeamDashboard(
            String slug,
            UserPrincipal userPrincipal
    ) {

        final var company = companyUtils.findCompanyBySlug(slug);
        final var member = companyMemberUtils.findCompanyMemberByUserAndCompany(userPrincipal.user().getId(), company.getId());

        permissionsService.hasAdministrativeAccess(member);

        List<CompanyMember> members = companyMembersRepository.findAllByCompany_Id(company.getId());

        Map<UUID, BigDecimal> avgByMember = assessmentAnswerRepository
                .findAvgPerMemberByCompanyId(company.getId()).stream()
                .collect(
                        Collectors.toMap(
                                AvgPerMemberResponse::id,
                                AvgPerMemberResponse::avg
                        )
                );

        return members.stream()
                .map(m -> new TeamMemberSummaryResponse(
                        m.getId(),
                        m.getUser().getFullName().trim(),
                        m.getRole(),
                        avgByMember.get(m.getId())
                ))
                .sorted(Comparator.comparing(TeamMemberSummaryResponse::memberName))
                .toList();
    }
}

