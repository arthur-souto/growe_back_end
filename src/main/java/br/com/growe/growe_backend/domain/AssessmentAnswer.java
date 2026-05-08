package br.com.growe.growe_backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "assessment_answer")
public class AssessmentAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "assessment_id", nullable = false)
  private Assessment assessment;

  @ManyToOne
  @JoinColumn(name = "competency_id", nullable = false)
  private Competency competency;

  @Column(nullable = false, precision = 2, scale = 1)
  private BigDecimal score;

  @Column(length = 500)
  private String comment;
}
