package br.com.growe.growe_backend.controller;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.dtos.request.*;
import br.com.growe.growe_backend.dtos.response.*;
import br.com.growe.growe_backend.service.CompanyMemberService;
import br.com.growe.growe_backend.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyMemberService companyMemberService;
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

  @GetMapping("/{slug}/members")
  public Page<ResumeMemberResponse> findAllMembers(@PathVariable String slug, Authentication authentication, Pageable pageable) {

    return companyMemberService.findMembersBySlug(slug, (UserPrincipal) authentication.getPrincipal() , pageable);
  }

  @PostMapping("/{slug}/add-member")
  public IdResponse addMember(@PathVariable String slug, CreateCompanyMemberRequest req, Authentication authentication) {

    return companyMemberService.createEmployeeMember(slug, req , (UserPrincipal) authentication.getPrincipal());
  }

  @PatchMapping("/{slug}/update-member/{memberId}")
  public IdResponse updateMember(@RequestBody UpdateCompanyMemberRequest req, @PathVariable UUID memberId, @PathVariable String slug, Authentication authentication) {

    return companyMemberService.updateEmployeeMember((UserPrincipal) authentication.getPrincipal(), req, memberId, slug);
  }

  @DeleteMapping("/{slug}/members/{memberId}")
  public void deleteMember(@PathVariable UUID memberId, @PathVariable String slug, Authentication authentication) {

    companyMemberService.deleteEmployeeMember((UserPrincipal) authentication.getPrincipal(), memberId, slug);
  }

  @PostMapping("/{slug}/members/import")
  public ImportSummaryResponse importMembers(
      @PathVariable String slug,
      @RequestParam("file") MultipartFile file,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {

    return companyMemberService.importMembersFromFile(slug, file, userPrincipal);

  }
}
