package br.com.growe.growe_backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidAssessmentParticipantsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAssessmentParticipants {
  String message() default "Evaluator and evaluated must be the same for SELF assessments, and different for PEER or MANAGER assessments";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
