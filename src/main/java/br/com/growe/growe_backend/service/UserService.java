package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.SignUpRequest;
import br.com.growe.growe_backend.dtos.response.SignUpResponse;
import br.com.growe.growe_backend.dtos.response.UserDetailsResponse;
import br.com.growe.growe_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final TokenService tokenService;

  @Transactional
  public SignUpResponse signUp(SignUpRequest request) {

    final var user = SignUpRequest.toEntity(request);
    final var token = tokenService.generateToken((new UserPrincipal(user)));

    userRepository.save(user);

    return new SignUpResponse(token);
  }

  @Transactional(readOnly = true)
  public UserDetailsResponse myInformations (UserPrincipal principal) {
    return UserDetailsResponse.toResponse(principal.user());
  }
}
