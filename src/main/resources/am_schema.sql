--
-- PostgreSQL database dump
--

-- Dumped from database version 14.3
-- Dumped by pg_dump version 14.3

-- Started on 2022-07-27 10:35:16

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 42516)
-- Name: asset; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.asset (
    asset_code character varying(10) NOT NULL,
    asset_name character varying(200) NOT NULL,
    installed_date timestamp without time zone,
    specification character varying(255) NOT NULL,
    state boolean,
    category_id character varying(10) NOT NULL,
    location_id character varying(10) NOT NULL
);


ALTER TABLE public.asset OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 42519)
-- Name: assignment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.assignment (
    asset_code character varying(10) NOT NULL,
    assigned_date timestamp without time zone NOT NULL,
    assigned_to character varying(10) NOT NULL,
    note character varying(255) NOT NULL,
    state character varying(50) NOT NULL,
    assigned_by character varying(10) NOT NULL
);


ALTER TABLE public.assignment OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 42522)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
    id character varying(10) NOT NULL,
    name character varying(150) NOT NULL,
    total_quantity bigint
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 42525)
-- Name: location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location (
    code character varying(10) NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.location OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 42528)
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id bigint NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 42531)
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_id_seq OWNER TO postgres;

--
-- TOC entry 3359 (class 0 OID 0)
-- Dependencies: 214
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;


--
-- TOC entry 215 (class 1259 OID 42532)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    staff_code character varying(10) NOT NULL,
    birth_date timestamp without time zone NOT NULL,
    first_name character varying(150) NOT NULL,
    gender boolean,
    joined_date timestamp without time zone NOT NULL,
    last_name character varying(150) NOT NULL,
    password character varying(250) NOT NULL,
    state character varying,
    username character varying(250) NOT NULL,
    location_id character varying(10) NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 3184 (class 2604 OID 42537)
-- Name: role id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);


--
-- TOC entry 3347 (class 0 OID 42516)
-- Dependencies: 209
-- Data for Name: asset; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.asset (asset_code, asset_name, installed_date, specification, state, category_id, location_id) FROM stdin;
\.


--
-- TOC entry 3348 (class 0 OID 42519)
-- Dependencies: 210
-- Data for Name: assignment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.assignment (asset_code, assigned_date, assigned_to, note, state, assigned_by) FROM stdin;
\.


--
-- TOC entry 3349 (class 0 OID 42522)
-- Dependencies: 211
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.category (id, name, total_quantity) FROM stdin;
\.


--
-- TOC entry 3350 (class 0 OID 42525)
-- Dependencies: 212
-- Data for Name: location; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.location (code, name) FROM stdin;
HCM	Ho Chi Minh
HN	Ha Noi
\.


--
-- TOC entry 3351 (class 0 OID 42528)
-- Dependencies: 213
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, name) FROM stdin;
1	Admin
2	Staff
\.


