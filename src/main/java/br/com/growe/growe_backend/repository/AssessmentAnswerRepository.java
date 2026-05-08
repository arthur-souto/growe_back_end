package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.AssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssessmentAnswerRepository extends JpaRepository<AssessmentAnswer, UUID> {
  List<AssessmentAnswer> findAllByAssessment_Id(UUID assessmentId);
}
