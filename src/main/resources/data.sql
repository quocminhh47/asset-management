INSERT INTO public.role (id, name)
VALUES (DEFAULT, 'ADMIN');
INSERT INTO public.role (id, name)
VALUES (DEFAULT, 'STAFF');
INSERT INTO public.location(code, name)
VALUES ('HCM', 'HO CHI MINH');
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0014', '2000-07-22 07:00:00.000000', 'Ga', true, '2022-07-22 07:00:00.000000', 'Vler ', 'gavd@22072000',
        'gav', 'HCM', 1, 'ACTIVE');
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0015', '2000-07-22 07:00:00.000000', 'Ga', true, '2022-07-22 07:00:00.000000', 'Vler ', 'gavd@22072000',
        'gav1', 'HCM', 1, 'INIT');