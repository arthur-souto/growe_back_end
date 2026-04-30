package br.com.growe.growe_backend.dtos.request;

import org.springframework.web.multipart.MultipartFile;

public record ImportMembersRequest(MultipartFile file) {
}