--
-- TOC entry 3353 (class 0 OID 42532)
-- Dependencies: 215
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (staff_code, birth_date, first_name, gender, joined_date, last_name, password, state, username, location_id, role_id) FROM stdin;
10	2021-09-18 00:00:00	Spence	t	2022-05-25 00:00:00	Prosser	j5zJPvcsi	ACTIVE	sprosser9	HN	1
11	2021-12-03 00:00:00	Marna	t	2021-12-30 00:00:00	Olenchenko	vjhCywxxvhh	ACTIVE	molenchenkoa	HN	1
12	2022-05-20 00:00:00	Griff	f	2022-02-12 00:00:00	Mathieu	dQTlWqc3QYO	ACTIVE	gmathieub	HN	1
13	2021-10-26 00:00:00	Karney	t	2021-10-25 00:00:00	Loudiane	aUdyMDnzY	ACTIVE	kloudianec	HN	1
14	2021-12-21 00:00:00	Danita	t	2022-07-20 00:00:00	Olden	zgVMgzjDf	ACTIVE	doldend	HN	1
15	2021-07-30 00:00:00	Dolf	f	2021-11-07 00:00:00	Yeudall	u6SXYMSmp	ACTIVE	dyeudalle	HN	1
16	2021-12-10 00:00:00	Findley	t	2021-11-15 00:00:00	Pembridge	STVpdf1aUVWy	ACTIVE	fpembridgef	HN	1
17	2021-10-24 00:00:00	Sosanna	f	2021-11-13 00:00:00	Baverstock	jFp3b4G	ACTIVE	sbaverstockg	HN	1
18	2022-02-23 00:00:00	Kirbie	t	2021-12-24 00:00:00	Spofforth	Kq44JI	ACTIVE	kspofforthh	HN	1
19	2021-08-13 00:00:00	Melli	f	2021-07-23 00:00:00	Milksop	pjsxZV	ACTIVE	mmilksopi	HN	1
2	2021-11-10 00:00:00	Melinde	t	2022-01-01 00:00:00	Newby	t41mmGBc	ACTIVE	mnewby1	HN	1
20	2022-07-07 00:00:00	Raychel	t	2022-02-17 00:00:00	Trayling	ORTuLGhlD6	ACTIVE	rtraylingj	HN	1
21	2022-05-13 00:00:00	Durward	t	2022-01-20 00:00:00	Frude	2XOBNeAJ	ACTIVE	dfrudek	HN	1
23	2021-07-31 00:00:00	Derwin	f	2022-06-22 00:00:00	Smoth	SE2S0jeHvIOz	ACTIVE	dsmothm	HN	2
24	2021-08-11 00:00:00	Mason	f	2022-03-16 00:00:00	Grebner	DNYV2QMne5	ACTIVE	mgrebnern	HN	2
25	2022-07-04 00:00:00	Thorpe	f	2021-11-06 00:00:00	Branton	4fBGUA21	ACTIVE	tbrantono	HN	2
26	2021-09-07 00:00:00	Jermayne	t	2021-07-23 00:00:00	Dominguez	FT1OQ9oMGfBJ	ACTIVE	jdominguezp	HCM	2
27	2021-08-21 00:00:00	Nonnah	f	2022-06-04 00:00:00	Chisolm	SAU9ZwV	ACTIVE	nchisolmq	HCM	2
28	2021-08-22 00:00:00	Evered	t	2022-04-15 00:00:00	Yakob	gIrlzzPWZ	ACTIVE	eyakobr	HCM	2
29	2021-10-12 00:00:00	Ardith	f	2022-04-19 00:00:00	Benwell	P910t6SPYfv	ACTIVE	abenwells	HCM	2
3	2021-10-11 00:00:00	Merrilee	t	2022-06-16 00:00:00	Primrose	urrZJuXT2Ak	ACTIVE	mprimrose2	HCM	2
30	2022-05-24 00:00:00	Justino	t	2022-04-27 00:00:00	Vasilchenko	EpCO8fYZ	ACTIVE	jvasilchenkot	HCM	2
31	2022-01-28 00:00:00	Benjamin	f	2022-04-27 00:00:00	Scholig	AIpdCnjrQVT	ACTIVE	bscholigu	HCM	2
32	2021-11-21 00:00:00	Mile	f	2022-07-14 00:00:00	Wolfe	i8quGjOW	ACTIVE	mwolfev	HCM	2
33	2021-12-15 00:00:00	Timotheus	f	2022-02-25 00:00:00	Haggeth	yuZ610l5dl	\N	thaggethw	HCM	2
34	2022-05-04 00:00:00	Nathan	t	2022-03-01 00:00:00	Goodridge	SidV8hJn	\N	ngoodridgex	HCM	2
35	2021-09-12 00:00:00	Mirabelle	f	2021-12-14 00:00:00	Petrolli	9EQwXE113z	\N	mpetrolliy	HCM	2
36	2022-04-16 00:00:00	Irvine	t	2021-11-15 00:00:00	Gooddy	rBg9rgiWPS55	\N	igooddyz	HCM	2
37	2021-07-23 00:00:00	Beckie	t	2021-10-26 00:00:00	Grills	9Wv2Qge	\N	bgrills10	HCM	2
38	2021-12-30 00:00:00	Trev	f	2022-01-03 00:00:00	Nower	MEd648qc0G	\N	tnower11	HCM	2
39	2022-04-25 00:00:00	Gabbi	f	2021-11-17 00:00:00	Fitzjohn	bHJvuP32	\N	gfitzjohn12	HCM	2
4	2021-12-15 00:00:00	Keenan	f	2022-01-01 00:00:00	Carillo	aeaWV7YLD	\N	kcarillo3	HCM	2
40	2022-04-29 00:00:00	Germain	t	2022-03-09 00:00:00	Yakobowitz	8TwdjWArO	\N	gyakobowitz13	HCM	2
41	2022-03-11 00:00:00	Ches	f	2022-03-11 00:00:00	Janik	GeLyurUx4	\N	cjanik14	HCM	2
42	2022-01-31 00:00:00	Sabra	t	2021-11-21 00:00:00	Yantsev	cuktyn	\N	syantsev15	HCM	2
43	2021-08-29 00:00:00	Gladi	f	2021-09-18 00:00:00	Tythe	L1JIqMuzOp2	\N	gtythe16	HCM	2
44	2022-04-29 00:00:00	Lucila	t	2022-02-21 00:00:00	Hallin	NKUtkp	\N	lhallin17	HCM	2
45	2021-10-15 00:00:00	Brendin	t	2021-12-05 00:00:00	Reilingen	Y3My5MX5Q	\N	breilingen18	HCM	2
46	2022-01-10 00:00:00	Isabel	f	2022-05-07 00:00:00	Cassey	AgG0OJ	\N	icassey19	HN	2
47	2022-07-13 00:00:00	Monica	f	2022-05-17 00:00:00	Malley	nhZT5Wrn	\N	mmalley1a	HN	2
48	2022-07-14 00:00:00	Laurella	t	2022-03-11 00:00:00	LLelweln	cbBAAflkO	\N	lllelweln1b	HCM	2
49	2022-02-11 00:00:00	Hasheem	t	2022-07-06 00:00:00	Rosenberg	00gS5UDYcQb	\N	hrosenberg1c	HCM	2
5	2022-04-01 00:00:00	Franni	f	2022-06-26 00:00:00	Lansberry	ukIVFh08O	\N	flansberry4	HCM	2
50	2021-11-18 00:00:00	Iormina	t	2021-11-10 00:00:00	Harder	XlnhyWPAVA	\N	iharder1d	HCM	2
51	2022-07-21 00:00:00	Alec	t	2022-06-03 00:00:00	Whysall	igLgcMH5gDI9	\N	awhysall1e	HCM	2
52	2021-11-19 00:00:00	Bathsheba	f	2022-02-02 00:00:00	Roscamp	ukvCMoqejiG	\N	broscamp1f	HCM	2
53	2022-05-09 00:00:00	Morey	t	2021-12-20 00:00:00	Smooth	BuL9HVX	\N	msmooth1g	HCM	1
54	2022-01-11 00:00:00	Obediah	t	2022-04-01 00:00:00	Paradis	5GzaSrsn3	\N	oparadis1h	HCM	1
55	2022-06-25 00:00:00	Rutger	f	2022-06-05 00:00:00	Harkness	3RXQKL0pL	\N	rharkness1i	HCM	1
56	2022-03-14 00:00:00	Emmalynn	t	2022-05-20 00:00:00	Blanket	S0qOsx71kJU	\N	eblanket1j	HCM	1
57	2021-08-06 00:00:00	Ezekiel	t	2022-02-13 00:00:00	Josephy	EZy5HcwMHy9	\N	ejosephy1k	HCM	1
58	2021-12-13 00:00:00	Dexter	f	2021-11-20 00:00:00	Haresnaip	IFNtQQkzug	\N	dharesnaip1l	HCM	1
59	2022-05-28 00:00:00	Carla	t	2022-01-31 00:00:00	Mennell	dy6u0gqNt	\N	cmennell1m	HCM	1
6	2022-03-05 00:00:00	Agace	f	2022-03-25 00:00:00	Schofield	EYWZFHaFXz	\N	aschofield5	HCM	1
60	2022-03-17 00:00:00	Lavena	f	2022-04-05 00:00:00	Muspratt	1nQBdQd	\N	lmuspratt1n	HCM	1
7	2021-09-12 00:00:00	Shanon	f	2021-09-14 00:00:00	Cathcart	8grxYT	\N	scathcart6	HCM	1
8	2021-08-24 00:00:00	Carole	t	2021-11-14 00:00:00	Winchurch	cLcR9MMqjml	\N	cwinchurch7	HCM	2
9	2021-07-25 00:00:00	Jocelin	f	2021-08-21 00:00:00	Jose	ChnLPpv9KHXh	\N	jjose8	HCM	2
SD001	2000-01-01 00:00:00	Pham	f	2022-01-01 00:00:00	Quoc	$2a$12$s4kBd2orOFTrguMyfogm4O.6AXExpzUnK2x3REaA8xRq4enKt/JAa	INIT	pquoc	HCM	1
SD002	2000-01-01 00:00:00	Nguyen	t	2022-01-01 00:00:00	Trai	$2a$12$s4kBd2orOFTrguMyfogm4O.6AXExpzUnK2x3REaA8xRq4enKt/JAa	INIT	ntrai	HCM	1
SD0005	2000-02-28 07:00:00	Long	t	2022-07-19 07:00:00	Test	$2a$12$PLXbRwFzONBeHFIl4Fnf2O4c7EHvw	ACTIVE	tonhue	HN	1
SD0003	2000-07-23 07:00:00	Hoang Long	t	2022-07-21 07:00:00	Truong	hoang longt@23072000	ACTIVE	hoang longt	HN	1
SD0004	2000-02-29 07:00:00	Test 	f	2022-07-25 07:00:00	Edit	test e@28022000	ACTIVE	test e	HN	2
22	2022-04-05 00:00:00	Marrissa	t	2021-11-18 00:00:00	Cornfield	$2a$12$PLXbRwFzONBeHFIl4Fnf2O4c7EHvw/Jv7SbGWw/GuK0WtIecTFzy6\n	ACTIVE	hoanglong123	HN	1
1	2021-09-12 00:00:00	Crissie	t	2021-10-24 00:00:00	tonhue	$2a$10$pp1NLnyUF1py9jzYzeYo0eDDUeqhkqpJ7ATeVOOxGgX3iAxM8AapK	ACTIVE	tonhue1	HN	1
\.


