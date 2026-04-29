package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.rules.CompanyRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyMembersRepository extends JpaRepository<CompanyMember, UUID> {

  Optional<CompanyMember> findByUser_idAndCompany_id(UUID userId, UUID companyID);

  @Query("""
    SELECT m from CompanyMember m
    JOIN m.company c 
        WHERE c.slug =:slug
            AND c.isActive =:isActive
      
     """
  )
  Page<CompanyMember> findAllBySlug(@Param("slug") String slug, @Param("isActive") boolean isActive, Pageable pageable);

}
