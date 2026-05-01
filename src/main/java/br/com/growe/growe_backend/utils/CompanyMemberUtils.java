package br.com.growe.growe_backend.utils;

import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.exceptions.AccessDeniedException;
import br.com.growe.growe_backend.repository.CompanyMembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyMemberUtils {

  private final CompanyMembersRepository companyMembersRepository;

  public CompanyMember findCompanyMemberByUserAndCompany(UUID userId, UUID companyId) {

    return companyMembersRepository.findByUser_idAndCompany_id(userId, companyId)
        .orElseThrow(() -> new AccessDeniedException("You dont have permissions"));
  }

  public CompanyMember findCompanyMemberByUserAndCompany(UUID userId, UUID companyId, String message) {

    return companyMembersRepository.findByUser_idAndCompany_id(userId, companyId)
        .orElseThrow(() -> new AccessDeniedException(message));
  }

}
