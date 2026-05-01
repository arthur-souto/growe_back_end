package br.com.growe.growe_backend.scheduler;

import br.com.growe.growe_backend.repository.EvaluationCycleRepository;
import br.com.growe.growe_backend.service.EvaluationCycleService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor

public class EvaluationCycleScheduler {

  private final EvaluationCycleService evaluationCycleService;

  @Scheduled(cron = "0 0 * * * *")
  @Transactional
  public void updateCyclesStatuses() {
   evaluationCycleService.refreshStatuses();
  }

}
