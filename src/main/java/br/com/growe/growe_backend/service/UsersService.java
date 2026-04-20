package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.dtos.request.SignUpRequest;
import br.com.growe.growe_backend.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsersService {

  private final UsersRepository usersRepository;

  @Transactional
  public void signUp(SignUpRequest request) {
    var user = request.toEntity(request);
    usersRepository.save(user);
  }
}
