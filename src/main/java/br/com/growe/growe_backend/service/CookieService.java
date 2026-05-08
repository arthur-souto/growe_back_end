package br.com.growe.growe_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

  private final static String COOKIE_KEY = "access_token";

  @Value("${app.cookie.secure:true}")
  private boolean secureCookie;

  public ResponseCookie generateCookie(String token) {
    return ResponseCookie.from(COOKIE_KEY, token)
        .httpOnly(true)
        .secure(secureCookie)
        .path("/")
        .maxAge(3600)
        .sameSite("Strict")
        .build();
  }

  public ResponseCookie clearCookie() {
    return ResponseCookie.from(COOKIE_KEY, "")
        .httpOnly(true)
        .secure(secureCookie)
        .path("/")
        .maxAge(0)
        .sameSite("Strict")
        .build();
  }

}
