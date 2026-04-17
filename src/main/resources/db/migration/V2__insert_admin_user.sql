INSERT INTO users (id, full_name, email, password, role, created_at)
VALUES (
           gen_random_uuid(),
           'Administrador Growe',
           'arthurtavaressouto@gmail.com',
           '$2a$10$8.73pUuD0T4UInuG4mS0Ie3SWh0S5.1tV8G2S.KpHvV1v0y.eC676',
           'ADMIN',
           NOW()
       );