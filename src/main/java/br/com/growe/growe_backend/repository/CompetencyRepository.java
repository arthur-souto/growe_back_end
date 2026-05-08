package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.Competency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompetencyRepository extends JpaRepository<Competency, UUID> {
  Page<Competency> findAllByCompany_Id(UUID companyId, Pageable pageable);
  boolean existsByCompany_IdAndName(UUID companyId, String name);
}
