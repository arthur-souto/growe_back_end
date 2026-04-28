package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.rules.CompanyRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionsService {

  public static void checkMemberPermissionForCompany(CompanyMember member) {
    if(member.getRole() != CompanyRole.OWNER) {
      throw new BadCredentialsException("You dont have permissions");
    }
  }
}
