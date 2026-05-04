package br.com.growe.growe_backend.domain;

import br.com.growe.growe_backend.rules.AssessmentType;
import br.com.growe.growe_backend.rules.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "evaluation_task")
public class EvaluationTask {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "cycle_id", nullable = false)
  private EvaluationCycle cycle;

  @ManyToOne
  @JoinColumn(name = "created_by", nullable = false)
  private CompanyMember createdBy;

  @ManyToOne
  @JoinColumn(name = "evaluator_id", nullable = false)
  private CompanyMember evaluator;

  @ManyToOne
  @JoinColumn(name = "evaluated_id", nullable = false)
  private CompanyMember evaluated;

  @Enumerated(EnumType.STRING)
  @Column(length = 10, nullable = false)
  private AssessmentType assessmentType;

  @Enumerated(EnumType.STRING)
  @Column(length = 10, nullable = false)
  @Builder.Default
  private TaskStatus status = TaskStatus.PENDING;

  @Column(nullable = false)
  private Instant deadline;

  @Column
  private Instant completedAt;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt;
}
