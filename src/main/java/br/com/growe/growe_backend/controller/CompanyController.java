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
  public CreateCompanyResponse createCompany(
      @RequestBody @Valid CreateCompanyRequest req,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    return companyService.createCompany(req, userPrincipal);
  }

  @GetMapping("/my-companies")
  public Page<ResumeCompanyResponse> findAllCompaniesByOwner(
      @AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {

    return companyService.findCompanies(userPrincipal, pageable);
  }

  @PutMapping("/{slug}")
  public IdResponse updateCompany(
      @PathVariable String slug,
      @RequestBody @Valid UpdateCompanyRequest req,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {

    return companyService.updateCompany(slug, userPrincipal, req);
  }


  @DeleteMapping("/{slug}")
  public void deleteCompany(
      @PathVariable String slug,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {

    companyService.deleteCompany(slug, userPrincipal);
  }

  @GetMapping("/{slug}")
  public CompanyDetailsResponse getCompany(
      @PathVariable String slug,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {

   return companyService.findBySlug(slug, userPrincipal);
  }

  @GetMapping("/{slug}/members")
  public Page<ResumeMemberResponse> findAllMembers(
      @PathVariable String slug,
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      Pageable pageable) {

    return companyMemberService.findMembersBySlug(slug, userPrincipal, pageable);
  }

  @PostMapping("/{slug}/add-member")
  public IdResponse addMember(
      @PathVariable String slug,
      CreateCompanyMemberRequest req,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    return companyMemberService.createEmployeeMember(slug, req ,userPrincipal);
  }

  @PatchMapping("/{slug}/update-member/{memberId}")
  public IdResponse updateMember(
      @RequestBody UpdateCompanyMemberRequest req,
      @PathVariable UUID memberId,
      @PathVariable String slug,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    return companyMemberService.updateEmployeeMember(userPrincipal, req, memberId, slug);
  }

  @DeleteMapping("/{slug}/members/{memberId}")
  public void deleteMember(
      @PathVariable UUID memberId,
      @PathVariable String slug,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    companyMemberService.deleteEmployeeMember(userPrincipal, memberId, slug);
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
