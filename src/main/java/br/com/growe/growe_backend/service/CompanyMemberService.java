package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.domain.User;
import br.com.growe.growe_backend.dtos.request.CreateCompanyMemberRequest;
import br.com.growe.growe_backend.dtos.response.IdResponse;
import br.com.growe.growe_backend.dtos.response.ResumeMemberResponse;
import br.com.growe.growe_backend.exceptions.AccessDeniedException;
import br.com.growe.growe_backend.exceptions.ConflictException;
import br.com.growe.growe_backend.exceptions.ResourceNotFoundException;
import br.com.growe.growe_backend.repository.CompanyMembersRepository;
import br.com.growe.growe_backend.repository.CompanyRepository;
import br.com.growe.growe_backend.repository.UserRepository;
import br.com.growe.growe_backend.rules.CompanyRole;
import br.com.growe.growe_backend.rules.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyMemberService {

  private final CompanyRepository companyRepository;
  private final UserRepository userRepository;
  private final CompanyMembersRepository companyMembersRepository;
  private final PasswordEncoder passwordEncoder;

  public CompanyMember findCompanyMemberByUserAndCompany(UUID userId, UUID companyId) {

    return companyMembersRepository.findByUser_idAndCompany_id(userId, companyId)
        .orElseThrow(() -> new AccessDeniedException("You dont have permissions"));
  }

  @Transactional(readOnly = true)
  public Page<ResumeMemberResponse> findMembersBySlug(String slug, UserPrincipal userPrincipal, Pageable pageable) {

    final var company = companyRepository.findBySlug(slug)
        .orElseThrow(() -> new ResourceNotFoundException("Company not found", slug));

    final var memberCompany = this.findCompanyMemberByUserAndCompany(userPrincipal.user().getId(), company.getId());

    PermissionsService.validateIsOwner(memberCompany);

    Page<CompanyMember> members = companyMembersRepository
        .findAllBySlug(slug, true, pageable);

    return members.map(m -> ResumeMemberResponse.fromEntity(m.getUser(), m));
  }

  @Transactional
  public IdResponse createEmployeeMember(String slug, CreateCompanyMemberRequest req, UserPrincipal userPrincipal) {

    final var company = companyRepository.findBySlug(slug)
        .orElseThrow(() -> new ResourceNotFoundException("Company not found", slug));

    final var memberCompany = this.findCompanyMemberByUserAndCompany(userPrincipal.user().getId(), company.getId());

    PermissionsService.validateIsOwner(memberCompany);

    if(req.role().equals(CompanyRole.OWNER)) {
      throw new ConflictException("You can`t create an OWNER");
    }

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




}
