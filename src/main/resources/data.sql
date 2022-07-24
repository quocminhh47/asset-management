INSERT INTO public.role (id, name)
VALUES (DEFAULT, 'ADMIN');
INSERT INTO public.role (id, name)
VALUES (DEFAULT, 'STAFF');
INSERT INTO public.location(code, name)
VALUES ('HCM', 'HO CHI MINH');
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0001', '2000-07-22 07:00:00.000000', 'Kiet', true, '2022-07-22 07:00:00.000000', 'Nguyen ', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'kietn', 'HCM', 1, 'ACTIVE');
-- user name : kietn pass: 123