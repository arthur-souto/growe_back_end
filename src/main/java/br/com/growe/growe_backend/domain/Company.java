package br.com.growe.growe_backend.domain;

import br.com.growe.growe_backend.rules.Plan;
import br.com.growe.growe_backend.rules.SizeRange;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "companies")
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String slug;

  @Column(length = 14, nullable = false, unique = true)
  private String cnpj;

  @Column(nullable = false)
  private SizeRange sizeRange;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Plan plan;

  @Column(nullable = true)
  private String companyImage;

  @Column(nullable = true)
  private Instant trialEndsAt;

  @Column(columnDefinition = "boolean default true", nullable = false)
  private boolean isActive;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(insertable = false)
  private Instant updatedAt;

  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CompanyMember> members;
}
