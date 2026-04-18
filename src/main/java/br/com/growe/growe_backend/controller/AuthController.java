package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.dtos.SignInRequest;
import br.com.growe.growe_backend.dtos.SignInResponse;
import br.com.growe.growe_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  @PostMapping("/sign-in")
  public SignInResponse signIn(@RequestBody SignInRequest req) {
    return authService.signIn(req);
  }
}
