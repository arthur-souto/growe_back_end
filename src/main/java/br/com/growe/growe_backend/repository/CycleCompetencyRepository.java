package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.CycleCompetency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CycleCompetencyRepository extends JpaRepository<CycleCompetency, UUID> {
  List<CycleCompetency> findAllByCycle_Id(UUID cycleId);
  boolean existsByCycle_IdAndCompetency_Id(UUID cycleId, UUID competencyId);
  void deleteByCycle_IdAndCompetency_Id(UUID cycleId, UUID competencyId);
}
