package br.com.growe.growe_backend.service;

import br.com.growe.growe_backend.config.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final JwtEncoder jwtEncoder;
  private static final long TOKEN_EXPIRATION_SECONDS = 3600L;


  public String generateToken(UserPrincipal principal) {

    final var now = Instant.now();

    final var claims = JwtClaimsSet.builder()
        .issuer("growe-backend")
        .subject(principal.user().getId().toString())
        .issuedAt(now)
        .expiresAt(now.plusSeconds(TOKEN_EXPIRATION_SECONDS))
        .claim("role", principal.user().getRole().name())
        .claim("email", principal.user().getEmail())
        .claim("lastLoginAt", formatInstant(principal.user().getLastLoginAt()))
        .claim("profileImage", principal.user().getProfileImage() != null ? principal. user().getProfileImage() : "")
        .claim("fullName", principal.user().getFullName())
        .build();

    final var header = JwsHeader.with(SignatureAlgorithm.RS256).build();

    return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
  }

  // Safely converts Instant → ISO-8601 String for JWT serialization
  private String formatInstant(Instant instant) {
    return instant != null ? instant.toString() : "";
  }
}