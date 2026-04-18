package br.com.growe.growe_backend.dtos;

public record SignInRequest(
    String email,
    String password
) {
}
