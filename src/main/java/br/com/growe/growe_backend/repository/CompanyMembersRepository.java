package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.CompanyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyMembersRepository extends JpaRepository<CompanyMember, UUID> {

  CompanyMember findByUser_idAndCompany_id(UUID userId, UUID companyID);

}
