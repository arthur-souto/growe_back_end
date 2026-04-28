package br.com.growe.growe_backend.dtos.response;

import br.com.growe.growe_backend.domain.CompanyMember;
import br.com.growe.growe_backend.domain.User;
import br.com.growe.growe_backend.rules.CompanyRole;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ResumeMemberResponse(
    String fullName,
    String email,
    String profileImage,
    CompanyRole role,
    Instant createdAt
) {
  public static ResumeMemberResponse fromEntity(User user, CompanyMember member) {
    return
        ResumeMemberResponse
            .builder()
            .fullName(user.getFullName())
            .email(user.getEmail())
            .profileImage(user.getProfileImage())
            .role(member.getRole())
            .createdAt(member.getCreatedAt())
            .build();
  }
}
