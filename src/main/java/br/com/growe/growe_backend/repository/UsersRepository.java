package br.com.growe.growe_backend.repository;

import br.com.growe.growe_backend.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

  boolean existsByEmail(String email);

  Optional<Users> findByEmail(String email);
}
