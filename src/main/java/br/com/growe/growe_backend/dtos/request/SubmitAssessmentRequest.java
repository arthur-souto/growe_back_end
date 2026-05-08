package br.com.growe.growe_backend.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SubmitAssessmentRequest(

    @Size(max = 500, message = "Comment must be at most 500 characters")
    String comment,

    @NotEmpty(message = "At least one competency answer is required")
    @Valid
    List<AssessmentAnswerRequest> answers

) {}
