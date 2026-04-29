package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.exceptions.AccessDeniedException;
import br.com.growe.growe_backend.exceptions.BusinessException;
import br.com.growe.growe_backend.rules.CompanyRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionsService {

  public static void validateIsOwner(CompanyMember member) {
    if(member.getRole() != CompanyRole.OWNER) {
      throw new AccessDeniedException("You dont have permissions");
    }
  }
}
