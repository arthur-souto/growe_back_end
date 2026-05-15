-- ================================================================
-- GROWE — Database Seed
-- Password for all users: "password"
-- Company slug: techcorp
--
-- Run: psql -U growe_dev -d growe_db -f src/main/resources/db/seed.sql
-- Idempotent: uses ON CONFLICT DO NOTHING throughout.
-- ================================================================

BEGIN;
SELECT setseed(0.42);

-- ── 1. USERS ──────────────────────────────────────────────────────
INSERT INTO users (id, full_name, email, password, role, active) VALUES
  ('a0000001-0000-0000-0000-000000000000','Arthur Souto',     'arthur@techcorp.com',   '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000002-0000-0000-0000-000000000000','Fernanda Lima',    'fernanda@techcorp.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000003-0000-0000-0000-000000000000','Rafael Mendes',    'rafael@techcorp.com',   '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000004-0000-0000-0000-000000000000','Camila Torres',    'camila@techcorp.com',   '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','RH',      true),
  ('a0000005-0000-0000-0000-000000000000','Lucas Oliveira',   'lucas@techcorp.com',    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000006-0000-0000-0000-000000000000','Mariana Costa',    'mariana@techcorp.com',  '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000007-0000-0000-0000-000000000000','Pedro Alves',      'pedro@techcorp.com',    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000008-0000-0000-0000-000000000000','Beatriz Santos',   'beatriz@techcorp.com',  '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000009-0000-0000-0000-000000000000','Thiago Ferreira',  'thiago@techcorp.com',   '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000010-0000-0000-0000-000000000000','Juliana Rocha',    'juliana@techcorp.com',  '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000011-0000-0000-0000-000000000000','Diego Nascimento', 'diego@techcorp.com',    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true),
  ('a0000012-0000-0000-0000-000000000000','Larissa Cardoso',  'larissa@techcorp.com',  '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG','EMPLOYEE',true)
ON CONFLICT (email) DO NOTHING;

-- ── 2. COMPANY ────────────────────────────────────────────────────
INSERT INTO companies (id, name, slug, cnpj, size_range, plan) VALUES
  ('b0000001-0000-0000-0000-000000000000','TechCorp Brasil','techcorp','12345678000100','11-50','GROWTH')
ON CONFLICT DO NOTHING;

-- ── 3. COMPANY MEMBERS ────────────────────────────────────────────
-- c0000001 = OWNER (Arthur), c0000002 = MANAGER (Fernanda)
INSERT INTO company_members (id, company_id, user_id, role) VALUES
  ('c0000001-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000001-0000-0000-0000-000000000000','OWNER'),
  ('c0000002-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000002-0000-0000-0000-000000000000','MANAGER'),
  ('c0000003-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000003-0000-0000-0000-000000000000','ADMIN'),
  ('c0000004-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000004-0000-0000-0000-000000000000','RH'),
  ('c0000005-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000005-0000-0000-0000-000000000000','EMPLOYEE'),
  ('c0000006-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000006-0000-0000-0000-000000000000','EMPLOYEE'),
  ('c0000007-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000007-0000-0000-0000-000000000000','EMPLOYEE'),
  ('c0000008-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000008-0000-0000-0000-000000000000','EMPLOYEE'),
  ('c0000009-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000009-0000-0000-0000-000000000000','EMPLOYEE'),
  ('c0000010-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000010-0000-0000-0000-000000000000','EMPLOYEE'),
  ('c0000011-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000011-0000-0000-0000-000000000000','EMPLOYEE'),
  ('c0000012-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','a0000012-0000-0000-0000-000000000000','EMPLOYEE')
ON CONFLICT DO NOTHING;

