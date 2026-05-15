package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.AssessmentAnswer;
import br.com.growe.growe_backend.dtos.response.AvgPerCycleByEvaluatedMember;
import br.com.growe.growe_backend.dtos.response.AvgPerMemberResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentAnswerRepository extends JpaRepository<AssessmentAnswer, UUID> {
  List<AssessmentAnswer> findAllByAssessment_Id(UUID assessmentId);

  @Query("SELECT AVG(aa.score) FROM AssessmentAnswer aa WHERE aa.assessment.evaluated.id = :memberId")
  Optional<BigDecimal> findOverallAvgByEvaluatedMemberId(@Param("memberId") UUID memberId);

  @Query("""
    SELECT new br.com.growe.growe_backend.dtos.response.AvgPerCycleByEvaluatedMember(
        a.cycle.id,
        a.cycle.name,
        a.cycle.startDate,
        AVG(aa.score)
    )
    FROM AssessmentAnswer aa JOIN aa.assessment a
    WHERE a.evaluated.id = :memberId
    GROUP BY a.cycle.id, a.cycle.name, a.cycle.startDate
    ORDER BY a.cycle.startDate ASC
""")

  List<AvgPerCycleByEvaluatedMember> findAvgPerCycleByEvaluatedMemberId(@Param("memberId") UUID memberId);
  @Query("""
    SELECT new br.com.growe.growe_backend.dtos.response.AvgPerMemberResponse(
    a.evaluated.id,
    AVG(aa.score)
    )
    FROM AssessmentAnswer aa JOIN aa.assessment a
    WHERE a.evaluated.company.id = :companyId
    GROUP BY a.evaluated.id
""")
  List<AvgPerMemberResponse> findAvgPerMemberByCompanyId(@Param("companyId") UUID companyId);

  // Note: AssessmentAnswer has no createdAt; order by the parent Assessment.createdAt
  @Query("""
    SELECT aa FROM AssessmentAnswer aa
    WHERE aa.assessment.evaluated.id = :memberId
      AND aa.comment IS NOT NULL AND TRIM(aa.comment) <> ''
    ORDER BY aa.assessment.createdAt DESC
""")
  List<AssessmentAnswer> findAnswerCommentsForMember(@Param("memberId") UUID memberId, Pageable pageable);

}
