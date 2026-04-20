package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.SignInRequest;
import br.com.growe.growe_backend.dtos.response.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  public SignInResponse signIn(SignInRequest req) {

    final var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.email(), req.password())
    );

    final var principal = (UserPrincipal) authentication.getPrincipal();
    final var token = tokenService.generateToken(principal);

    return new SignInResponse(token);
  }
}
