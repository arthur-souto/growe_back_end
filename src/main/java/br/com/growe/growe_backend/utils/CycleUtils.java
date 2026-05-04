package br.com.growe.growe_backend.utils;

import br.com.growe.growe_backend.domain.EvaluationCycle;
import br.com.growe.growe_backend.exceptions.ResourceNotFoundException;
import br.com.growe.growe_backend.repository.EvaluationCycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CycleUtils {

  private final EvaluationCycleRepository evaluationCycleRepository;

  public EvaluationCycle findEvaluationCycleById(UUID cycleId) {

    return evaluationCycleRepository
        .findById(cycleId)
        .orElseThrow(() -> new ResourceNotFoundException("Cycle not found", cycleId.toString()));
  }

}
