INSERT INTO users (id, full_name, email, password, role, created_at)
VALUES (
           gen_random_uuid(),
           'Administrador Growe',
           'arthurtavaressouto@gmail.com',
           '$2a$12$EiVbJUMtHztdLXtPd6Ch1u64xhPCJJeFMbcIZot4nI/QxzQ2cRZNe',
           'ADMIN',
           NOW()
       );