--
-- TOC entry 3360 (class 0 OID 0)
-- Dependencies: 214
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_id_seq', 1, false);


--
-- TOC entry 3186 (class 2606 OID 42539)
-- Name: asset asset_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.asset
    ADD CONSTRAINT asset_pkey PRIMARY KEY (asset_code);


--
-- TOC entry 3188 (class 2606 OID 42541)
-- Name: assignment assignment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assignment
    ADD CONSTRAINT assignment_pkey PRIMARY KEY (asset_code, assigned_date, assigned_to);


--
-- TOC entry 3190 (class 2606 OID 42543)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 3192 (class 2606 OID 42545)
-- Name: location location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_pkey PRIMARY KEY (code);


--
-- TOC entry 3194 (class 2606 OID 42547)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 3196 (class 2606 OID 42549)
-- Name: role uk_epk9im9l9q67xmwi4hbed25do; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT uk_epk9im9l9q67xmwi4hbed25do UNIQUE (name);


--
-- TOC entry 3198 (class 2606 OID 42551)
-- Name: users uk_sx468g52bpetvlad2j9y0lptc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_sx468g52bpetvlad2j9y0lptc UNIQUE (username);


--
-- TOC entry 3200 (class 2606 OID 42553)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (staff_code);


--
-- TOC entry 3203 (class 2606 OID 42554)
-- Name: assignment fk2u12yqg906ih3ak0wmlu0y40b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assignment
    ADD CONSTRAINT fk2u12yqg906ih3ak0wmlu0y40b FOREIGN KEY (asset_code) REFERENCES public.asset(asset_code);


