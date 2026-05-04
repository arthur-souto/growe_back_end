CREATE TABLE evaluation_task (
                                 id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 cycle_id        UUID NOT NULL REFERENCES evaluation_cycle(id) ON DELETE CASCADE,
                                 created_by      UUID NOT NULL REFERENCES company_members(id) ON DELETE CASCADE,
                                 evaluator_id    UUID NOT NULL REFERENCES company_members(id) ON DELETE CASCADE,
                                 evaluated_id    UUID NOT NULL REFERENCES company_members(id) ON DELETE CASCADE,
                                 assessment_type VARCHAR(10) NOT NULL CHECK (assessment_type IN ('SELF', 'PEER', 'MANAGER')),
                                 status          VARCHAR(10) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'DONE')),
                                 completed_at    TIMESTAMPTZ,
                                 deadline        TIMESTAMPTZ NOT NULL,
                                 created_at      TIMESTAMPTZ DEFAULT now(),

                                 CONSTRAINT uq_task UNIQUE (cycle_id, evaluator_id, evaluated_id)
);

CREATE INDEX idx_task_cycle     ON evaluation_task(cycle_id);
CREATE INDEX idx_task_evaluator ON evaluation_task(evaluator_id);
CREATE INDEX idx_task_status    ON evaluation_task(status);
CREATE INDEX idx_task_deadline  ON evaluation_task(deadline);



CREATE TABLE assessment (
                            id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            cycle_id        UUID NOT NULL REFERENCES evaluation_cycle(id) ON DELETE CASCADE,
                            evaluator_id    UUID NOT NULL REFERENCES company_members(id) ON DELETE CASCADE,
                            evaluated_id    UUID NOT NULL REFERENCES company_members(id) ON DELETE CASCADE,
                            score           NUMERIC(2,1) NOT NULL,
                            comment         VARCHAR(500) NOT NULL,
                            assessment_type VARCHAR(10) NOT NULL CHECK (assessment_type IN ('SELF', 'PEER', 'MANAGER')),
                            task_id         UUID REFERENCES evaluation_task(id) ON DELETE SET NULL,
                            created_at      TIMESTAMPTZ DEFAULT now(),
                            updated_at      TIMESTAMPTZ DEFAULT now(),

                            CONSTRAINT score_range   CHECK (score BETWEEN 1.0 AND 5.0),
                            CONSTRAINT self_eval     CHECK (
                                (assessment_type = 'SELF'    AND evaluator_id = evaluated_id) OR
                                (assessment_type != 'SELF'   AND evaluator_id != evaluated_id)
                                ),
                            CONSTRAINT uq_assessment UNIQUE (cycle_id, evaluator_id, evaluated_id)
);

CREATE INDEX idx_assessment_cycle     ON assessment(cycle_id);
CREATE INDEX idx_assessment_evaluated ON assessment(evaluated_id);
CREATE INDEX idx_assessment_evaluator ON assessment(evaluator_id);
CREATE INDEX idx_assessment_task      ON assessment(task_id);