package br.com.growe.growe_backend.domain;

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
@Table(name = "competency")
public class Competency {

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

  @Column(length = 100, nullable = false)
  private String name;

  @Column(length = 500)
  private String description;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt;
}
