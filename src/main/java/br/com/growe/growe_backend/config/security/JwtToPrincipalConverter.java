package br.com.growe.growe_backend.config.security;


import br.com.growe.growe_backend.exceptions.ResourceNotFoundException;
import br.com.growe.growe_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class JwtToPrincipalConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final UserRepository userRepository;

  @Override
  public AbstractAuthenticationToken convert(Jwt source) {

    final var userId = UUID.fromString(source.getSubject());

    final var user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

    final var principal = new UserPrincipal(user);

    return new UserAuthenticationToken(principal, principal.getAuthorities());
  }
}