--
-- TOC entry 3206 (class 2606 OID 42559)
-- Name: users fk9r0nkg8ayb3vshdjs3lsx7627; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk9r0nkg8ayb3vshdjs3lsx7627 FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- TOC entry 3204 (class 2606 OID 42564)
-- Name: assignment fkbs00pqi04jqoa78vbiafk7qql; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assignment
    ADD CONSTRAINT fkbs00pqi04jqoa78vbiafk7qql FOREIGN KEY (assigned_to) REFERENCES public.users(staff_code);


--
-- TOC entry 3201 (class 2606 OID 42569)
-- Name: asset fke69ydkxgcthslax73274q33fs; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.asset
    ADD CONSTRAINT fke69ydkxgcthslax73274q33fs FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- TOC entry 3207 (class 2606 OID 42574)
-- Name: users fkfel1rc0t5lmtkf7r58pebp8h6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkfel1rc0t5lmtkf7r58pebp8h6 FOREIGN KEY (location_id) REFERENCES public.location(code);


--
-- TOC entry 3205 (class 2606 OID 42579)
-- Name: assignment fkh3qsayk0grj2k2v4l7pbgydky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assignment
    ADD CONSTRAINT fkh3qsayk0grj2k2v4l7pbgydky FOREIGN KEY (assigned_by) REFERENCES public.users(staff_code);


--
-- TOC entry 3202 (class 2606 OID 42584)
-- Name: asset fkoo11h2f4j12wv0axk6d8u1wy0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.asset
    ADD CONSTRAINT fkoo11h2f4j12wv0axk6d8u1wy0 FOREIGN KEY (location_id) REFERENCES public.location(code);


-- Completed on 2022-07-27 10:35:16

--
-- PostgreSQL database dump complete
--

