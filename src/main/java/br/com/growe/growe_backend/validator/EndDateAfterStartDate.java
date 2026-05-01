package br.com.growe.growe_backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EndDateAfterStartDateValidator.class)
@Target(ElementType.TYPE)  // applies to class/record level
@Retention(RetentionPolicy.RUNTIME)
public @interface EndDateAfterStartDate {
  String message() default "End date must be after start date";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}