package br.com.growe.growe_backend.domain;

import br.com.growe.growe_backend.rules.AssessmentType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "assessment")
public class Assessment {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "cycle_id", nullable = false)
  private EvaluationCycle cycle;

  @ManyToOne
  @JoinColumn(name = "evaluator_id", nullable = false)
  private CompanyMember evaluator;

  @ManyToOne
  @JoinColumn(name = "evaluated_id", nullable = false)
  private CompanyMember evaluated;

  @Column(nullable = false, precision = 2, scale = 1)
  private BigDecimal score;

  @Column(length = 500, nullable = false)
  private String comment;

  @Enumerated(EnumType.STRING)
  @Column(length = 10, nullable = false)
  private AssessmentType assessmentType;

  @ManyToOne
  @JoinColumn(name = "task_id")
  private EvaluationTask task;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(insertable = false)
  private Instant updatedAt;
}
