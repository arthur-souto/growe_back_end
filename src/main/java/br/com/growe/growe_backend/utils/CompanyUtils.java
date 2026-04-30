package br.com.growe.growe_backend.utils;

import br.com.growe.growe_backend.domain.Company;
import br.com.growe.growe_backend.exceptions.ResourceNotFoundException;
import br.com.growe.growe_backend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyUtils {

  private final CompanyRepository companyRepository;

  public Company findCompanyBySlug(String slug) {
    return  companyRepository.findBySlug(slug)
        .orElseThrow(() -> new ResourceNotFoundException("Company not found", slug));
  }

  public Company findCompanyBySlug(String slug, String message) {
    return  companyRepository.findBySlug(slug)
        .orElseThrow(() -> new ResourceNotFoundException(message, slug));
  }

}