-- ── 4. COMPETENCIES ───────────────────────────────────────────────
INSERT INTO competency (id, company_id, created_by, name, description) VALUES
  ('d0000001-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','c0000001-0000-0000-0000-000000000000','Communication',    'Clarity and effectiveness in verbal and written communication'),
  ('d0000002-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','c0000001-0000-0000-0000-000000000000','Technical Skills', 'Domain expertise and ability to apply technical knowledge'),
  ('d0000003-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','c0000001-0000-0000-0000-000000000000','Teamwork',         'Collaboration and contribution to team goals'),
  ('d0000004-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','c0000001-0000-0000-0000-000000000000','Leadership',       'Ability to inspire, guide, and influence others'),
  ('d0000005-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','c0000001-0000-0000-0000-000000000000','Problem Solving',  'Analytical thinking and creative problem resolution'),
  ('d0000006-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','c0000001-0000-0000-0000-000000000000','Adaptability',     'Flexibility and ability to thrive in a changing environment')
ON CONFLICT DO NOTHING;

-- ── 5. EVALUATION CYCLES (3 completed historical cycles) ──────────
INSERT INTO evaluation_cycle (id, company_id, created_by, name, description, color, is_active, start_date, end_date) VALUES
  ('e0000001-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','c0000001-0000-0000-0000-000000000000',
   'Q1 2024','First quarter evaluation cycle of 2024','#6366f1',false,
   '2024-01-15 00:00:00+00','2024-03-31 23:59:59+00'),
  ('e0000002-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','c0000001-0000-0000-0000-000000000000',
   'Q3 2024','Third quarter evaluation cycle of 2024','#10b981',false,
   '2024-07-01 00:00:00+00','2024-09-30 23:59:59+00'),
  ('e0000003-0000-0000-0000-000000000000','b0000001-0000-0000-0000-000000000000','c0000001-0000-0000-0000-000000000000',
   'Q1 2025','First quarter evaluation cycle of 2025','#f59e0b',false,
   '2025-01-15 00:00:00+00','2025-03-31 23:59:59+00')
ON CONFLICT DO NOTHING;

-- ── 6. CYCLE COMPETENCIES (all 6 competencies × 3 cycles) ─────────
INSERT INTO cycle_competency (id, cycle_id, competency_id) VALUES
  (gen_random_uuid(),'e0000001-0000-0000-0000-000000000000','d0000001-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000001-0000-0000-0000-000000000000','d0000002-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000001-0000-0000-0000-000000000000','d0000003-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000001-0000-0000-0000-000000000000','d0000004-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000001-0000-0000-0000-000000000000','d0000005-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000001-0000-0000-0000-000000000000','d0000006-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000002-0000-0000-0000-000000000000','d0000001-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000002-0000-0000-0000-000000000000','d0000002-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000002-0000-0000-0000-000000000000','d0000003-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000002-0000-0000-0000-000000000000','d0000004-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000002-0000-0000-0000-000000000000','d0000005-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000002-0000-0000-0000-000000000000','d0000006-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000003-0000-0000-0000-000000000000','d0000001-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000003-0000-0000-0000-000000000000','d0000002-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000003-0000-0000-0000-000000000000','d0000003-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000003-0000-0000-0000-000000000000','d0000004-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000003-0000-0000-0000-000000000000','d0000005-0000-0000-0000-000000000000'),
  (gen_random_uuid(),'e0000003-0000-0000-0000-000000000000','d0000006-0000-0000-0000-000000000000')
ON CONFLICT DO NOTHING;

