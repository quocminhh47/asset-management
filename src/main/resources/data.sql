INSERT INTO public.role (id, name)
VALUES (DEFAULT, 'ADMIN');
INSERT INTO public.role (id, name)
VALUES (DEFAULT, 'STAFF');
INSERT INTO public.location(code, name)
VALUES ('HCM', 'HO CHI MINH');
INSERT INTO public.location(code, name)
VALUES ('HN', 'HA NOI');
INSERT INTO public.location(code, name)
VALUES ('DN', 'DA NANG');
--admin--
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0001', '2000-07-22 07:00:00.000000', 'Van', true, '2022-07-22 07:00:00.000000', 'Nguyen', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'vann', 'HCM', 1, 'INIT');
-- user name : vann pass: 123
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0002', '2001-05-20 07:00:00.000000', 'Quoc', true, '2020-06-22 07:00:00.000000', 'Pham', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'quocp', 'DN', 1, 'INIT');
-- user name : quocp pass: 123
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0003', '2000-06-19 07:00:00.000000', 'Duc', true, '2021-07-22 07:00:00.000000', 'Nguyen', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'ducn', 'HN', 1, 'INIT');
-- user name : ducn pass: 123
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0004', '2000-01-01 07:00:00.000000', 'Hue', true, '2022-06-22 07:00:00.000000', 'Ton', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'huet', 'HCM', 1, 'INIT');
-- user name : huet pass: 123
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0005', '2001-12-12 07:00:00.000000', 'Long', true, '2020-07-22 07:00:00.000000', 'Truong', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'longt', 'HCM', 1, 'INIT');
-- user name : longt pass: 123
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('AT0001', '2001-07-07 07:00:00.000000', 'Linh', true, '2022-07-07 07:00:00.000000', 'Truong', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'linht', 'DN', 1, 'INIT');
-- user name : linht pass: 123
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('AT0002', '2000-08-08 07:00:00.000000', 'Nhat', true, '2022-06-22 07:00:00.000000', 'Do', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'nhatd', 'HN', 1, 'INIT');
-- user name : nhatd pass: 123

--staff--
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0006', '2000-06-19 07:00:00.000000', 'Goku', true, '2022-07-22 07:00:00.000000', 'Nguyen', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'gokun', 'HCM', 2, 'INIT');
-- user name : gokun pass: 123
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('SD0007', '1999-06-19 07:00:00.000000', 'Conan', true, '2020-06-22 07:00:00.000000', 'Yang', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'conany', 'HCM', 2, 'INIT');
-- user name : conany pass: 123
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('AT0003', '1998-06-19 07:00:00.000000', 'Depp', true, '2020-06-22 07:00:00.000000', 'Johny', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'deppj', 'HN', 2, 'INIT');
-- user name : deppj pass: 123
INSERT INTO public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, username,
                          location_id, role_id, state)
VALUES ('AT0004', '1995-06-19 07:00:00.000000', 'Amber', true, '2020-06-22 07:00:00.000000', 'Dun', '$2a$10$r3uRMuUjABLpLEK16a2z3ul7dHRQCRRR58jGsvhaJL3WJZlNH/E3y',
        'amberd', 'DN', 2, 'INIT');
-- user name : amberd pass: 123
