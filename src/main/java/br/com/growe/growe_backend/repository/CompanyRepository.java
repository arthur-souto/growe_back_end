package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.Company;
import br.com.growe.growe_backend.rules.CompanyRole;
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
public interface CompanyRepository extends JpaRepository<Company, UUID> {

  Optional<Company> findBySlug(String slug);

  @Query("""
    SELECT c FROM Company c
        JOIN c.members cm
            WHERE cm.user.id = :userId
                AND c.isActive = :isActive
                    AND cm.role IN :roles
""")
  Page<Company> findCompanies(
      @Param("userId") UUID userId,
      @Param("roles") List<CompanyRole> roles,
      @Param("isActive") boolean isActive,
      Pageable pageable
  );

  boolean existsByCnpj(String cnpj);
}
