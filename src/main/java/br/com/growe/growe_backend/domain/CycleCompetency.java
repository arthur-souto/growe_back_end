package br.com.growe.growe_backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cycle_competency")
public class CycleCompetency {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "cycle_id", nullable = false)
  private EvaluationCycle cycle;

  @ManyToOne
  @JoinColumn(name = "competency_id", nullable = false)
  private Competency competency;
}
