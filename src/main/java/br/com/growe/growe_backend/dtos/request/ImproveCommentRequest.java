package br.com.growe.growe_backend.dtos.request;

import br.com.growe.growe_backend.rules.AssessmentType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ImproveCommentRequest(
        @NotNull(message = "Score is required")
        @DecimalMin(value = "0.0", message = "Score must be at least 0")
        @DecimalMax(value = "5.0", message = "Score must be at most 5")
        BigDecimal score,

        @NotBlank(message = "Comment is required")
        @Size(max = 500, message = "Comment must be at most 500 characters")
        String comment,

        @NotNull
        AssessmentType assessmentType
) {


}
