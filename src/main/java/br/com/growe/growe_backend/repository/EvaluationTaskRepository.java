package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.EvaluationTask;
import br.com.growe.growe_backend.rules.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvaluationTaskRepository extends JpaRepository<EvaluationTask, UUID> {

  Page<EvaluationTask> findAllByCycle_Id(UUID cycleId, Pageable pageable);

  List<EvaluationTask> findAllByEvaluator_IdAndStatus(UUID evaluatorId, TaskStatus status);

  @Query("""
    SELECT e FROM EvaluationTask e 
    WHERE (e.evaluator.id = :userId OR e.evaluated.id = :userId) 
    AND e.cycle.id = :cycleId
""")
  Page<EvaluationTask> findAllByUserAndCycle(
      @Param("userId") UUID userId,
      @Param("cycleId") UUID cycleId,
      Pageable pageable
  );

  Optional<EvaluationTask> findByCycle_IdAndEvaluator_IdAndEvaluated_Id(UUID cycleId, UUID evaluatorId, UUID evaluatedId);
}
