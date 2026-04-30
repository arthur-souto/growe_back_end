package br.com.growe.growe_backend.dtos.request;

import br.com.growe.growe_backend.rules.CompanyRole;

public record ParsedMember(String fullName, String email, String password, CompanyRole role) {
}
