CREATE TABLE companies (
                           id               UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
                           name             VARCHAR(200)  NOT NULL,
                           slug             VARCHAR(100)  UNIQUE NOT NULL,
                           cnpj             VARCHAR(14)      UNIQUE,
                           size_range       VARCHAR(30)   CHECK (size_range IN ('1-10','11-50','51-200','201+')),
                           plan             VARCHAR(20)   NOT NULL DEFAULT 'FREE'
                               CHECK (plan IN ('FREE','STARTER','GROWTH','ENTERPRISE')),
                           company_image     VARCHAR(500),
                           trial_ends_at    TIMESTAMPTZ,
                           is_active        BOOLEAN       NOT NULL DEFAULT TRUE,
                           created_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
                           updated_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE TABLE company_members (
                                 id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
                                 company_id   UUID        NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
                                 user_id      UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                 role         VARCHAR(30) NOT NULL DEFAULT 'EMPLOYEE'
                                     CHECK (role IN ('OWNER','ADMIN','MANAGER','EMPLOYEE', 'RH')),
                                 created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                 UNIQUE (company_id, user_id)
);