package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.dtos.request.SignInRequest;
import br.com.growe.growe_backend.dtos.response.SignInResponse;
import br.com.growe.growe_backend.dtos.response.UserDetailsResponse;
import br.com.growe.growe_backend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/sign-in")
  public SignInResponse signIn(@RequestBody @Valid SignInRequest req, HttpServletResponse response) {
    return authService.signIn(req, response);
  }

  @PostMapping("/logout")
  public void logout(HttpServletResponse response) {
    authService.logout(response);
  }

}
