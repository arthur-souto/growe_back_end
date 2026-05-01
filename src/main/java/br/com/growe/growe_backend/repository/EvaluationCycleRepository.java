package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.EvaluationCycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface EvaluationCycleRepository extends JpaRepository<EvaluationCycle, UUID> {

  @Modifying
  @Query("""
    UPDATE EvaluationCycle e
    SET e.isActive = false
    WHERE e.endDate <= :now
    AND e.isActive = true
""")
  int closeCycles(@Param("now") Instant now);

  @Modifying
  @Query("""
    UPDATE EvaluationCycle e
    SET e.isActive = true
    WHERE e.startDate <= :now
    AND e.endDate > :now
    AND e.isActive = false
""")
  int activateCycles(@Param("now") Instant now);

  @Modifying
  @Query("""
    UPDATE EvaluationCycle e
    SET e.isActive = true
    WHERE e.startDate <= :now
    AND e.endDate > :now
    AND e.isActive = false
    AND e.company.slug = :slug
""")
  int activateCyclesBySlug(@Param("now") Instant now, @Param("slug") String slug);

  @Modifying
  @Query("""
    UPDATE EvaluationCycle e
    SET e.isActive = false
    WHERE e.endDate <= :now
    AND e.isActive = true
    AND e.company.slug = :slug
""")
  int closeCyclesBySlug(@Param("now") Instant now, @Param("slug") String slug);

  Page<EvaluationCycle> findAllByCompany_slug(String slug, Pageable pageable);
}
