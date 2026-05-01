package br.com.growe.growe_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
@Table(name = "evaluation_cycle")
public class EvaluationCycle {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  private Company company;

  @ManyToOne
  @JoinColumn(name = "created_by", nullable = false)
  private CompanyMember createdBy;

  @Column(nullable = false)
  private String name;

  @Column(length = 500)
  private String description;

  @Column(length = 7, nullable = false)
  private String color = "#6366f1";

  @Column(columnDefinition = "boolean default false", nullable = false)
  private boolean isActive;

  @Column(nullable = false)
  private Instant startDate;

  @Column(nullable = false)
  private Instant endDate;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(insertable = false)
  private Instant updatedAt;

}
