package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.Assessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {

  List<Assessment> findAllByCycle_Id(UUID cycleId);

  Page<Assessment> findAllByCycle_Id(UUID cycleId, Pageable pageable);

  List<Assessment> findAllByEvaluated_Id(UUID evaluatedId);

  Page<Assessment> findAllByEvaluated_Id(UUID evaluatedId, Pageable pageable);

  Page<Assessment> findAllByEvaluator_Id(UUID evaluatorId, Pageable pageable);

  @Query("""
    SELECT a FROM Assessment a
    WHERE a.evaluated.id = :memberId
      AND a.comment IS NOT NULL AND TRIM(a.comment) <> ''
    ORDER BY a.createdAt DESC
""")
  List<Assessment> findAssessmentCommentsForMember(@Param("memberId") UUID memberId, Pageable pageable);
}
