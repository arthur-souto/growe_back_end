package br.com.growe.growe_backend.validator;

import br.com.growe.growe_backend.dtos.request.CreateEvaluationCycleRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndDateAfterStartDateValidator
    implements ConstraintValidator<EndDateAfterStartDate, CreateEvaluationCycleRequest> {

  @Override
  public boolean isValid(CreateEvaluationCycleRequest request, ConstraintValidatorContext context) {
    if (request.startDate() == null || request.endDate() == null) {
      return true;
    }
    return request.endDate().isAfter(request.startDate());
  }
}
