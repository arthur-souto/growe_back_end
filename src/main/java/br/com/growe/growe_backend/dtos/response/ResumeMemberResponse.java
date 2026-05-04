package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.domain.User;
import br.com.growe.growe_backend.rules.CompanyRole;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ResumeMemberResponse(
    UUID id,
    String fullName,
    String email,
    String profileImage,
    CompanyRole role,
    Instant createdAt
) {
  public static ResumeMemberResponse fromEntity(CompanyMember member) {
    return
        ResumeMemberResponse
            .builder()
            .id(member.getId())
            .fullName(member.getUser().getFullName())
            .email(member.getUser().getEmail())
            .profileImage(member.getUser().getProfileImage())
            .role(member.getRole())
            .createdAt(member.getCreatedAt())
            .build();
  }
}
