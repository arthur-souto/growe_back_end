package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.SignUpRequest;
import br.com.growe.growe_backend.dtos.response.SignUpResponse;
import br.com.growe.growe_backend.dtos.response.UserDetailsResponse;
import br.com.growe.growe_backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

  private final UserService usersService;

  @PostMapping("/sign-up")
  public SignUpResponse signUp(@RequestBody @Valid SignUpRequest req, HttpServletResponse response) {
    return usersService.signUp(req, response);
  }

  @GetMapping("/me")
  public UserDetailsResponse me(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    final var principal = userPrincipal;
    return usersService.myInformation(principal);
  }

}
