package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.CompanyMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyMembersRepository extends JpaRepository<CompanyMembers, UUID> {
}
