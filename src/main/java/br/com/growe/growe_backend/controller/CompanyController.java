package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.CreateCompanyRequest;
import br.com.growe.growe_backend.dtos.request.UpdateCompanyRequest;
import br.com.growe.growe_backend.dtos.response.CompanyDetailsResponse;
import br.com.growe.growe_backend.dtos.response.CreateCompanyResponse;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.dtos.response.ResumeCompanyResponse;
import br.com.growe.growe_backend.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @PostMapping("/create-company")
  public CreateCompanyResponse createCompany(@RequestBody @Valid CreateCompanyRequest req, Authentication authentication) {

    return companyService.createCompany(req, (UserPrincipal) authentication.getPrincipal());
  }

  @GetMapping("/my-companies")
  public Page<ResumeCompanyResponse> findAllCompaniesByOwner(Authentication authentication, Pageable pageable) {

    return companyService.findCompaniesByOwner(( UserPrincipal) authentication.getPrincipal(), pageable);
  }

  @PutMapping("/{slug}")
  public IdResponse updateCompany(
      @PathVariable String slug,
      @RequestBody @Valid UpdateCompanyRequest req,
      Authentication authentication
  ) {

    return companyService.updateCompany(slug, (UserPrincipal) authentication.getPrincipal(), req);
  }


  @DeleteMapping("/{slug}")
  public void deleteCompany(
      @PathVariable String slug,
      Authentication authentication
  ) {

    companyService.deleteCompany(slug, (UserPrincipal) authentication.getPrincipal());
  }

  @GetMapping("/{slug}")
  public CompanyDetailsResponse getCompany(
      @PathVariable String slug,
      Authentication authentication
  ) {

   return companyService.findBySlug(slug, (UserPrincipal) authentication.getPrincipal());
  }

}
