package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.SignUpRequest;
import br.com.growe.growe_backend.dtos.response.SignUpResponse;
import br.com.growe.growe_backend.dtos.response.UserDetailsResponse;
import br.com.growe.growe_backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final CookieService cookieService;

  @Transactional
  public SignUpResponse signUp(SignUpRequest request, HttpServletResponse response) {

    final var user = SignUpRequest.toEntity(request);
    final var token = tokenService.generateToken((new UserPrincipal(user)));
    final var savedUser = userRepository.save(user);
    final var cookie = cookieService.generateCookie(token);

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return SignUpResponse.from(savedUser);
  }

  @Transactional(readOnly = true)
  public UserDetailsResponse myInformations (UserPrincipal principal) {
    return UserDetailsResponse.toResponse(principal.user());
  }
}
