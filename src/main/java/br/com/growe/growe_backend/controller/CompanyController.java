package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.CreateCompanyRequest;
import br.com.growe.growe_backend.dtos.response.CreateCompanyResponse;
import br.com.growe.growe_backend.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @PostMapping("/create-company")
  public CreateCompanyResponse createCompany(@RequestBody @Valid CreateCompanyRequest req, Authentication authentication) {

    return companyService.createCompany(req, (UserPrincipal) authentication.getPrincipal());
  }

}
