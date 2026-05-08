package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.Assessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {

  List<Assessment> findAllByCycle_Id(UUID cycleId);

  Page<Assessment> findAllByCycle_Id(UUID cycleId, Pageable pageable);

  List<Assessment> findAllByEvaluated_Id(UUID evaluatedId);

  Page<Assessment> findAllByEvaluated_Id(UUID evaluatedId, Pageable pageable);

  Page<Assessment> findAllByEvaluator_Id(UUID evaluatorId, Pageable pageable);

  Optional<Assessment> findByCycle_IdAndEvaluator_IdAndEvaluated_Id(UUID cycleId, UUID evaluatorId, UUID evaluatedId);

  Optional<Assessment> findByTask_Id(UUID taskId);
}