-- ── 7. TASKS, ASSESSMENTS & ANSWERS ──────────────────────────────
-- Per cycle:
--   SELF  : 12 (every member evaluates themselves)
--   PEER  : 22 (11 evaluators × 2 targets via ring; manager skipped as evaluator)
--   MANAGER: 11 (Fernanda evaluates all except herself)
-- Total : 45 assessments × 6 answers = 270 answer rows per cycle → 810 total
DO $$
DECLARE
  v_members UUID[] := ARRAY[
    'c0000001-0000-0000-0000-000000000000'::uuid,
    'c0000002-0000-0000-0000-000000000000'::uuid,
    'c0000003-0000-0000-0000-000000000000'::uuid,
    'c0000004-0000-0000-0000-000000000000'::uuid,
    'c0000005-0000-0000-0000-000000000000'::uuid,
    'c0000006-0000-0000-0000-000000000000'::uuid,
    'c0000007-0000-0000-0000-000000000000'::uuid,
    'c0000008-0000-0000-0000-000000000000'::uuid,
    'c0000009-0000-0000-0000-000000000000'::uuid,
    'c0000010-0000-0000-0000-000000000000'::uuid,
    'c0000011-0000-0000-0000-000000000000'::uuid,
    'c0000012-0000-0000-0000-000000000000'::uuid
  ];
  v_cycles UUID[] := ARRAY[
    'e0000001-0000-0000-0000-000000000000'::uuid,
    'e0000002-0000-0000-0000-000000000000'::uuid,
    'e0000003-0000-0000-0000-000000000000'::uuid
  ];
  v_comps UUID[] := ARRAY[
    'd0000001-0000-0000-0000-000000000000'::uuid,
    'd0000002-0000-0000-0000-000000000000'::uuid,
    'd0000003-0000-0000-0000-000000000000'::uuid,
    'd0000004-0000-0000-0000-000000000000'::uuid,
    'd0000005-0000-0000-0000-000000000000'::uuid,
    'd0000006-0000-0000-0000-000000000000'::uuid
  ];

  v_owner_id   UUID := 'c0000001-0000-0000-0000-000000000000';
  v_manager_id UUID := 'c0000002-0000-0000-0000-000000000000';

  -- Assessment-level comment pools
  v_c_self TEXT[] := ARRAY[
    'I feel I have grown significantly this cycle and consistently delivered quality work.',
    'I identified areas where I need improvement and am actively working on them.',
    'This cycle I took on more responsibilities and believe I met the expectations set.',
    'I contributed meaningfully to the team and delivered all commitments on time.',
    'I had challenges this period but learned from each one and improved steadily.'
  ];
  v_c_peer TEXT[] := ARRAY[
    'A pleasure to work with — always collaborative and reliable.',
    'Brings great energy to the team and helps others when needed.',
    'Very skilled and always willing to share knowledge with colleagues.',
    'Could improve on communication but delivers solid results.',
    'A consistent performer who can always be counted on.',
    'Shows initiative and takes ownership of their responsibilities.'
  ];
  v_c_manager TEXT[] := ARRAY[
    'This team member met all expectations this cycle and shows great potential.',
    'Demonstrated strong performance and a positive attitude throughout the period.',
    'Needs to develop further in certain areas but is on the right track.',
    'A reliable contributor who consistently adds value to the team.',
    'Has shown notable improvement compared to the previous cycle.'
  ];

  -- Answer-level comment pools (by score tier)
  v_c_high TEXT[] := ARRAY[
    'Outstanding performance in this area — a true strength.',
    'Consistently exceeds expectations here.',
    'An excellent example for the team in this competency.',
    'Handles this with confidence and great skill.',
    'Remarkable ability shown throughout the cycle.'
  ];
  v_c_mid TEXT[] := ARRAY[
    'Meets expectations with room to grow further.',
    'Solid contribution, could deepen expertise over time.',
    'Good foundation; consistent application will help.',
    'Satisfactory performance; building on this will be key.',
    'Adequate work with some areas to refine.'
  ];
  v_c_low TEXT[] := ARRAY[
    'This area needs more attention and deliberate practice.',
    'Below expected level; a development plan would help.',
    'Improvement needed — would benefit from mentoring here.',
    'Consistent effort required to meet the standard.',
    'Room for significant growth; should prioritize this competency.'
  ];

  i INT; j INT; k INT;
  v_evaluator  UUID;
  v_evaluated  UUID;
  v_cycle      UUID;
  v_task_id    UUID;
  v_asmt_id    UUID;
  v_score      NUMERIC(2,1);
  v_start      TIMESTAMPTZ;
  v_deadline   TIMESTAMPTZ;
  v_completed  TIMESTAMPTZ;
  v_created    TIMESTAMPTZ;

