package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.domain.User;
import br.com.growe.growe_backend.dtos.request.CreateCompanyMemberRequest;
import br.com.growe.growe_backend.dtos.request.ImportSummaryResponse;
import br.com.growe.growe_backend.dtos.request.ParsedMember;
import br.com.growe.growe_backend.dtos.request.UpdateCompanyMemberRequest;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.dtos.response.ResumeMemberResponse;
import br.com.growe.growe_backend.exceptions.BusinessException;
import br.com.growe.growe_backend.exceptions.ConflictException;
import br.com.growe.growe_backend.exceptions.ResourceNotFoundException;
import br.com.growe.growe_backend.repository.CompanyMembersRepository;
import br.com.growe.growe_backend.repository.UserRepository;
import br.com.growe.growe_backend.rules.CompanyRole;
import br.com.growe.growe_backend.rules.Role;
import br.com.growe.growe_backend.utils.CompanyMemberUtils;
import br.com.growe.growe_backend.utils.CompanyUtils;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyMemberService {

  private final UserRepository userRepository;
  private final CompanyMembersRepository companyMembersRepository;
  private final PasswordEncoder passwordEncoder;
  private final LLMParserService llmParserService;
  private final CompanyUtils companyUtils;
  private final CompanyMemberUtils companyMemberUtils;

  private static final int START_SKIPPED_VALUE = 0;
  private static final int START_CREATED_VALUE = 0;

  @Transactional(readOnly = true)
  public Page<ResumeMemberResponse> findMembersBySlug(String slug, UserPrincipal userPrincipal, Pageable pageable) {

    final var company = companyUtils.findCompanyBySlug(slug);

    final var memberCompany = companyMemberUtils.findCompanyMemberByUserAndCompany(userPrincipal.user().getId(), company.getId());

    PermissionsService.validateHighPermission(memberCompany);

    Page<CompanyMember> members = companyMembersRepository
        .findAllBySlug(slug, true, pageable);

    return members.map(m -> ResumeMemberResponse.fromEntity(m.getUser(), m));
  }

  private void valideNewRoleForMember(CompanyRole role) {
    if(role.equals(CompanyRole.OWNER)) {
      throw new ConflictException("You can`t create/update an OWNER");
    }
  }

  @Transactional
  public IdResponse createEmployeeMember(String slug, CreateCompanyMemberRequest req, UserPrincipal userPrincipal) {

    final var company = companyUtils.findCompanyBySlug(slug);

    final var memberCompany = companyMemberUtils.findCompanyMemberByUserAndCompany(userPrincipal.user().getId(), company.getId());

    PermissionsService.validateHighPermission(memberCompany);

    this.valideNewRoleForMember(req.role());

    final var user = User
        .builder()
        .fullName(req.fullName())
        .email(req.email())
        .role(Role.EMPLOYEE)
        .active(true)
        .password(passwordEncoder.encode(req.password()))
        .build();

    final var savedUser = userRepository.save(user);

    final var member = CompanyMember
        .builder()
        .company(company)
        .user(savedUser)
        .role(req.role())
        .build();

    final var savedMember = companyMembersRepository.save(member);

    return  new IdResponse(savedMember.getId());
  }

  private CompanyMember findCompanyMemberById(UUID memberId ) {
    return companyMembersRepository.findById(memberId)
        .orElseThrow(() -> new ResourceNotFoundException("Member not found", memberId.toString()));
  }


  @Transactional
  public IdResponse updateEmployeeMember(UserPrincipal userPrincipal, UpdateCompanyMemberRequest request, UUID memberId, String slug) {

    if(memberId.equals(userPrincipal.user().getId())) {
      throw new ConflictException("You can't edit yourself");
    }

    final var company = companyUtils.findCompanyBySlug(slug);
    final var requesterMember = companyMemberUtils.findCompanyMemberByUserAndCompany(userPrincipal.user().getId(), company.getId());

    PermissionsService.validateHighPermission(requesterMember);

    final var member = findCompanyMemberById(memberId);

    this.valideNewRoleForMember(request.role());

    member.setRole(request.role());

    return new IdResponse(
        companyMembersRepository.save(member).getId()
    );
  }

  @Transactional
  public void deleteEmployeeMember(UserPrincipal userPrincipal, UUID memberId, String slug) {

    if(memberId.equals(userPrincipal.user().getId())) {
      throw new ConflictException("You can't remove yourself");
    }

    final var company = companyUtils.findCompanyBySlug(slug);
    final var requesterMember = companyMemberUtils.findCompanyMemberByUserAndCompany(userPrincipal.user().getId(), company.getId());

    PermissionsService.validateHighPermission(requesterMember);

    final var member = this.findCompanyMemberById(memberId);
    final var memberUser = member.getUser().getId();

    companyMembersRepository.delete(member);
    userRepository.deleteById(memberUser);
  }


  private String extractTextFromFile(MultipartFile file) {

    if(file == null) {
      throw new BusinessException("File is null", HttpStatus.BAD_REQUEST, "NULL_EXCEPTION");
    }

    var filename = file.getOriginalFilename();

    if(filename == null) {
      throw new BusinessException("File Name is null", HttpStatus.BAD_REQUEST, "NULL_EXCEPTION");
    }

    try {
      if (filename.toLowerCase().endsWith(".pdf")) {
        try (PDDocument doc = Loader.loadPDF(
            new RandomAccessReadBuffer(file.getBytes()))) {
          PDFTextStripper stripper = new PDFTextStripper();
          stripper.setSortByPosition(true);
          return stripper.getText(doc);
        }
      } else if (filename.toLowerCase().endsWith(".docx")) {
        try (XWPFDocument doc = new XWPFDocument(file.getInputStream());
             XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
          return extractor.getText();
        }
      } else {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
      }
    } catch (IOException e) {
      throw new BusinessException("Failed to read file", HttpStatus.BAD_REQUEST, "ERROR_FILE");
    }
  }

  @Transactional
  public ImportSummaryResponse importMembersFromFile(
      String slug,
      MultipartFile file,
      UserPrincipal userPrincipal
  ) {

    final var company = companyUtils.findCompanyBySlug(slug);

    final var memberCompany = companyMemberUtils.findCompanyMemberByUserAndCompany(
        userPrincipal.user().getId(), company.getId()
    );

    PermissionsService.validateHighPermission(memberCompany);

    String rawText = extractTextFromFile(file);
    List<ParsedMember> parsed = llmParserService.extractMembers(rawText);

    int created = START_CREATED_VALUE;
    int skipped = START_SKIPPED_VALUE;

    List<String> errors = new ArrayList<>();

    for (ParsedMember p : parsed) {
      if (userRepository.existsByEmail(p.email())) {
        skipped++;
        errors.add("Email already exists: " + p.email());
        continue;
      }
      try {
        valideNewRoleForMember(p.role());

        final var user = User.builder()
            .fullName(p.fullName())
            .email(p.email())
            .role(Role.EMPLOYEE)
            .active(true)
            .password(passwordEncoder.encode(p.password()))
            .build();

        final var savedUser = userRepository.save(user);

        final var member = CompanyMember.builder()
            .company(company)
            .user(savedUser)
            .role(p.role())
            .build();

        companyMembersRepository.save(member);
        created++;
      } catch (Exception e) {
        skipped++;
        errors.add("Failed for " + p.email() + ": " + e.getMessage());
      }
    }

    return new ImportSummaryResponse(created, skipped, errors);
  }

}
