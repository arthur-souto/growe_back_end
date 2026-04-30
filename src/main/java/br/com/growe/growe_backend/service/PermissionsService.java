package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.exceptions.AccessDeniedException;
import br.com.growe.growe_backend.rules.CompanyRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionsService {

  public static void validateIsOwner(CompanyMember member) {
    if(member.getRole() != CompanyRole.OWNER) {
      throw new AccessDeniedException("You don`t have permissions");
    }
  }

  public static void validateIsOwnerOrManager(CompanyMember member) {
    if(member.getRole() != CompanyRole.OWNER && member.getRole() != CompanyRole.MANAGER) {
      throw new AccessDeniedException("You don`t have permissions");
    }
  }
}
