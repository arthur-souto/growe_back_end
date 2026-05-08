CREATE TABLE competency (
                            id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            company_id   UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
                            created_by   UUID NOT NULL REFERENCES company_members(id) ON DELETE CASCADE,
                            name         VARCHAR(100) NOT NULL,
                            description  VARCHAR(500),
                            created_at   TIMESTAMPTZ DEFAULT now(),
                            CONSTRAINT uq_competency UNIQUE (company_id, name)
);


CREATE TABLE cycle_competency (
                                  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                  cycle_id       UUID NOT NULL REFERENCES evaluation_cycle(id) ON DELETE CASCADE,
                                  competency_id  UUID NOT NULL REFERENCES competency(id) ON DELETE CASCADE,
                                  CONSTRAINT uq_cycle_competency UNIQUE (cycle_id, competency_id)
);


CREATE TABLE assessment_answer (
                                   id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   assessment_id  UUID NOT NULL REFERENCES assessment(id) ON DELETE CASCADE,
                                   competency_id  UUID NOT NULL REFERENCES competency(id) ON DELETE CASCADE,
                                   score          NUMERIC(2,1) NOT NULL CHECK (score BETWEEN 1.0 AND 5.0),
                                   comment        VARCHAR(500),
                                   CONSTRAINT uq_answer UNIQUE (assessment_id, competency_id)
);


ALTER TABLE assessment DROP COLUMN score;
ALTER TABLE assessment ALTER COLUMN comment DROP NOT NULL;