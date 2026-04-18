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

  public String generateToken(UserPrincipal principal) {

    final var now = Instant.now();

    final var claims = JwtClaimsSet.builder()
        .issuer("growe-backend")
        .subject(principal.user().getEmail())
        .issuedAt(now)
        .expiresAt(now.plusSeconds(3600)) // Token válido por 1 hora
        .claim("role", principal.user().getRole().name())
        .claim("fullName", principal.user().getFullName())
        .build();

    JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();

    return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
  }
}
