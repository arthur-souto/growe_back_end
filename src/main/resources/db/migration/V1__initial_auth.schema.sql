CREATE TABLE users(
    id UUID PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    profile_image VARCHAR(500),
    last_login_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,

    CONSTRAINT ck_role CHECK ( role IN ('ADMIN', 'EMPLOYEE', 'RH', 'MANAGER', 'GUEST') )
);

CREATE INDEX idx_users_email ON users(email);