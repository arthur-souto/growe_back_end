package br.com.growe.growe_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

  private final static String COOKIE_KEY = "access_token";

  public ResponseCookie generateCookie(String token) {
    return  ResponseCookie.from(COOKIE_KEY, token)
        .httpOnly(true)
        .secure(false)    // true in production
        .path("/")
        .maxAge(3600)
        .sameSite("Lax")  //  works cross-origin in dev
        .build();
  }

  public ResponseCookie clearCookie() {
   return ResponseCookie.from(COOKIE_KEY, "")
        .httpOnly(true)
        .secure(false)    // true in production
        .path("/")
        .maxAge(0)        //  deletes the cookie
        .sameSite("Lax")
        .build();
  }

}
