package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.dtos.request.SignUpRequest;
import br.com.growe.growe_backend.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

  private final UsersService usersService;

  @PostMapping("/sign-up")
  public void signUp(@RequestBody @Valid SignUpRequest req) {
    usersService.signUp(req);
  }



}
