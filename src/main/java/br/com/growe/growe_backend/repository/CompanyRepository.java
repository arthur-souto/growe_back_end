package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

  Optional<CompanyRepository> findBySlug(String slug);

  boolean existsByCnpj(String cnpj);
}
