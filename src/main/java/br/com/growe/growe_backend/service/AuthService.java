package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.SignInRequest;
import br.com.growe.growe_backend.dtos.response.SignInResponse;
import br.com.growe.growe_backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;


@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final TokenService tokenService;

  @Transactional
  public SignInResponse signIn(SignInRequest req, HttpServletResponse response) {

    final var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.email(), req.password())
    );

    final var principal = (UserPrincipal) authentication.getPrincipal();
    final var token = tokenService.generateToken(principal);

    final var cookie = ResponseCookie.from("access_token", token)
        .httpOnly(true)
        .secure(false)    // true in production
        .path("/")
        .maxAge(3600)
        .sameSite("Lax")  //  works cross-origin in dev
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    final var user = principal.user();

    user.setLastLoginAt(Instant.now());
    userRepository.save(user);

    return SignInResponse.from(user);
  }


  public void logout(HttpServletResponse response) {
    final var cookie = ResponseCookie.from("access_token", "")
        .httpOnly(true)
        .secure(false)    // true in production
        .path("/")
        .maxAge(0)        //  deletes the cookie
        .sameSite("Lax")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