BEGIN
  FOR i IN 1..3 LOOP
    v_cycle := v_cycles[i];
    v_start := CASE i
      WHEN 1 THEN '2024-01-15 00:00:00+00'::TIMESTAMPTZ
      WHEN 2 THEN '2024-07-01 00:00:00+00'::TIMESTAMPTZ
      WHEN 3 THEN '2025-01-15 00:00:00+00'::TIMESTAMPTZ
    END;
    v_deadline := v_start + INTERVAL '45 days';

    -- ── SELF evaluations ────────────────────────────────────────
    FOR j IN 1..12 LOOP
      v_evaluator := v_members[j];
      v_evaluated := v_members[j];
      v_task_id   := gen_random_uuid();
      v_asmt_id   := gen_random_uuid();
      v_completed := v_start + (10 + (random() * 15)::int) * INTERVAL '1 day';
      v_created   := v_completed + INTERVAL '1 hour';

      INSERT INTO evaluation_task (id, cycle_id, created_by, evaluator_id, evaluated_id, assessment_type, status, completed_at, deadline)
      VALUES (v_task_id, v_cycle, v_owner_id, v_evaluator, v_evaluated, 'SELF', 'DONE', v_completed, v_deadline);

      INSERT INTO assessment (id, cycle_id, evaluator_id, evaluated_id, comment, assessment_type, task_id, created_at)
      VALUES (v_asmt_id, v_cycle, v_evaluator, v_evaluated,
              CASE WHEN random() > 0.25 THEN v_c_self[(floor(random() * 5) + 1)::int] ELSE NULL END,
              'SELF', v_task_id, v_created);

      FOR k IN 1..6 LOOP
        v_score := ROUND((random() * 2.5 + 2.5)::numeric, 1)::numeric(2,1);
        INSERT INTO assessment_answer (id, assessment_id, competency_id, score, comment)
        VALUES (gen_random_uuid(), v_asmt_id, v_comps[k], v_score,
                CASE WHEN random() > 0.4 THEN
                  CASE WHEN v_score >= 4.0 THEN v_c_high[(floor(random() * 5) + 1)::int]
                       WHEN v_score >= 3.0 THEN v_c_mid [(floor(random() * 5) + 1)::int]
                       ELSE                     v_c_low [(floor(random() * 5) + 1)::int]
                  END
                ELSE NULL END);
      END LOOP;
    END LOOP;

    -- ── PEER evaluations (ring; manager skipped as evaluator) ────
    -- Ring: member[j] evaluates member[(j%12)+1] and member[((j+1)%12)+1]
    -- Skipping j=2 (manager) avoids duplicate (cycle, evaluator, evaluated) with MANAGER rows.
    FOR j IN 1..12 LOOP
      v_evaluator := v_members[j];
      IF v_evaluator = v_manager_id THEN CONTINUE; END IF;

      -- first peer target
      v_evaluated := v_members[(j % 12) + 1];
      v_task_id   := gen_random_uuid();
      v_asmt_id   := gen_random_uuid();
      v_completed := v_start + (12 + (random() * 20)::int) * INTERVAL '1 day';
      v_created   := v_completed + INTERVAL '2 hours';

      INSERT INTO evaluation_task (id, cycle_id, created_by, evaluator_id, evaluated_id, assessment_type, status, completed_at, deadline)
      VALUES (v_task_id, v_cycle, v_owner_id, v_evaluator, v_evaluated, 'PEER', 'DONE', v_completed, v_deadline);

      INSERT INTO assessment (id, cycle_id, evaluator_id, evaluated_id, comment, assessment_type, task_id, created_at)
      VALUES (v_asmt_id, v_cycle, v_evaluator, v_evaluated,
              CASE WHEN random() > 0.35 THEN v_c_peer[(floor(random() * 6) + 1)::int] ELSE NULL END,
              'PEER', v_task_id, v_created);

      FOR k IN 1..6 LOOP
        v_score := ROUND((random() * 4 + 1)::numeric, 1)::numeric(2,1);
        INSERT INTO assessment_answer (id, assessment_id, competency_id, score, comment)
        VALUES (gen_random_uuid(), v_asmt_id, v_comps[k], v_score,
                CASE WHEN random() > 0.45 THEN
                  CASE WHEN v_score >= 4.0 THEN v_c_high[(floor(random() * 5) + 1)::int]
                       WHEN v_score >= 3.0 THEN v_c_mid [(floor(random() * 5) + 1)::int]
                       ELSE                     v_c_low [(floor(random() * 5) + 1)::int]
                  END
                ELSE NULL END);
      END LOOP;

      -- second peer target (two ahead in ring)
      v_evaluated := v_members[((j + 1) % 12) + 1];
      v_task_id   := gen_random_uuid();
      v_asmt_id   := gen_random_uuid();
      v_completed := v_start + (12 + (random() * 20)::int) * INTERVAL '1 day';
      v_created   := v_completed + INTERVAL '2 hours';

      INSERT INTO evaluation_task (id, cycle_id, created_by, evaluator_id, evaluated_id, assessment_type, status, completed_at, deadline)
      VALUES (v_task_id, v_cycle, v_owner_id, v_evaluator, v_evaluated, 'PEER', 'DONE', v_completed, v_deadline);

      INSERT INTO assessment (id, cycle_id, evaluator_id, evaluated_id, comment, assessment_type, task_id, created_at)
      VALUES (v_asmt_id, v_cycle, v_evaluator, v_evaluated,
              CASE WHEN random() > 0.35 THEN v_c_peer[(floor(random() * 6) + 1)::int] ELSE NULL END,
              'PEER', v_task_id, v_created);

      FOR k IN 1..6 LOOP
        v_score := ROUND((random() * 4 + 1)::numeric, 1)::numeric(2,1);
        INSERT INTO assessment_answer (id, assessment_id, competency_id, score, comment)
        VALUES (gen_random_uuid(), v_asmt_id, v_comps[k], v_score,
                CASE WHEN random() > 0.45 THEN
                  CASE WHEN v_score >= 4.0 THEN v_c_high[(floor(random() * 5) + 1)::int]
                       WHEN v_score >= 3.0 THEN v_c_mid [(floor(random() * 5) + 1)::int]
                       ELSE                     v_c_low [(floor(random() * 5) + 1)::int]
                  END
                ELSE NULL END);
      END LOOP;
    END LOOP;

    -- ── MANAGER evaluations (Fernanda evaluates all except herself) ──
    FOR j IN 1..12 LOOP
      v_evaluated := v_members[j];
      IF v_evaluated = v_manager_id THEN CONTINUE; END IF;

      v_task_id   := gen_random_uuid();
      v_asmt_id   := gen_random_uuid();
      v_completed := v_start + (20 + (random() * 10)::int) * INTERVAL '1 day';
      v_created   := v_completed + INTERVAL '3 hours';

      INSERT INTO evaluation_task (id, cycle_id, created_by, evaluator_id, evaluated_id, assessment_type, status, completed_at, deadline)
      VALUES (v_task_id, v_cycle, v_owner_id, v_manager_id, v_evaluated, 'MANAGER', 'DONE', v_completed, v_deadline);

      INSERT INTO assessment (id, cycle_id, evaluator_id, evaluated_id, comment, assessment_type, task_id, created_at)
      VALUES (v_asmt_id, v_cycle, v_manager_id, v_evaluated,
              CASE WHEN random() > 0.2 THEN v_c_manager[(floor(random() * 5) + 1)::int] ELSE NULL END,
              'MANAGER', v_task_id, v_created);

      FOR k IN 1..6 LOOP
        v_score := ROUND((random() * 3.5 + 1.5)::numeric, 1)::numeric(2,1);
        INSERT INTO assessment_answer (id, assessment_id, competency_id, score, comment)
        VALUES (gen_random_uuid(), v_asmt_id, v_comps[k], v_score,
                CASE WHEN random() > 0.3 THEN
                  CASE WHEN v_score >= 4.0 THEN v_c_high[(floor(random() * 5) + 1)::int]
                       WHEN v_score >= 3.0 THEN v_c_mid [(floor(random() * 5) + 1)::int]
                       ELSE                     v_c_low [(floor(random() * 5) + 1)::int]
                  END
                ELSE NULL END);
      END LOOP;
    END LOOP;

  END LOOP; -- cycles
END $$;

COMMIT;
