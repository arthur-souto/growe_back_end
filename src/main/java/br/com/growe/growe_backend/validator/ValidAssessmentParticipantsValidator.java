package br.com.growe.growe_backend.validator;

import br.com.growe.growe_backend.dtos.request.CreateEvaluationTaskRequest;
import br.com.growe.growe_backend.rules.AssessmentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAssessmentParticipantsValidator
    implements ConstraintValidator<ValidAssessmentParticipants, CreateEvaluationTaskRequest> {

  @Override
  public boolean isValid(CreateEvaluationTaskRequest request, ConstraintValidatorContext context) {
    if (request.assessmentType() == null || request.evaluatorId() == null || request.evaluatedId() == null) {
      return true;
    }

    boolean isSelf = request.assessmentType() == AssessmentType.SELF;
    boolean sameParticipant = request.evaluatorId().equals(request.evaluatedId());

    return isSelf == sameParticipant;
  }
}
