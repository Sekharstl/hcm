--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.0

-- Started on 2025-07-11 04:57:36

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE stl_hcm_db;
--
-- TOC entry 3971 (class 1262 OID 16384)
-- Name: stl_hcm_db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE stl_hcm_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.UTF-8';


ALTER DATABASE stl_hcm_db OWNER TO postgres;

\connect stl_hcm_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 16402)
-- Name: hcm; Type: SCHEMA; Schema: -; Owner: hcm_admin
--

CREATE SCHEMA hcm;


ALTER SCHEMA hcm OWNER TO hcm_admin;

--
-- TOC entry 292 (class 1255 OID 16927)
-- Name: fn_set_updated_at(); Type: FUNCTION; Schema: hcm; Owner: postgres
--

CREATE FUNCTION hcm.fn_set_updated_at() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$;


ALTER FUNCTION hcm.fn_set_updated_at() OWNER TO postgres;

--
-- TOC entry 293 (class 1255 OID 16953)
-- Name: fn_soft_delete_candidate(); Type: FUNCTION; Schema: hcm; Owner: postgres
--

CREATE FUNCTION hcm.fn_soft_delete_candidate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  UPDATE hcm.candidate SET deleted_at = now(), deleted_by = OLD.updated_by WHERE candidate_id = OLD.candidate_id;
  RETURN NULL;
END;
$$;


ALTER FUNCTION hcm.fn_soft_delete_candidate() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 224 (class 1259 OID 16460)
-- Name: department; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.department (
    department_id integer NOT NULL,
    tenant_id uuid NOT NULL,
    organization_id uuid NOT NULL,
    name character varying(200) NOT NULL,
    parent_department_id integer,
    status_id integer NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.department OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16447)
-- Name: department_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.department_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    description text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.department_status OWNER TO postgres;

--
-- TOC entry 261 (class 1259 OID 16923)
-- Name: active_departments; Type: VIEW; Schema: hcm; Owner: postgres
--

CREATE VIEW hcm.active_departments AS
 SELECT department_id,
    tenant_id,
    organization_id,
    name,
    parent_department_id,
    status_id,
    created_at,
    created_by,
    updated_at,
    updated_by
   FROM hcm.department
  WHERE (status_id = ( SELECT department_status.status_id
           FROM hcm.department_status
          WHERE ((department_status.name)::text = 'Active'::text)));


ALTER VIEW hcm.active_departments OWNER TO postgres;

--
-- TOC entry 265 (class 1259 OID 17001)
-- Name: application; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.application (
    application_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    requisition_id integer NOT NULL,
    status_id integer NOT NULL,
    applied_date date DEFAULT now() NOT NULL,
    source character varying(100),
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.application OWNER TO postgres;

--
-- TOC entry 264 (class 1259 OID 17000)
-- Name: application_application_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.application_application_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.application_application_id_seq OWNER TO postgres;

--
-- TOC entry 3977 (class 0 OID 0)
-- Dependencies: 264
-- Name: application_application_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.application_application_id_seq OWNED BY hcm.application.application_id;


--
-- TOC entry 232 (class 1259 OID 16579)
-- Name: application_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.application_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.application_status OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 16578)
-- Name: application_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.application_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.application_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 3979 (class 0 OID 0)
-- Dependencies: 231
-- Name: application_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.application_status_status_id_seq OWNED BY hcm.application_status.status_id;


--
-- TOC entry 267 (class 1259 OID 17026)
-- Name: approval; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.approval (
    approval_id integer NOT NULL,
    requisition_id integer NOT NULL,
    approver_id uuid NOT NULL,
    status_id integer NOT NULL,
    action_date date DEFAULT now() NOT NULL,
    comments text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.approval OWNER TO postgres;

--
-- TOC entry 266 (class 1259 OID 17025)
-- Name: approval_approval_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.approval_approval_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.approval_approval_id_seq OWNER TO postgres;

--
-- TOC entry 3981 (class 0 OID 0)
-- Dependencies: 266
-- Name: approval_approval_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.approval_approval_id_seq OWNED BY hcm.approval.approval_id;


--
-- TOC entry 234 (class 1259 OID 16600)
-- Name: approval_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.approval_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.approval_status OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 16599)
-- Name: approval_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.approval_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.approval_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 3983 (class 0 OID 0)
-- Dependencies: 233
-- Name: approval_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.approval_status_status_id_seq OWNED BY hcm.approval_status.status_id;


--
-- TOC entry 251 (class 1259 OID 16816)
-- Name: candidate; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.candidate (
    candidate_id uuid NOT NULL,
    tenant_id uuid NOT NULL,
    organization_id uuid NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    email character varying(200) NOT NULL,
    phone character varying(20),
    address character varying(500),
    date_of_birth date,
    gender character varying(50),
    nationality character varying(100),
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL,
    deleted_at timestamp with time zone DEFAULT now(),
    deleted_by uuid,
    availability_date date,
    city character varying(100),
    country character varying(100),
    current_salary numeric(12,2),
    expected_salary numeric(12,2),
    linkedin_url character varying(255),
    middle_name character varying(100),
    notes character varying(255),
    notice_period integer,
    postal_code character varying(20),
    source character varying(100),
    state character varying(100),
    status character varying(50),
    vendor_id uuid
);


ALTER TABLE hcm.candidate OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 16871)
-- Name: candidate_certification; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.candidate_certification (
    certification_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    certificate_name character varying(200),
    issued_by character varying(200),
    issue_date date,
    expiry_date date,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL,
    certification_name character varying(100),
    issuing_organization character varying(100)
);


ALTER TABLE hcm.candidate_certification OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 16870)
-- Name: candidate_certification_certification_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.candidate_certification_certification_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.candidate_certification_certification_id_seq OWNER TO postgres;

--
-- TOC entry 3986 (class 0 OID 0)
-- Dependencies: 256
-- Name: candidate_certification_certification_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.candidate_certification_certification_id_seq OWNED BY hcm.candidate_certification.certification_id;


--
-- TOC entry 279 (class 1259 OID 32770)
-- Name: candidate_document; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.candidate_document (
    document_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid NOT NULL,
    document_type_id integer NOT NULL,
    expiry_date date,
    file_name character varying(255) NOT NULL,
    file_path character varying(500) NOT NULL,
    file_size bigint NOT NULL,
    is_verified boolean,
    mime_type character varying(100),
    original_file_name character varying(255) NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    updated_by uuid NOT NULL,
    upload_date timestamp(6) with time zone NOT NULL,
    verification_date timestamp(6) with time zone,
    verified_by uuid
);


ALTER TABLE hcm.candidate_document OWNER TO postgres;

--
-- TOC entry 278 (class 1259 OID 32769)
-- Name: candidate_document_document_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.candidate_document_document_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.candidate_document_document_id_seq OWNER TO postgres;

--
-- TOC entry 3987 (class 0 OID 0)
-- Dependencies: 278
-- Name: candidate_document_document_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.candidate_document_document_id_seq OWNED BY hcm.candidate_document.document_id;


--
-- TOC entry 253 (class 1259 OID 16839)
-- Name: candidate_education; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.candidate_education (
    education_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    institution character varying(200),
    degree character varying(100),
    field_of_study character varying(100),
    start_date date,
    end_date date,
    grade character varying(10),
    notes text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL,
    description character varying(255),
    institution_name character varying(255)
);


ALTER TABLE hcm.candidate_education OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 16838)
-- Name: candidate_education_education_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.candidate_education_education_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.candidate_education_education_id_seq OWNER TO postgres;

--
-- TOC entry 3989 (class 0 OID 0)
-- Dependencies: 252
-- Name: candidate_education_education_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.candidate_education_education_id_seq OWNED BY hcm.candidate_education.education_id;


--
-- TOC entry 281 (class 1259 OID 32779)
-- Name: candidate_identity; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.candidate_identity (
    identity_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid NOT NULL,
    expiry_date date,
    id_number character varying(100) NOT NULL,
    id_type_id integer NOT NULL,
    is_verified boolean,
    issue_date date,
    issuing_country character varying(100),
    updated_at timestamp(6) with time zone NOT NULL,
    updated_by uuid NOT NULL,
    verification_date timestamp(6) with time zone,
    verified_by uuid
);


ALTER TABLE hcm.candidate_identity OWNER TO postgres;

--
-- TOC entry 280 (class 1259 OID 32778)
-- Name: candidate_identity_identity_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.candidate_identity_identity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.candidate_identity_identity_id_seq OWNER TO postgres;

--
-- TOC entry 3990 (class 0 OID 0)
-- Dependencies: 280
-- Name: candidate_identity_identity_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.candidate_identity_identity_id_seq OWNED BY hcm.candidate_identity.identity_id;


--
-- TOC entry 283 (class 1259 OID 32786)
-- Name: candidate_reference; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.candidate_reference (
    reference_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    company character varying(200),
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid NOT NULL,
    email character varying(255),
    is_verified boolean,
    phone character varying(20),
    "position" character varying(200),
    reference_name character varying(200) NOT NULL,
    relationship character varying(100),
    updated_at timestamp(6) with time zone NOT NULL,
    updated_by uuid NOT NULL,
    verification_date timestamp(6) with time zone,
    verified_by uuid
);


ALTER TABLE hcm.candidate_reference OWNER TO postgres;

--
-- TOC entry 282 (class 1259 OID 32785)
-- Name: candidate_reference_reference_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.candidate_reference_reference_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.candidate_reference_reference_id_seq OWNER TO postgres;

--
-- TOC entry 3991 (class 0 OID 0)
-- Dependencies: 282
-- Name: candidate_reference_reference_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.candidate_reference_reference_id_seq OWNED BY hcm.candidate_reference.reference_id;


--
-- TOC entry 260 (class 1259 OID 16905)
-- Name: candidate_skill; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.candidate_skill (
    candidate_id uuid NOT NULL,
    skill_id integer NOT NULL,
    proficiency_level character varying(50),
    years_of_experience integer,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.candidate_skill OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 16855)
-- Name: candidate_work_history; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.candidate_work_history (
    work_history_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    company_name character varying(200),
    position_title character varying(100),
    location character varying(200),
    start_date date,
    end_date date,
    responsibilities text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL,
    description character varying(500),
    job_title character varying(100)
);


ALTER TABLE hcm.candidate_work_history OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 16854)
-- Name: candidate_work_history_work_history_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.candidate_work_history_work_history_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.candidate_work_history_work_history_id_seq OWNER TO postgres;

--
-- TOC entry 3994 (class 0 OID 0)
-- Dependencies: 254
-- Name: candidate_work_history_work_history_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.candidate_work_history_work_history_id_seq OWNED BY hcm.candidate_work_history.work_history_id;


--
-- TOC entry 287 (class 1259 OID 32987)
-- Name: country; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.country (
    country_id integer NOT NULL,
    country_code character varying(3) NOT NULL,
    country_name character varying(100) NOT NULL,
    phone_code character varying(10),
    currency_code character varying(3),
    is_active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.country OWNER TO postgres;

--
-- TOC entry 286 (class 1259 OID 32986)
-- Name: country_country_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.country_country_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.country_country_id_seq OWNER TO postgres;

--
-- TOC entry 3995 (class 0 OID 0)
-- Dependencies: 286
-- Name: country_country_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.country_country_id_seq OWNED BY hcm.country.country_id;


--
-- TOC entry 223 (class 1259 OID 16459)
-- Name: department_department_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.department_department_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.department_department_id_seq OWNER TO postgres;

--
-- TOC entry 3996 (class 0 OID 0)
-- Dependencies: 223
-- Name: department_department_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.department_department_id_seq OWNED BY hcm.department.department_id;


--
-- TOC entry 221 (class 1259 OID 16446)
-- Name: department_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.department_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.department_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 3997 (class 0 OID 0)
-- Dependencies: 221
-- Name: department_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.department_status_status_id_seq OWNED BY hcm.department_status.status_id;


--
-- TOC entry 285 (class 1259 OID 32967)
-- Name: document_type; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.document_type (
    document_type_id integer NOT NULL,
    tenant_id uuid NOT NULL,
    type_name character varying(100) NOT NULL,
    description text,
    is_required boolean DEFAULT false,
    max_file_size integer DEFAULT 10485760,
    allowed_extensions character varying(500),
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.document_type OWNER TO postgres;

--
-- TOC entry 284 (class 1259 OID 32966)
-- Name: document_type_document_type_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.document_type_document_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.document_type_document_type_id_seq OWNER TO postgres;

--
-- TOC entry 3998 (class 0 OID 0)
-- Dependencies: 284
-- Name: document_type_document_type_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.document_type_document_type_id_seq OWNED BY hcm.document_type.document_type_id;


--
-- TOC entry 291 (class 1259 OID 33016)
-- Name: id_type; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.id_type (
    id_type_id integer NOT NULL,
    tenant_id uuid NOT NULL,
    type_name character varying(100) NOT NULL,
    description text,
    is_required boolean DEFAULT false,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.id_type OWNER TO postgres;

--
-- TOC entry 290 (class 1259 OID 33015)
-- Name: id_type_id_type_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.id_type_id_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.id_type_id_type_id_seq OWNER TO postgres;

--
-- TOC entry 3999 (class 0 OID 0)
-- Dependencies: 290
-- Name: id_type_id_type_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.id_type_id_type_id_seq OWNED BY hcm.id_type.id_type_id;


--
-- TOC entry 275 (class 1259 OID 17121)
-- Name: interview; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.interview (
    interview_id integer NOT NULL,
    application_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    requisition_id integer NOT NULL,
    interviewer_id uuid NOT NULL,
    status_id integer NOT NULL,
    scheduled_date date,
    mode character varying(50),
    location character varying(200),
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.interview OWNER TO postgres;

--
-- TOC entry 277 (class 1259 OID 17155)
-- Name: interview_feedback; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.interview_feedback (
    feedback_id integer NOT NULL,
    interview_id integer NOT NULL,
    interviewer_id uuid NOT NULL,
    candidate_id uuid NOT NULL,
    feedback text,
    rating integer,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.interview_feedback OWNER TO postgres;

--
-- TOC entry 276 (class 1259 OID 17154)
-- Name: interview_feedback_feedback_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.interview_feedback_feedback_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.interview_feedback_feedback_id_seq OWNER TO postgres;

--
-- TOC entry 4002 (class 0 OID 0)
-- Dependencies: 276
-- Name: interview_feedback_feedback_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.interview_feedback_feedback_id_seq OWNED BY hcm.interview_feedback.feedback_id;


--
-- TOC entry 274 (class 1259 OID 17120)
-- Name: interview_interview_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.interview_interview_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.interview_interview_id_seq OWNER TO postgres;

--
-- TOC entry 4003 (class 0 OID 0)
-- Dependencies: 274
-- Name: interview_interview_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.interview_interview_id_seq OWNED BY hcm.interview.interview_id;


--
-- TOC entry 243 (class 1259 OID 16709)
-- Name: interview_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.interview_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.interview_status OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 16708)
-- Name: interview_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.interview_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.interview_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 4005 (class 0 OID 0)
-- Dependencies: 242
-- Name: interview_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.interview_status_status_id_seq OWNED BY hcm.interview_status.status_id;


--
-- TOC entry 263 (class 1259 OID 16957)
-- Name: job_requisition; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.job_requisition (
    requisition_id integer NOT NULL,
    tenant_id uuid NOT NULL,
    organization_id uuid NOT NULL,
    position_id integer NOT NULL,
    department_id integer NOT NULL,
    title character varying(200) NOT NULL,
    location character varying(200),
    employment_type character varying(50),
    posted_date date NOT NULL,
    closing_date date,
    status_id integer NOT NULL,
    hiring_manager_id uuid,
    vendor_id uuid,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.job_requisition OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 16956)
-- Name: job_requisition_requisition_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.job_requisition_requisition_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.job_requisition_requisition_id_seq OWNER TO postgres;

--
-- TOC entry 4007 (class 0 OID 0)
-- Dependencies: 262
-- Name: job_requisition_requisition_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.job_requisition_requisition_id_seq OWNED BY hcm.job_requisition.requisition_id;


--
-- TOC entry 230 (class 1259 OID 16534)
-- Name: job_requisition_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.job_requisition_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.job_requisition_status OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 16533)
-- Name: job_requisition_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.job_requisition_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.job_requisition_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 4009 (class 0 OID 0)
-- Dependencies: 229
-- Name: job_requisition_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.job_requisition_status_status_id_seq OWNED BY hcm.job_requisition_status.status_id;


--
-- TOC entry 271 (class 1259 OID 17067)
-- Name: offer; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.offer (
    offer_id integer NOT NULL,
    application_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    requisition_id integer NOT NULL,
    status_id integer NOT NULL,
    salary numeric(12,2),
    currency character varying(3),
    offer_date date DEFAULT now() NOT NULL,
    acceptance_deadline date,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.offer OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 17066)
-- Name: offer_offer_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.offer_offer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.offer_offer_id_seq OWNER TO postgres;

--
-- TOC entry 4011 (class 0 OID 0)
-- Dependencies: 270
-- Name: offer_offer_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.offer_offer_id_seq OWNED BY hcm.offer.offer_id;


--
-- TOC entry 239 (class 1259 OID 16668)
-- Name: offer_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.offer_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.offer_status OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 16667)
-- Name: offer_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.offer_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.offer_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 4013 (class 0 OID 0)
-- Dependencies: 238
-- Name: offer_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.offer_status_status_id_seq OWNED BY hcm.offer_status.status_id;


--
-- TOC entry 273 (class 1259 OID 17097)
-- Name: onboarding; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.onboarding (
    onboarding_id integer NOT NULL,
    offer_id integer NOT NULL,
    candidate_id uuid NOT NULL,
    status_id integer NOT NULL,
    start_date date,
    orientation_date date,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.onboarding OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 17096)
-- Name: onboarding_onboarding_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.onboarding_onboarding_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.onboarding_onboarding_id_seq OWNER TO postgres;

--
-- TOC entry 4015 (class 0 OID 0)
-- Dependencies: 272
-- Name: onboarding_onboarding_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.onboarding_onboarding_id_seq OWNED BY hcm.onboarding.onboarding_id;


--
-- TOC entry 241 (class 1259 OID 16689)
-- Name: onboarding_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.onboarding_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.onboarding_status OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 16688)
-- Name: onboarding_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.onboarding_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.onboarding_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 4017 (class 0 OID 0)
-- Dependencies: 240
-- Name: onboarding_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.onboarding_status_status_id_seq OWNED BY hcm.onboarding_status.status_id;


--
-- TOC entry 220 (class 1259 OID 16426)
-- Name: organization; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.organization (
    organization_id uuid NOT NULL,
    tenant_id uuid NOT NULL,
    name character varying(200) NOT NULL,
    address character varying(500),
    status_id integer NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.organization OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16414)
-- Name: organization_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.organization_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    description text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.organization_status OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16413)
-- Name: organization_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.organization_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.organization_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 4020 (class 0 OID 0)
-- Dependencies: 218
-- Name: organization_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.organization_status_status_id_seq OWNED BY hcm.organization_status.status_id;


--
-- TOC entry 269 (class 1259 OID 17053)
-- Name: pipeline_stage; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.pipeline_stage (
    stage_id integer NOT NULL,
    requisition_id integer NOT NULL,
    name character varying(100) NOT NULL,
    sequence integer NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.pipeline_stage OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 17052)
-- Name: pipeline_stage_stage_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.pipeline_stage_stage_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.pipeline_stage_stage_id_seq OWNER TO postgres;

--
-- TOC entry 4022 (class 0 OID 0)
-- Dependencies: 268
-- Name: pipeline_stage_stage_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.pipeline_stage_stage_id_seq OWNED BY hcm.pipeline_stage.stage_id;


--
-- TOC entry 228 (class 1259 OID 16502)
-- Name: position; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm."position" (
    position_id integer NOT NULL,
    tenant_id uuid NOT NULL,
    organization_id uuid NOT NULL,
    department_id integer NOT NULL,
    title character varying(200) NOT NULL,
    location character varying(200),
    description text,
    employment_type character varying(50),
    status_id integer NOT NULL,
    headcount integer DEFAULT 1,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm."position" OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16501)
-- Name: position_position_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.position_position_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.position_position_id_seq OWNER TO postgres;

--
-- TOC entry 4024 (class 0 OID 0)
-- Dependencies: 227
-- Name: position_position_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.position_position_id_seq OWNED BY hcm."position".position_id;


--
-- TOC entry 226 (class 1259 OID 16489)
-- Name: position_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.position_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    description text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.position_status OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16488)
-- Name: position_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.position_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.position_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 4026 (class 0 OID 0)
-- Dependencies: 225
-- Name: position_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.position_status_status_id_seq OWNED BY hcm.position_status.status_id;


--
-- TOC entry 259 (class 1259 OID 16885)
-- Name: skill; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.skill (
    skill_id integer NOT NULL,
    tenant_id uuid NOT NULL,
    organization_id uuid NOT NULL,
    skill_name character varying(100),
    skill_category character varying(100),
    description text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.skill OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 16884)
-- Name: skill_skill_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.skill_skill_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.skill_skill_id_seq OWNER TO postgres;

--
-- TOC entry 4028 (class 0 OID 0)
-- Dependencies: 258
-- Name: skill_skill_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.skill_skill_id_seq OWNED BY hcm.skill.skill_id;


--
-- TOC entry 289 (class 1259 OID 32999)
-- Name: state; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.state (
    state_id integer NOT NULL,
    country_id integer NOT NULL,
    state_code character varying(10) NOT NULL,
    state_name character varying(100) NOT NULL,
    is_active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.state OWNER TO postgres;

--
-- TOC entry 288 (class 1259 OID 32998)
-- Name: state_state_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.state_state_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.state_state_id_seq OWNER TO postgres;

--
-- TOC entry 4029 (class 0 OID 0)
-- Dependencies: 288
-- Name: state_state_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.state_state_id_seq OWNED BY hcm.state.state_id;


--
-- TOC entry 217 (class 1259 OID 16403)
-- Name: tenant; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.tenant (
    tenant_id uuid NOT NULL,
    name character varying(200) NOT NULL,
    domain character varying(200) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.tenant OWNER TO postgres;

--
-- TOC entry 4030 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE tenant; Type: COMMENT; Schema: hcm; Owner: postgres
--

COMMENT ON TABLE hcm.tenant IS 'Stores tenants of the HCM application';


--
-- TOC entry 4031 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN tenant.domain; Type: COMMENT; Schema: hcm; Owner: postgres
--

COMMENT ON COLUMN hcm.tenant.domain IS 'Unique domain for tenant routing';


--
-- TOC entry 250 (class 1259 OID 16777)
-- Name: user; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm."user" (
    user_id uuid NOT NULL,
    tenant_id uuid NOT NULL,
    organization_id uuid NOT NULL,
    username character varying(100) NOT NULL,
    email character varying(200) NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    role_id integer NOT NULL,
    type_id integer NOT NULL,
    status_id integer NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm."user" OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 16753)
-- Name: user_role; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.user_role (
    role_id integer NOT NULL,
    name character varying(100) NOT NULL,
    permissions jsonb DEFAULT '[]'::jsonb NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.user_role OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 16752)
-- Name: user_role_role_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.user_role_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.user_role_role_id_seq OWNER TO postgres;

--
-- TOC entry 4035 (class 0 OID 0)
-- Dependencies: 246
-- Name: user_role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.user_role_role_id_seq OWNED BY hcm.user_role.role_id;


--
-- TOC entry 249 (class 1259 OID 16767)
-- Name: user_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.user_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.user_status OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 16766)
-- Name: user_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.user_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.user_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 4037 (class 0 OID 0)
-- Dependencies: 248
-- Name: user_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.user_status_status_id_seq OWNED BY hcm.user_status.status_id;


--
-- TOC entry 245 (class 1259 OID 16740)
-- Name: user_type; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.user_type (
    type_id integer NOT NULL,
    name character varying(100) NOT NULL,
    description text,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.user_type OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 16739)
-- Name: user_type_type_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.user_type_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.user_type_type_id_seq OWNER TO postgres;

--
-- TOC entry 4039 (class 0 OID 0)
-- Dependencies: 244
-- Name: user_type_type_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.user_type_type_id_seq OWNED BY hcm.user_type.type_id;


--
-- TOC entry 237 (class 1259 OID 16642)
-- Name: vendor; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.vendor (
    vendor_id uuid NOT NULL,
    tenant_id uuid NOT NULL,
    organization_id uuid NOT NULL,
    vendor_name character varying(200) NOT NULL,
    contact_name character varying(200),
    contact_email character varying(200),
    contact_phone character varying(20),
    address character varying(500),
    status_id integer NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.vendor OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 16632)
-- Name: vendor_status; Type: TABLE; Schema: hcm; Owner: postgres
--

CREATE TABLE hcm.vendor_status (
    status_id integer NOT NULL,
    name character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by uuid NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_by uuid NOT NULL
);


ALTER TABLE hcm.vendor_status OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 16631)
-- Name: vendor_status_status_id_seq; Type: SEQUENCE; Schema: hcm; Owner: postgres
--

CREATE SEQUENCE hcm.vendor_status_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE hcm.vendor_status_status_id_seq OWNER TO postgres;

--
-- TOC entry 4042 (class 0 OID 0)
-- Dependencies: 235
-- Name: vendor_status_status_id_seq; Type: SEQUENCE OWNED BY; Schema: hcm; Owner: postgres
--

ALTER SEQUENCE hcm.vendor_status_status_id_seq OWNED BY hcm.vendor_status.status_id;


--
-- TOC entry 3480 (class 2604 OID 17004)
-- Name: application application_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.application ALTER COLUMN application_id SET DEFAULT nextval('hcm.application_application_id_seq'::regclass);


--
-- TOC entry 3428 (class 2604 OID 16582)
-- Name: application_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.application_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.application_status_status_id_seq'::regclass);


--
-- TOC entry 3484 (class 2604 OID 17029)
-- Name: approval approval_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.approval ALTER COLUMN approval_id SET DEFAULT nextval('hcm.approval_approval_id_seq'::regclass);


--
-- TOC entry 3431 (class 2604 OID 16603)
-- Name: approval_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.approval_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.approval_status_status_id_seq'::regclass);


--
-- TOC entry 3469 (class 2604 OID 16874)
-- Name: candidate_certification certification_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_certification ALTER COLUMN certification_id SET DEFAULT nextval('hcm.candidate_certification_certification_id_seq'::regclass);


--
-- TOC entry 3504 (class 2604 OID 32773)
-- Name: candidate_document document_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_document ALTER COLUMN document_id SET DEFAULT nextval('hcm.candidate_document_document_id_seq'::regclass);


--
-- TOC entry 3463 (class 2604 OID 16842)
-- Name: candidate_education education_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_education ALTER COLUMN education_id SET DEFAULT nextval('hcm.candidate_education_education_id_seq'::regclass);


--
-- TOC entry 3505 (class 2604 OID 32782)
-- Name: candidate_identity identity_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_identity ALTER COLUMN identity_id SET DEFAULT nextval('hcm.candidate_identity_identity_id_seq'::regclass);


--
-- TOC entry 3506 (class 2604 OID 32789)
-- Name: candidate_reference reference_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_reference ALTER COLUMN reference_id SET DEFAULT nextval('hcm.candidate_reference_reference_id_seq'::regclass);


--
-- TOC entry 3466 (class 2604 OID 16858)
-- Name: candidate_work_history work_history_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_work_history ALTER COLUMN work_history_id SET DEFAULT nextval('hcm.candidate_work_history_work_history_id_seq'::regclass);


--
-- TOC entry 3512 (class 2604 OID 32990)
-- Name: country country_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.country ALTER COLUMN country_id SET DEFAULT nextval('hcm.country_country_id_seq'::regclass);


--
-- TOC entry 3415 (class 2604 OID 16463)
-- Name: department department_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.department ALTER COLUMN department_id SET DEFAULT nextval('hcm.department_department_id_seq'::regclass);


--
-- TOC entry 3412 (class 2604 OID 16450)
-- Name: department_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.department_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.department_status_status_id_seq'::regclass);


--
-- TOC entry 3507 (class 2604 OID 32970)
-- Name: document_type document_type_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.document_type ALTER COLUMN document_type_id SET DEFAULT nextval('hcm.document_type_document_type_id_seq'::regclass);


--
-- TOC entry 3520 (class 2604 OID 33019)
-- Name: id_type id_type_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.id_type ALTER COLUMN id_type_id SET DEFAULT nextval('hcm.id_type_id_type_id_seq'::regclass);


--
-- TOC entry 3498 (class 2604 OID 17124)
-- Name: interview interview_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview ALTER COLUMN interview_id SET DEFAULT nextval('hcm.interview_interview_id_seq'::regclass);


--
-- TOC entry 3501 (class 2604 OID 17158)
-- Name: interview_feedback feedback_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview_feedback ALTER COLUMN feedback_id SET DEFAULT nextval('hcm.interview_feedback_feedback_id_seq'::regclass);


--
-- TOC entry 3445 (class 2604 OID 16712)
-- Name: interview_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.interview_status_status_id_seq'::regclass);


--
-- TOC entry 3477 (class 2604 OID 16960)
-- Name: job_requisition requisition_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition ALTER COLUMN requisition_id SET DEFAULT nextval('hcm.job_requisition_requisition_id_seq'::regclass);


--
-- TOC entry 3425 (class 2604 OID 16537)
-- Name: job_requisition_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.job_requisition_status_status_id_seq'::regclass);


--
-- TOC entry 3491 (class 2604 OID 17070)
-- Name: offer offer_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.offer ALTER COLUMN offer_id SET DEFAULT nextval('hcm.offer_offer_id_seq'::regclass);


--
-- TOC entry 3439 (class 2604 OID 16671)
-- Name: offer_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.offer_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.offer_status_status_id_seq'::regclass);


--
-- TOC entry 3495 (class 2604 OID 17100)
-- Name: onboarding onboarding_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.onboarding ALTER COLUMN onboarding_id SET DEFAULT nextval('hcm.onboarding_onboarding_id_seq'::regclass);


--
-- TOC entry 3442 (class 2604 OID 16692)
-- Name: onboarding_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.onboarding_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.onboarding_status_status_id_seq'::regclass);


--
-- TOC entry 3407 (class 2604 OID 16417)
-- Name: organization_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.organization_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.organization_status_status_id_seq'::regclass);


--
-- TOC entry 3488 (class 2604 OID 17056)
-- Name: pipeline_stage stage_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.pipeline_stage ALTER COLUMN stage_id SET DEFAULT nextval('hcm.pipeline_stage_stage_id_seq'::regclass);


--
-- TOC entry 3421 (class 2604 OID 16505)
-- Name: position position_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."position" ALTER COLUMN position_id SET DEFAULT nextval('hcm.position_position_id_seq'::regclass);


--
-- TOC entry 3418 (class 2604 OID 16492)
-- Name: position_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.position_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.position_status_status_id_seq'::regclass);


--
-- TOC entry 3472 (class 2604 OID 16888)
-- Name: skill skill_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.skill ALTER COLUMN skill_id SET DEFAULT nextval('hcm.skill_skill_id_seq'::regclass);


--
-- TOC entry 3516 (class 2604 OID 33002)
-- Name: state state_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.state ALTER COLUMN state_id SET DEFAULT nextval('hcm.state_state_id_seq'::regclass);


--
-- TOC entry 3451 (class 2604 OID 16756)
-- Name: user_role role_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.user_role ALTER COLUMN role_id SET DEFAULT nextval('hcm.user_role_role_id_seq'::regclass);


--
-- TOC entry 3455 (class 2604 OID 16770)
-- Name: user_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.user_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.user_status_status_id_seq'::regclass);


--
-- TOC entry 3448 (class 2604 OID 16743)
-- Name: user_type type_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.user_type ALTER COLUMN type_id SET DEFAULT nextval('hcm.user_type_type_id_seq'::regclass);


--
-- TOC entry 3434 (class 2604 OID 16635)
-- Name: vendor_status status_id; Type: DEFAULT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.vendor_status ALTER COLUMN status_id SET DEFAULT nextval('hcm.vendor_status_status_id_seq'::regclass);


--
-- TOC entry 3939 (class 0 OID 17001)
-- Dependencies: 265
-- Data for Name: application; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3907 (class 0 OID 16579)
-- Dependencies: 232
-- Data for Name: application_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3941 (class 0 OID 17026)
-- Dependencies: 267
-- Data for Name: approval; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3909 (class 0 OID 16600)
-- Dependencies: 234
-- Data for Name: approval_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3926 (class 0 OID 16816)
-- Dependencies: 251
-- Data for Name: candidate; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.candidate VALUES ('530b9ff4-aadb-4bd2-a23a-ef8fd837a117', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '08b06d14-4e03-11f0-bc56-325096b39f47', 'John2', 'Doe2', 'john2.doe1@example.com', '+919870345788', NULL, NULL, NULL, NULL, '2025-06-21 10:30:21.104+00', '00000000-0000-0000-0000-000000000000', '2025-06-21 10:30:21.104+00', '00000000-0000-0000-0000-000000000000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO hcm.candidate VALUES ('4626b874-53e4-46cf-9821-a9dbe514ecbf', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '08b06d14-4e03-11f0-bc56-325096b39f47', 'Test 1', 'Test 1', 'test1@stldigital.tech', '9087654243', 'Nagawara, bangalore', '1985-10-04', 'Male', 'indian', '2025-07-02 20:08:14.630772+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '2025-07-03 06:50:06.247516+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '2025-07-02 20:26:02.084166+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO hcm.candidate VALUES ('5ae80e4e-66ac-4d24-9afc-73ccbf296720', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '08b06d14-4e03-11f0-bc56-325096b39f47', 'John1', 'Doe1', 'john1.doe1@example.com', '+919870345788', 'vidyaranyapura post banglore 97', '1997-08-07', 'Male', 'indian', '2025-06-21 10:29:30.007+00', '00000000-0000-0000-0000-000000000000', '2025-07-03 07:23:30.707226+00', '00000000-0000-0000-0000-000000000000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO hcm.candidate VALUES ('fbe89413-c0bb-4b55-9e7e-ead0da5d199d', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '08b06d14-4e03-11f0-bc56-325096b39f47', 'John', 'doe', 'john.doe@example11.com', '3122142423', 'fewfewfew fewfew', '2432-12-31', 'Male', 'USA', '2025-07-03 06:42:30.703253+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '2025-07-03 09:27:01.260579+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO hcm.candidate VALUES ('ebd3cdc9-ad2a-4d5e-9053-ec1a2cf2c357', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '08b06d14-4e03-11f0-bc56-325096b39f47', 'testnew', 'newtest', 'testnew@test.com', '13214321421', 'test123', '2017-01-12', 'Male', 'Indian', '2025-07-03 06:47:03.992248+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '2025-07-03 09:38:06.375753+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO hcm.candidate VALUES ('30574003-1c92-4c14-b6e4-e5cda813dad1', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '08b06d14-4e03-11f0-bc56-325096b39f47', 'Mohammed', 'Waseem S', 'mohammed.s@stl.tech', '0000000000', 'vidyaranyapura post banglore 560097', '1996-10-09', 'Male', 'Indian', '2025-07-03 13:18:34.484731+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '2025-07-03 13:19:21.843446+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO hcm.candidate VALUES ('6881c664-1173-49e7-8732-481db2a04e41', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '08b06d14-4e03-11f0-bc56-325096b39f47', 'Vijay', 'Anand', 'vijayanand@stl.tech', '9876543210', 'Nagawara, bangalore', '1994-04-03', 'Male', 'Indian', '2025-07-04 12:34:19.003894+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '2025-07-04 12:34:19.003894+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


--
-- TOC entry 3932 (class 0 OID 16871)
-- Dependencies: 257
-- Data for Name: candidate_certification; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3953 (class 0 OID 32770)
-- Dependencies: 279
-- Data for Name: candidate_document; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3928 (class 0 OID 16839)
-- Dependencies: 253
-- Data for Name: candidate_education; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.candidate_education VALUES (1, '30574003-1c92-4c14-b6e4-e5cda813dad1', 'VIT', 'BE', 'ECE', '2015-04-05', '2019-07-07', 'A', 'test', '2025-07-10 12:26:41.995674+00', '4290f438-5333-a117-7aa5-ef89eb211645', '2025-07-10 12:26:41.995674+00', '4290f438-5333-a117-7aa5-ef89eb211645', NULL, NULL);


--
-- TOC entry 3955 (class 0 OID 32779)
-- Dependencies: 281
-- Data for Name: candidate_identity; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3957 (class 0 OID 32786)
-- Dependencies: 283
-- Data for Name: candidate_reference; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3935 (class 0 OID 16905)
-- Dependencies: 260
-- Data for Name: candidate_skill; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3930 (class 0 OID 16855)
-- Dependencies: 255
-- Data for Name: candidate_work_history; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3961 (class 0 OID 32987)
-- Dependencies: 287
-- Data for Name: country; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.country VALUES (1, 'USA', 'United States', '+1', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (2, 'CAN', 'Canada', '+1', 'CAD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (3, 'MEX', 'Mexico', '+52', 'MXN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (4, 'GTM', 'Guatemala', '+502', 'GTQ', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (5, 'BLZ', 'Belize', '+501', 'BZD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (6, 'SLV', 'El Salvador', '+503', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (7, 'HND', 'Honduras', '+504', 'HNL', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (8, 'NIC', 'Nicaragua', '+505', 'NIO', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (9, 'CRI', 'Costa Rica', '+506', 'CRC', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (10, 'PAN', 'Panama', '+507', 'PAB', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (11, 'CUB', 'Cuba', '+53', 'CUP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (12, 'JAM', 'Jamaica', '+1876', 'JMD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (13, 'HTI', 'Haiti', '+509', 'HTG', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (14, 'DOM', 'Dominican Republic', '+1809', 'DOP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (15, 'PRI', 'Puerto Rico', '+1787', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (16, 'BRB', 'Barbados', '+1246', 'BBD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (17, 'GRD', 'Grenada', '+1473', 'XCD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (18, 'LCA', 'Saint Lucia', '+1758', 'XCD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (19, 'VCT', 'Saint Vincent and the Grenadines', '+1784', 'XCD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (20, 'ATG', 'Antigua and Barbuda', '+1268', 'XCD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (21, 'DMA', 'Dominica', '+1767', 'XCD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (22, 'KNA', 'Saint Kitts and Nevis', '+1869', 'XCD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (23, 'TTO', 'Trinidad and Tobago', '+1868', 'TTD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (24, 'BHS', 'Bahamas', '+1242', 'BSD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (25, 'GBR', 'United Kingdom', '+44', 'GBP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (26, 'DEU', 'Germany', '+49', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (27, 'FRA', 'France', '+33', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (28, 'ITA', 'Italy', '+39', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (29, 'ESP', 'Spain', '+34', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (30, 'NLD', 'Netherlands', '+31', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (31, 'CHE', 'Switzerland', '+41', 'CHF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (32, 'SWE', 'Sweden', '+46', 'SEK', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (33, 'NOR', 'Norway', '+47', 'NOK', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (34, 'DNK', 'Denmark', '+45', 'DKK', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (35, 'FIN', 'Finland', '+358', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (36, 'POL', 'Poland', '+48', 'PLN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (37, 'AUT', 'Austria', '+43', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (38, 'BEL', 'Belgium', '+32', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (39, 'IRL', 'Ireland', '+353', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (40, 'PRT', 'Portugal', '+351', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (41, 'GRC', 'Greece', '+30', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (42, 'CZE', 'Czech Republic', '+420', 'CZK', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (43, 'HUN', 'Hungary', '+36', 'HUF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (44, 'ROU', 'Romania', '+40', 'RON', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (45, 'BGR', 'Bulgaria', '+359', 'BGN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (46, 'HRV', 'Croatia', '+385', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (47, 'SVN', 'Slovenia', '+386', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (48, 'SVK', 'Slovakia', '+421', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (49, 'LTU', 'Lithuania', '+370', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (50, 'LVA', 'Latvia', '+371', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (51, 'EST', 'Estonia', '+372', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (52, 'LUX', 'Luxembourg', '+352', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (53, 'MLT', 'Malta', '+356', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (54, 'CYP', 'Cyprus', '+357', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (55, 'ISL', 'Iceland', '+354', 'ISK', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (56, 'ALB', 'Albania', '+355', 'ALL', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (57, 'MKD', 'North Macedonia', '+389', 'MKD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (58, 'MNE', 'Montenegro', '+382', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (59, 'BIH', 'Bosnia and Herzegovina', '+387', 'BAM', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (60, 'SRB', 'Serbia', '+381', 'RSD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (61, 'KOS', 'Kosovo', '+383', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (62, 'MDA', 'Moldova', '+373', 'MDL', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (63, 'UKR', 'Ukraine', '+380', 'UAH', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (64, 'BLR', 'Belarus', '+375', 'BYN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (65, 'RUS', 'Russia', '+7', 'RUB', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (66, 'GEO', 'Georgia', '+995', 'GEL', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (67, 'ARM', 'Armenia', '+374', 'AMD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (68, 'AZE', 'Azerbaijan', '+994', 'AZN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (69, 'AND', 'Andorra', '+376', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (70, 'MCO', 'Monaco', '+377', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (71, 'LIE', 'Liechtenstein', '+423', 'CHF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (72, 'SMR', 'San Marino', '+378', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (73, 'VAT', 'Vatican City', '+379', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (74, 'CHN', 'China', '+86', 'CNY', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (75, 'JPN', 'Japan', '+81', 'JPY', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (76, 'IND', 'India', '+91', 'INR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (77, 'KOR', 'South Korea', '+82', 'KRW', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (78, 'SGP', 'Singapore', '+65', 'SGD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (79, 'MYS', 'Malaysia', '+60', 'MYR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (80, 'THA', 'Thailand', '+66', 'THB', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (81, 'IDN', 'Indonesia', '+62', 'IDR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (82, 'PHL', 'Philippines', '+63', 'PHP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (83, 'VNM', 'Vietnam', '+84', 'VND', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (84, 'TWN', 'Taiwan', '+886', 'TWD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (85, 'HKG', 'Hong Kong', '+852', 'HKD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (86, 'ISR', 'Israel', '+972', 'ILS', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (87, 'TUR', 'Turkey', '+90', 'TRY', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (88, 'SAU', 'Saudi Arabia', '+966', 'SAR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (89, 'ARE', 'United Arab Emirates', '+971', 'AED', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (90, 'QAT', 'Qatar', '+974', 'QAR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (91, 'KWT', 'Kuwait', '+965', 'KWD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (92, 'BHR', 'Bahrain', '+973', 'BHD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (93, 'OMN', 'Oman', '+968', 'OMR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (94, 'YEM', 'Yemen', '+967', 'YER', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (95, 'JOR', 'Jordan', '+962', 'JOD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (96, 'LBN', 'Lebanon', '+961', 'LBP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (97, 'SYR', 'Syria', '+963', 'SYP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (98, 'IRQ', 'Iraq', '+964', 'IQD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (99, 'IRN', 'Iran', '+98', 'IRR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (100, 'AFG', 'Afghanistan', '+93', 'AFN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (101, 'PAK', 'Pakistan', '+92', 'PKR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (102, 'BGD', 'Bangladesh', '+880', 'BDT', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (103, 'NPL', 'Nepal', '+977', 'NPR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (104, 'BTN', 'Bhutan', '+975', 'BTN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (105, 'MMR', 'Myanmar', '+95', 'MMK', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (106, 'LAO', 'Laos', '+856', 'LAK', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (107, 'KHM', 'Cambodia', '+855', 'KHR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (108, 'MNG', 'Mongolia', '+976', 'MNT', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (109, 'KAZ', 'Kazakhstan', '+7', 'KZT', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (110, 'UZB', 'Uzbekistan', '+998', 'UZS', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (111, 'TJK', 'Tajikistan', '+992', 'TJS', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (112, 'KGZ', 'Kyrgyzstan', '+996', 'KGS', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (113, 'TKM', 'Turkmenistan', '+993', 'TMT', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (114, 'MAC', 'Macau', '+853', 'MOP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (115, 'BRN', 'Brunei', '+673', 'BND', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (116, 'TLS', 'Timor-Leste', '+670', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (117, 'PNG', 'Papua New Guinea', '+675', 'PGK', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (118, 'FJI', 'Fiji', '+679', 'FJD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (119, 'SLB', 'Solomon Islands', '+677', 'SBD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (120, 'VUT', 'Vanuatu', '+678', 'VUV', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (121, 'NCL', 'New Caledonia', '+687', 'XPF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (122, 'WSM', 'Samoa', '+685', 'WST', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (123, 'TON', 'Tonga', '+676', 'TOP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (124, 'KIR', 'Kiribati', '+686', 'AUD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (125, 'TUV', 'Tuvalu', '+688', 'AUD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (126, 'NRU', 'Nauru', '+674', 'AUD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (127, 'PLW', 'Palau', '+680', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (128, 'FSM', 'Micronesia', '+691', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (129, 'MHL', 'Marshall Islands', '+692', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (130, 'COK', 'Cook Islands', '+682', 'NZD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (131, 'NIU', 'Niue', '+683', 'NZD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (132, 'TKL', 'Tokelau', '+690', 'NZD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (133, 'ASM', 'American Samoa', '+1684', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (134, 'GUM', 'Guam', '+1671', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (135, 'MNP', 'Northern Mariana Islands', '+1670', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (136, 'PYF', 'French Polynesia', '+689', 'XPF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (137, 'WLF', 'Wallis and Futuna', '+681', 'XPF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (138, 'PCN', 'Pitcairn Islands', '+64', 'NZD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (139, 'NFK', 'Norfolk Island', '+672', 'AUD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (140, 'CXR', 'Christmas Island', '+61', 'AUD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (141, 'CCK', 'Cocos Islands', '+61', 'AUD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (142, 'HMD', 'Heard and McDonald Islands', '+672', 'AUD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (143, 'AUS', 'Australia', '+61', 'AUD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (144, 'NZL', 'New Zealand', '+64', 'NZD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (145, 'BRA', 'Brazil', '+55', 'BRL', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (146, 'ARG', 'Argentina', '+54', 'ARS', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (147, 'CHL', 'Chile', '+56', 'CLP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (148, 'COL', 'Colombia', '+57', 'COP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (149, 'PER', 'Peru', '+51', 'PEN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (150, 'VEN', 'Venezuela', '+58', 'VES', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (151, 'ECU', 'Ecuador', '+593', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (152, 'BOL', 'Bolivia', '+591', 'BOB', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (153, 'PRY', 'Paraguay', '+595', 'PYG', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (154, 'URY', 'Uruguay', '+598', 'UYU', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (155, 'GUY', 'Guyana', '+592', 'GYD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (156, 'SUR', 'Suriname', '+597', 'SRD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (157, 'GUF', 'French Guiana', '+594', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (158, 'FLK', 'Falkland Islands', '+500', 'FKP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (159, 'ZAF', 'South Africa', '+27', 'ZAR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (160, 'EGY', 'Egypt', '+20', 'EGP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (161, 'NGA', 'Nigeria', '+234', 'NGN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (162, 'KEN', 'Kenya', '+254', 'KES', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (163, 'MAR', 'Morocco', '+212', 'MAD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (164, 'TUN', 'Tunisia', '+216', 'TND', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (165, 'GHA', 'Ghana', '+233', 'GHS', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (166, 'ETH', 'Ethiopia', '+251', 'ETB', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (167, 'DZA', 'Algeria', '+213', 'DZD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (168, 'SDN', 'Sudan', '+249', 'SDG', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (169, 'SSD', 'South Sudan', '+211', 'SSP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (170, 'LBY', 'Libya', '+218', 'LYD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (171, 'TCD', 'Chad', '+235', 'XAF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (172, 'NER', 'Niger', '+227', 'XOF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (173, 'MLI', 'Mali', '+223', 'XOF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (174, 'BFA', 'Burkina Faso', '+226', 'XOF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (175, 'CIV', 'Ivory Coast', '+225', 'XOF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (176, 'SEN', 'Senegal', '+221', 'XOF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (177, 'GMB', 'Gambia', '+220', 'GMD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (178, 'GNB', 'Guinea-Bissau', '+245', 'XOF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (179, 'GIN', 'Guinea', '+224', 'GNF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (180, 'SLE', 'Sierra Leone', '+232', 'SLL', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (181, 'LBR', 'Liberia', '+231', 'LRD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (182, 'CMR', 'Cameroon', '+237', 'XAF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (183, 'CAF', 'Central African Republic', '+236', 'XAF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (184, 'COG', 'Republic of the Congo', '+242', 'XAF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (185, 'COD', 'Democratic Republic of the Congo', '+243', 'CDF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (186, 'GAB', 'Gabon', '+241', 'XAF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (187, 'GNQ', 'Equatorial Guinea', '+240', 'XAF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (188, 'STP', 'São Tomé and Príncipe', '+239', 'STN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (189, 'AGO', 'Angola', '+244', 'AOA', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (190, 'ZMB', 'Zambia', '+260', 'ZMW', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (191, 'ZWE', 'Zimbabwe', '+263', 'ZWL', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (192, 'BWA', 'Botswana', '+267', 'BWP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (193, 'NAM', 'Namibia', '+264', 'NAD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (194, 'SWZ', 'Eswatini', '+268', 'SZL', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (195, 'LSO', 'Lesotho', '+266', 'LSL', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (196, 'MDG', 'Madagascar', '+261', 'MGA', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (197, 'MUS', 'Mauritius', '+230', 'MUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (198, 'SYC', 'Seychelles', '+248', 'SCR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (199, 'COM', 'Comoros', '+269', 'KMF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (200, 'DJI', 'Djibouti', '+253', 'DJF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (201, 'SOM', 'Somalia', '+252', 'SOS', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (202, 'ERI', 'Eritrea', '+291', 'ERN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (203, 'BDI', 'Burundi', '+257', 'BIF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (204, 'RWA', 'Rwanda', '+250', 'RWF', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (205, 'UGA', 'Uganda', '+256', 'UGX', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (206, 'TZA', 'Tanzania', '+255', 'TZS', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (207, 'MOZ', 'Mozambique', '+258', 'MZN', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (208, 'MWI', 'Malawi', '+265', 'MWK', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (209, 'CPV', 'Cape Verde', '+238', 'CVE', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (210, 'SHN', 'Saint Helena', '+290', 'SHP', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (211, 'IOT', 'British Indian Ocean Territory', '+246', 'USD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (212, 'MYT', 'Mayotte', '+262', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (213, 'REU', 'Réunion', '+262', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (214, 'ATF', 'French Southern Territories', '+262', 'EUR', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.country VALUES (215, 'ESH', 'Western Sahara', '+212', 'MAD', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');


--
-- TOC entry 3899 (class 0 OID 16460)
-- Dependencies: 224
-- Data for Name: department; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3897 (class 0 OID 16447)
-- Dependencies: 222
-- Data for Name: department_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3959 (class 0 OID 32967)
-- Dependencies: 285
-- Data for Name: document_type; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.document_type VALUES (1, '00000000-0000-0000-0000-000000000001', 'RESUME', 'Professional resume/CV', true, 10485760, 'pdf,doc,docx', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.document_type VALUES (3, '00000000-0000-0000-0000-000000000001', 'COVER_LETTER', 'Cover letter', false, 10485760, 'pdf,doc,docx', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.document_type VALUES (4, '00000000-0000-0000-0000-000000000001', 'PORTFOLIO', 'Work portfolio', false, 10485760, 'pdf,zip,rar', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.document_type VALUES (2, '00000000-0000-0000-0000-000000000001', 'PHOTO', 'Profile photo', true, 10485760, 'jpg,jpeg,png', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');


--
-- TOC entry 3965 (class 0 OID 33016)
-- Dependencies: 291
-- Data for Name: id_type; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.id_type VALUES (1, '00000000-0000-0000-0000-000000000001', 'PASSPORT', 'Passport', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (2, '00000000-0000-0000-0000-000000000001', 'NATIONAL_ID', 'National ID Card', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (3, '00000000-0000-0000-0000-000000000001', 'DRIVERS_LICENSE', 'Drivers License', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (4, '00000000-0000-0000-0000-000000000001', 'SSN', 'Social Security Number', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (5, '00000000-0000-0000-0000-000000000001', 'AADHAR', 'Aadhar Card (India)', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (6, '00000000-0000-0000-0000-000000000001', 'PAN', 'PAN Card (India)', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (7, '00000000-0000-0000-0000-000000000001', 'VOTER_ID', 'Voter ID Card', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (8, '00000000-0000-0000-0000-000000000001', 'BIRTH_CERTIFICATE', 'Birth Certificate', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (9, '00000000-0000-0000-0000-000000000001', 'WORK_PERMIT', 'Work Permit', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (10, '00000000-0000-0000-0000-000000000001', 'GREEN_CARD', 'Green Card (US)', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (11, '00000000-0000-0000-0000-000000000001', 'VISA', 'Visa', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (12, '00000000-0000-0000-0000-000000000001', 'EMPLOYEE_ID', 'Employee ID', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (13, '00000000-0000-0000-0000-000000000001', 'STUDENT_ID', 'Student ID', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.id_type VALUES (14, '00000000-0000-0000-0000-000000000001', 'MILITARY_ID', 'Military ID', false, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');


--
-- TOC entry 3949 (class 0 OID 17121)
-- Dependencies: 275
-- Data for Name: interview; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3951 (class 0 OID 17155)
-- Dependencies: 277
-- Data for Name: interview_feedback; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3918 (class 0 OID 16709)
-- Dependencies: 243
-- Data for Name: interview_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3937 (class 0 OID 16957)
-- Dependencies: 263
-- Data for Name: job_requisition; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3905 (class 0 OID 16534)
-- Dependencies: 230
-- Data for Name: job_requisition_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3945 (class 0 OID 17067)
-- Dependencies: 271
-- Data for Name: offer; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3914 (class 0 OID 16668)
-- Dependencies: 239
-- Data for Name: offer_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3947 (class 0 OID 17097)
-- Dependencies: 273
-- Data for Name: onboarding; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3916 (class 0 OID 16689)
-- Dependencies: 241
-- Data for Name: onboarding_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3895 (class 0 OID 16426)
-- Dependencies: 220
-- Data for Name: organization; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.organization VALUES ('08b06d14-4e03-11f0-bc56-325096b39f47', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', 'STL Digital', 'STL Digital, 8th Floor, Smartworks Building, Karle Town, Nagawara, Bangalore', 1, '2025-06-20 18:20:10.482479+00', '71e6ffd8-4e02-11f0-b8f3-325096b39f47', '2025-06-20 18:25:37.187026+00', '71e6ffd8-4e02-11f0-b8f3-325096b39f47');
INSERT INTO hcm.organization VALUES ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Default Organization', 'Default Address', 1, '2025-07-05 00:37:52.14509+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 00:37:52.14509+00', '00000000-0000-0000-0000-000000000001');


--
-- TOC entry 3894 (class 0 OID 16414)
-- Dependencies: 219
-- Data for Name: organization_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.organization_status VALUES (1, 'Active', 'Currently Functional', '2025-06-20 18:20:06.080097+00', '71e6ffd8-4e02-11f0-b8f3-325096b39f47', '2025-06-20 18:20:06.080097+00', '71e6ffd8-4e02-11f0-b8f3-325096b39f47');


--
-- TOC entry 3943 (class 0 OID 17053)
-- Dependencies: 269
-- Data for Name: pipeline_stage; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3903 (class 0 OID 16502)
-- Dependencies: 228
-- Data for Name: position; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3901 (class 0 OID 16489)
-- Dependencies: 226
-- Data for Name: position_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3934 (class 0 OID 16885)
-- Dependencies: 259
-- Data for Name: skill; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.skill VALUES (146, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Swift', 'Programming Language', 'Apple iOS/macOS language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (139, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Java', 'Programming Language', 'Object-oriented programming language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (140, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Python', 'Programming Language', 'High-level programming language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (141, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'JavaScript', 'Programming Language', 'Web scripting language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (142, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'TypeScript', 'Programming Language', 'Typed superset of JavaScript', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (143, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'C#', 'Programming Language', 'Microsoft .NET language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (144, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Go', 'Programming Language', 'Google systems language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (145, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Kotlin', 'Programming Language', 'Modern JVM language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (147, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'PHP', 'Programming Language', 'Web backend scripting language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (148, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Ruby', 'Programming Language', 'Dynamic scripting language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (149, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Rust', 'Programming Language', 'Systems programming language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (150, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Scala', 'Programming Language', 'Functional programming on JVM', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (151, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'C++', 'Programming Language', 'High-performance programming', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (152, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'C', 'Programming Language', 'Low-level programming language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (153, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'R', 'Programming Language', 'Statistical computing language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (154, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'MATLAB', 'Programming Language', 'Numerical computing environment', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (155, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Perl', 'Programming Language', 'Text processing language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (156, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Haskell', 'Programming Language', 'Pure functional programming', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (157, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Elixir', 'Programming Language', 'Functional programming for web', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (158, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Clojure', 'Programming Language', 'Lisp dialect for JVM', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (159, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Dart', 'Programming Language', 'Client-optimized language', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (160, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Spring Boot', 'Framework', 'Java microservices framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (161, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'React', 'Frontend Framework', 'JavaScript UI library', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (162, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Angular', 'Frontend Framework', 'TypeScript web framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (163, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Vue.js', 'Frontend Framework', 'Progressive JavaScript framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (164, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Django', 'Framework', 'Python web framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (165, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Flask', 'Framework', 'Lightweight Python web framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (166, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Express.js', 'Framework', 'Node.js web framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (167, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'ASP.NET Core', 'Framework', 'Microsoft web framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (168, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Laravel', 'Framework', 'PHP web framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (169, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'FastAPI', 'Framework', 'Modern Python API framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (170, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Spring Framework', 'Framework', 'Java enterprise framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (171, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Next.js', 'Frontend Framework', 'React full-stack framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (172, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Nuxt.js', 'Frontend Framework', 'Vue.js full-stack framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (173, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Svelte', 'Frontend Framework', 'Modern reactive framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (174, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Flutter', 'Mobile Framework', 'Google mobile app framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (175, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'React Native', 'Mobile Framework', 'React for mobile apps', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (176, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Xamarin', 'Mobile Framework', 'Microsoft mobile framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (177, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Ionic', 'Mobile Framework', 'Hybrid mobile framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (178, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Symfony', 'Framework', 'PHP enterprise framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (179, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'CodeIgniter', 'Framework', 'Lightweight PHP framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (180, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Ruby on Rails', 'Framework', 'Ruby web framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (181, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'PostgreSQL', 'Database', 'Open-source relational database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (182, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'MySQL', 'Database', 'Popular open-source database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (183, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'MongoDB', 'Database', 'NoSQL document database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (184, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Redis', 'Database', 'In-memory key-value store', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (185, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Oracle', 'Database', 'Enterprise RDBMS', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (186, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'SQL Server', 'Database', 'Microsoft relational database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (187, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'SQLite', 'Database', 'Lightweight embedded database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (188, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Cassandra', 'Database', 'Distributed NoSQL database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (189, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'DynamoDB', 'Database', 'AWS NoSQL database service', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (190, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Elasticsearch', 'Database', 'Search and analytics engine', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (191, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Neo4j', 'Database', 'Graph database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (192, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'InfluxDB', 'Database', 'Time series database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (193, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'CouchDB', 'Database', 'Document-oriented NoSQL database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (194, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'HBase', 'Database', 'Distributed column-oriented database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (195, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'RethinkDB', 'Database', 'Real-time database', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (196, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'AWS', 'Cloud Platform', 'Amazon Web Services', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (197, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Azure', 'Cloud Platform', 'Microsoft Azure', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (198, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Google Cloud', 'Cloud Platform', 'Google Cloud Platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (199, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'IBM Cloud', 'Cloud Platform', 'IBM Cloud Platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (200, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Oracle Cloud', 'Cloud Platform', 'Oracle Cloud Infrastructure', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (201, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'DigitalOcean', 'Cloud Platform', 'Developer-friendly cloud platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (202, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Heroku', 'Cloud Platform', 'Platform as a Service', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (203, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Vercel', 'Cloud Platform', 'Frontend deployment platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (204, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Netlify', 'Cloud Platform', 'Web hosting and deployment', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (205, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Firebase', 'Cloud Platform', 'Google mobile and web platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (206, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Docker', 'DevOps', 'Containerization platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (207, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Kubernetes', 'DevOps', 'Container orchestration', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (208, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Jenkins', 'DevOps', 'CI/CD automation server', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (209, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'GitLab CI', 'DevOps', 'GitLab continuous integration', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (210, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'GitHub Actions', 'DevOps', 'GitHub CI/CD platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (211, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'CircleCI', 'DevOps', 'Continuous integration platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (212, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Travis CI', 'DevOps', 'Hosted continuous integration', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (213, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Ansible', 'DevOps', 'Configuration management tool', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (214, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Terraform', 'DevOps', 'Infrastructure as code tool', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (215, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Chef', 'DevOps', 'Configuration management platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (216, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Puppet', 'DevOps', 'Configuration management tool', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (217, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Vagrant', 'DevOps', 'Development environment tool', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (218, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Prometheus', 'DevOps', 'Monitoring and alerting', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (219, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Grafana', 'DevOps', 'Data visualization platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (220, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'ELK Stack', 'DevOps', 'Elasticsearch, Logstash, Kibana', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (221, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Splunk', 'DevOps', 'Log management and analytics', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (222, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Istio', 'DevOps', 'Service mesh platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (223, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Helm', 'DevOps', 'Kubernetes package manager', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (224, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'ArgoCD', 'DevOps', 'GitOps continuous delivery', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (225, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Spinnaker', 'DevOps', 'Multi-cloud continuous delivery', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (226, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Kafka', 'Message Broker', 'Distributed streaming platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (227, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'RabbitMQ', 'Message Broker', 'Message queueing broker', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (228, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Apache ActiveMQ', 'Message Broker', 'Open source message broker', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (229, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Apache Pulsar', 'Message Broker', 'Cloud-native messaging platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (230, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'NATS', 'Message Broker', 'Cloud native messaging system', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (231, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Redis Pub/Sub', 'Message Broker', 'Redis publish/subscribe messaging', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (232, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'ZeroMQ', 'Message Broker', 'High-performance messaging library', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (233, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Amazon SQS', 'Message Broker', 'AWS managed message queuing', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (234, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Azure Service Bus', 'Message Broker', 'Microsoft cloud messaging service', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (235, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Google Cloud Pub/Sub', 'Message Broker', 'Google Cloud messaging service', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (236, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Communication', 'Soft Skill', 'Effective verbal and written communication', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (237, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Team Leadership', 'Soft Skill', 'Leading and motivating teams', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (238, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Problem Solving', 'Soft Skill', 'Analytical and creative problem solving', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (239, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Time Management', 'Soft Skill', 'Efficient planning and prioritization', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (240, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Mentoring', 'Soft Skill', 'Guiding and developing others', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (241, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Adaptability', 'Soft Skill', 'Flexibility and willingness to change', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (242, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Critical Thinking', 'Soft Skill', 'Logical analysis and evaluation', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (243, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Creativity', 'Soft Skill', 'Innovative thinking and idea generation', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (244, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Emotional Intelligence', 'Soft Skill', 'Understanding and managing emotions', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (245, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Conflict Resolution', 'Soft Skill', 'Managing and resolving disagreements', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (246, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Negotiation', 'Soft Skill', 'Reaching mutually beneficial agreements', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (247, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Presentation Skills', 'Soft Skill', 'Delivering effective presentations', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (248, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Active Listening', 'Soft Skill', 'Fully concentrating on what is being said', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (249, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Collaboration', 'Soft Skill', 'Working effectively with others', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (250, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Strategic Thinking', 'Soft Skill', 'Long-term planning and vision', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (251, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Customer Focus', 'Soft Skill', 'Understanding and meeting customer needs', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (252, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Decision Making', 'Soft Skill', 'Making sound and timely decisions', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (253, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Stress Management', 'Soft Skill', 'Handling pressure and stress effectively', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (254, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Cultural Awareness', 'Soft Skill', 'Understanding diverse cultures and perspectives', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (255, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Innovation', 'Soft Skill', 'Generating new ideas and approaches', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (256, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Machine Learning', 'AI/ML', 'Building and training ML models', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (257, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Deep Learning', 'AI/ML', 'Neural networks and deep learning', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (258, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Data Science', 'AI/ML', 'Data analysis and insights', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (259, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Natural Language Processing', 'AI/ML', 'Text and language processing', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (260, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Computer Vision', 'AI/ML', 'Image and video processing', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (261, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'TensorFlow', 'AI/ML', 'Google ML framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (262, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'PyTorch', 'AI/ML', 'Facebook ML framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (263, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Scikit-learn', 'AI/ML', 'Python ML library', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (264, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Pandas', 'Data Analysis', 'Python data manipulation', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (265, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'NumPy', 'Data Analysis', 'Numerical computing in Python', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (266, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Matplotlib', 'Data Analysis', 'Python plotting library', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (267, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Seaborn', 'Data Analysis', 'Statistical data visualization', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (268, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Tableau', 'Data Analysis', 'Data visualization platform', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (269, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Power BI', 'Data Analysis', 'Microsoft business analytics', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (270, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Jupyter', 'Data Analysis', 'Interactive computing environment', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (271, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Apache Spark', 'Big Data', 'Distributed computing framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (272, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Hadoop', 'Big Data', 'Distributed storage and processing', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (273, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Apache Flink', 'Big Data', 'Stream processing framework', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (274, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Apache Storm', 'Big Data', 'Real-time stream processing', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (275, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Apache Beam', 'Big Data', 'Unified programming model', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.skill VALUES (276, '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Apache Airflow', 'Big Data', 'Workflow orchestration', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001', '2025-07-06 11:02:03.933942+00', '00000000-0000-0000-0000-000000000001');


--
-- TOC entry 3963 (class 0 OID 32999)
-- Dependencies: 289
-- Data for Name: state; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.state VALUES (1, 1, 'CA', 'California', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (2, 1, 'TX', 'Texas', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (3, 1, 'FL', 'Florida', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (4, 1, 'NY', 'New York', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (5, 1, 'IL', 'Illinois', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (6, 1, 'PA', 'Pennsylvania', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (7, 1, 'OH', 'Ohio', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (8, 1, 'GA', 'Georgia', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (9, 1, 'NC', 'North Carolina', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (10, 1, 'MI', 'Michigan', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (11, 1, 'NJ', 'New Jersey', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (12, 1, 'VA', 'Virginia', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (13, 1, 'WA', 'Washington', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (14, 1, 'AZ', 'Arizona', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (15, 1, 'MA', 'Massachusetts', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (16, 1, 'TN', 'Tennessee', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (17, 1, 'IN', 'Indiana', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (18, 1, 'MO', 'Missouri', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (19, 1, 'MD', 'Maryland', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (20, 1, 'CO', 'Colorado', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (21, 1, 'WI', 'Wisconsin', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (22, 76, 'MH', 'Maharashtra', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (23, 76, 'UP', 'Uttar Pradesh', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (24, 76, 'WB', 'West Bengal', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (25, 76, 'BR', 'Bihar', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (26, 76, 'MP', 'Madhya Pradesh', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (27, 76, 'TN', 'Tamil Nadu', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (28, 76, 'KA', 'Karnataka', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (29, 76, 'GJ', 'Gujarat', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (30, 76, 'AP', 'Andhra Pradesh', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (31, 76, 'TG', 'Telangana', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (32, 76, 'DL', 'Delhi', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (33, 76, 'HR', 'Haryana', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (34, 76, 'RJ', 'Rajasthan', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (35, 76, 'KL', 'Kerala', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (36, 76, 'PB', 'Punjab', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (37, 2, 'ON', 'Ontario', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (38, 2, 'QC', 'Quebec', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (39, 2, 'BC', 'British Columbia', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (40, 2, 'AB', 'Alberta', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (41, 2, 'NS', 'Nova Scotia', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (42, 2, 'NB', 'New Brunswick', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (43, 2, 'MB', 'Manitoba', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (44, 2, 'SK', 'Saskatchewan', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (45, 2, 'PE', 'Prince Edward Island', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (46, 2, 'NL', 'Newfoundland and Labrador', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (47, 2, 'NT', 'Northwest Territories', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (48, 2, 'NU', 'Nunavut', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (49, 2, 'YT', 'Yukon', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (50, 143, 'NSW', 'New South Wales', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (51, 143, 'VIC', 'Victoria', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (52, 143, 'QLD', 'Queensland', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (53, 143, 'WA', 'Western Australia', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (54, 143, 'SA', 'South Australia', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (55, 143, 'TAS', 'Tasmania', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (56, 143, 'ACT', 'Australian Capital Territory', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (57, 143, 'NT', 'Northern Territory', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (58, 25, 'ENG', 'England', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (59, 25, 'SCT', 'Scotland', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (60, 25, 'WLS', 'Wales', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (61, 25, 'NIR', 'Northern Ireland', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (62, 26, 'BY', 'Bavaria', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (63, 26, 'NW', 'North Rhine-Westphalia', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (64, 26, 'BW', 'Baden-Württemberg', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (65, 26, 'HE', 'Hesse', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (66, 26, 'NI', 'Lower Saxony', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (67, 26, 'RP', 'Rhineland-Palatinate', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (68, 26, 'BE', 'Berlin', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (69, 26, 'HH', 'Hamburg', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (70, 26, 'SH', 'Schleswig-Holstein', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (71, 26, 'BB', 'Brandenburg', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (72, 26, 'MV', 'Mecklenburg-Vorpommern', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (73, 26, 'SN', 'Saxony', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (74, 26, 'ST', 'Saxony-Anhalt', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (75, 26, 'TH', 'Thuringia', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (76, 26, 'SL', 'Saarland', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');
INSERT INTO hcm.state VALUES (77, 26, 'HB', 'Bremen', true, '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 01:16:57.852886+00', '00000000-0000-0000-0000-000000000001');


--
-- TOC entry 3892 (class 0 OID 16403)
-- Dependencies: 217
-- Data for Name: tenant; Type: TABLE DATA; Schema: hcm; Owner: postgres
--

INSERT INTO hcm.tenant VALUES ('a15104c0-44b7-4512-b9b1-6122e7af7d41', 'STL DIgital ', 'www.stldigital.tech', '2025-06-20 18:09:55.096812+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41', '2025-06-20 18:09:55.096812+00', 'a15104c0-44b7-4512-b9b1-6122e7af7d41');
INSERT INTO hcm.tenant VALUES ('00000000-0000-0000-0000-000000000001', 'Default Tenant', 'default.local', '2025-07-05 00:37:52.14509+00', '00000000-0000-0000-0000-000000000001', '2025-07-05 00:37:52.14509+00', '00000000-0000-0000-0000-000000000001');


--
-- TOC entry 3925 (class 0 OID 16777)
-- Dependencies: 250
-- Data for Name: user; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3922 (class 0 OID 16753)
-- Dependencies: 247
-- Data for Name: user_role; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3924 (class 0 OID 16767)
-- Dependencies: 249
-- Data for Name: user_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3920 (class 0 OID 16740)
-- Dependencies: 245
-- Data for Name: user_type; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3912 (class 0 OID 16642)
-- Dependencies: 237
-- Data for Name: vendor; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 3911 (class 0 OID 16632)
-- Dependencies: 236
-- Data for Name: vendor_status; Type: TABLE DATA; Schema: hcm; Owner: postgres
--



--
-- TOC entry 4043 (class 0 OID 0)
-- Dependencies: 264
-- Name: application_application_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.application_application_id_seq', 1, false);


--
-- TOC entry 4044 (class 0 OID 0)
-- Dependencies: 231
-- Name: application_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.application_status_status_id_seq', 1, false);


--
-- TOC entry 4045 (class 0 OID 0)
-- Dependencies: 266
-- Name: approval_approval_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.approval_approval_id_seq', 1, false);


--
-- TOC entry 4046 (class 0 OID 0)
-- Dependencies: 233
-- Name: approval_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.approval_status_status_id_seq', 1, false);


--
-- TOC entry 4047 (class 0 OID 0)
-- Dependencies: 256
-- Name: candidate_certification_certification_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.candidate_certification_certification_id_seq', 1, false);


--
-- TOC entry 4048 (class 0 OID 0)
-- Dependencies: 278
-- Name: candidate_document_document_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.candidate_document_document_id_seq', 1, false);


--
-- TOC entry 4049 (class 0 OID 0)
-- Dependencies: 252
-- Name: candidate_education_education_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.candidate_education_education_id_seq', 1, true);


--
-- TOC entry 4050 (class 0 OID 0)
-- Dependencies: 280
-- Name: candidate_identity_identity_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.candidate_identity_identity_id_seq', 1, false);


--
-- TOC entry 4051 (class 0 OID 0)
-- Dependencies: 282
-- Name: candidate_reference_reference_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.candidate_reference_reference_id_seq', 1, false);


--
-- TOC entry 4052 (class 0 OID 0)
-- Dependencies: 254
-- Name: candidate_work_history_work_history_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.candidate_work_history_work_history_id_seq', 1, false);


--
-- TOC entry 4053 (class 0 OID 0)
-- Dependencies: 286
-- Name: country_country_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.country_country_id_seq', 215, true);


--
-- TOC entry 4054 (class 0 OID 0)
-- Dependencies: 223
-- Name: department_department_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.department_department_id_seq', 1, false);


--
-- TOC entry 4055 (class 0 OID 0)
-- Dependencies: 221
-- Name: department_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.department_status_status_id_seq', 1, false);


--
-- TOC entry 4056 (class 0 OID 0)
-- Dependencies: 284
-- Name: document_type_document_type_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.document_type_document_type_id_seq', 4, true);


--
-- TOC entry 4057 (class 0 OID 0)
-- Dependencies: 290
-- Name: id_type_id_type_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.id_type_id_type_id_seq', 14, true);


--
-- TOC entry 4058 (class 0 OID 0)
-- Dependencies: 276
-- Name: interview_feedback_feedback_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.interview_feedback_feedback_id_seq', 1, false);


--
-- TOC entry 4059 (class 0 OID 0)
-- Dependencies: 274
-- Name: interview_interview_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.interview_interview_id_seq', 1, false);


--
-- TOC entry 4060 (class 0 OID 0)
-- Dependencies: 242
-- Name: interview_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.interview_status_status_id_seq', 1, false);


--
-- TOC entry 4061 (class 0 OID 0)
-- Dependencies: 262
-- Name: job_requisition_requisition_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.job_requisition_requisition_id_seq', 1, false);


--
-- TOC entry 4062 (class 0 OID 0)
-- Dependencies: 229
-- Name: job_requisition_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.job_requisition_status_status_id_seq', 1, false);


--
-- TOC entry 4063 (class 0 OID 0)
-- Dependencies: 270
-- Name: offer_offer_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.offer_offer_id_seq', 1, false);


--
-- TOC entry 4064 (class 0 OID 0)
-- Dependencies: 238
-- Name: offer_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.offer_status_status_id_seq', 1, false);


--
-- TOC entry 4065 (class 0 OID 0)
-- Dependencies: 272
-- Name: onboarding_onboarding_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.onboarding_onboarding_id_seq', 1, false);


--
-- TOC entry 4066 (class 0 OID 0)
-- Dependencies: 240
-- Name: onboarding_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.onboarding_status_status_id_seq', 1, false);


--
-- TOC entry 4067 (class 0 OID 0)
-- Dependencies: 218
-- Name: organization_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.organization_status_status_id_seq', 1, false);


--
-- TOC entry 4068 (class 0 OID 0)
-- Dependencies: 268
-- Name: pipeline_stage_stage_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.pipeline_stage_stage_id_seq', 1, false);


--
-- TOC entry 4069 (class 0 OID 0)
-- Dependencies: 227
-- Name: position_position_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.position_position_id_seq', 1, false);


--
-- TOC entry 4070 (class 0 OID 0)
-- Dependencies: 225
-- Name: position_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.position_status_status_id_seq', 1, false);


--
-- TOC entry 4071 (class 0 OID 0)
-- Dependencies: 258
-- Name: skill_skill_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.skill_skill_id_seq', 276, true);


--
-- TOC entry 4072 (class 0 OID 0)
-- Dependencies: 288
-- Name: state_state_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.state_state_id_seq', 77, true);


--
-- TOC entry 4073 (class 0 OID 0)
-- Dependencies: 246
-- Name: user_role_role_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.user_role_role_id_seq', 1, false);


--
-- TOC entry 4074 (class 0 OID 0)
-- Dependencies: 248
-- Name: user_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.user_status_status_id_seq', 1, false);


--
-- TOC entry 4075 (class 0 OID 0)
-- Dependencies: 244
-- Name: user_type_type_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.user_type_type_id_seq', 1, false);


--
-- TOC entry 4076 (class 0 OID 0)
-- Dependencies: 235
-- Name: vendor_status_status_id_seq; Type: SEQUENCE SET; Schema: hcm; Owner: postgres
--

SELECT pg_catalog.setval('hcm.vendor_status_status_id_seq', 1, false);


--
-- TOC entry 3618 (class 2606 OID 17009)
-- Name: application application_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.application
    ADD CONSTRAINT application_pkey PRIMARY KEY (application_id);


--
-- TOC entry 3551 (class 2606 OID 16588)
-- Name: application_status application_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.application_status
    ADD CONSTRAINT application_status_name_key UNIQUE (name);


--
-- TOC entry 3553 (class 2606 OID 16586)
-- Name: application_status application_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.application_status
    ADD CONSTRAINT application_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3620 (class 2606 OID 17036)
-- Name: approval approval_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.approval
    ADD CONSTRAINT approval_pkey PRIMARY KEY (approval_id);


--
-- TOC entry 3555 (class 2606 OID 16609)
-- Name: approval_status approval_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.approval_status
    ADD CONSTRAINT approval_status_name_key UNIQUE (name);


--
-- TOC entry 3557 (class 2606 OID 16607)
-- Name: approval_status approval_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.approval_status
    ADD CONSTRAINT approval_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3608 (class 2606 OID 16878)
-- Name: candidate_certification candidate_certification_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_certification
    ADD CONSTRAINT candidate_certification_pkey PRIMARY KEY (certification_id);


--
-- TOC entry 3633 (class 2606 OID 32777)
-- Name: candidate_document candidate_document_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_document
    ADD CONSTRAINT candidate_document_pkey PRIMARY KEY (document_id);


--
-- TOC entry 3604 (class 2606 OID 16848)
-- Name: candidate_education candidate_education_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_education
    ADD CONSTRAINT candidate_education_pkey PRIMARY KEY (education_id);


--
-- TOC entry 3597 (class 2606 OID 16827)
-- Name: candidate candidate_email_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate
    ADD CONSTRAINT candidate_email_key UNIQUE (email);


--
-- TOC entry 3637 (class 2606 OID 32784)
-- Name: candidate_identity candidate_identity_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_identity
    ADD CONSTRAINT candidate_identity_pkey PRIMARY KEY (identity_id);


--
-- TOC entry 3599 (class 2606 OID 16825)
-- Name: candidate candidate_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate
    ADD CONSTRAINT candidate_pkey PRIMARY KEY (candidate_id);


--
-- TOC entry 3641 (class 2606 OID 32793)
-- Name: candidate_reference candidate_reference_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_reference
    ADD CONSTRAINT candidate_reference_pkey PRIMARY KEY (reference_id);


--
-- TOC entry 3612 (class 2606 OID 16911)
-- Name: candidate_skill candidate_skill_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_skill
    ADD CONSTRAINT candidate_skill_pkey PRIMARY KEY (candidate_id, skill_id);


--
-- TOC entry 3606 (class 2606 OID 16864)
-- Name: candidate_work_history candidate_work_history_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_work_history
    ADD CONSTRAINT candidate_work_history_pkey PRIMARY KEY (work_history_id);


--
-- TOC entry 3648 (class 2606 OID 32997)
-- Name: country country_country_code_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.country
    ADD CONSTRAINT country_country_code_key UNIQUE (country_code);


--
-- TOC entry 3650 (class 2606 OID 32995)
-- Name: country country_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (country_id);


--
-- TOC entry 3539 (class 2606 OID 16467)
-- Name: department department_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.department
    ADD CONSTRAINT department_pkey PRIMARY KEY (department_id);


--
-- TOC entry 3535 (class 2606 OID 16458)
-- Name: department_status department_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.department_status
    ADD CONSTRAINT department_status_name_key UNIQUE (name);


--
-- TOC entry 3537 (class 2606 OID 16456)
-- Name: department_status department_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.department_status
    ADD CONSTRAINT department_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3644 (class 2606 OID 32978)
-- Name: document_type document_type_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.document_type
    ADD CONSTRAINT document_type_pkey PRIMARY KEY (document_type_id);


--
-- TOC entry 3646 (class 2606 OID 32980)
-- Name: document_type document_type_tenant_id_type_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.document_type
    ADD CONSTRAINT document_type_tenant_id_type_name_key UNIQUE (tenant_id, type_name);


--
-- TOC entry 3656 (class 2606 OID 33026)
-- Name: id_type id_type_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.id_type
    ADD CONSTRAINT id_type_pkey PRIMARY KEY (id_type_id);


--
-- TOC entry 3658 (class 2606 OID 33028)
-- Name: id_type id_type_tenant_id_type_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.id_type
    ADD CONSTRAINT id_type_tenant_id_type_name_key UNIQUE (tenant_id, type_name);


--
-- TOC entry 3631 (class 2606 OID 17164)
-- Name: interview_feedback interview_feedback_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview_feedback
    ADD CONSTRAINT interview_feedback_pkey PRIMARY KEY (feedback_id);


--
-- TOC entry 3629 (class 2606 OID 17128)
-- Name: interview interview_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview
    ADD CONSTRAINT interview_pkey PRIMARY KEY (interview_id);


--
-- TOC entry 3573 (class 2606 OID 16718)
-- Name: interview_status interview_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview_status
    ADD CONSTRAINT interview_status_name_key UNIQUE (name);


--
-- TOC entry 3575 (class 2606 OID 16716)
-- Name: interview_status interview_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview_status
    ADD CONSTRAINT interview_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3614 (class 2606 OID 16964)
-- Name: job_requisition job_requisition_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition
    ADD CONSTRAINT job_requisition_pkey PRIMARY KEY (requisition_id);


--
-- TOC entry 3547 (class 2606 OID 16543)
-- Name: job_requisition_status job_requisition_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition_status
    ADD CONSTRAINT job_requisition_status_name_key UNIQUE (name);


--
-- TOC entry 3549 (class 2606 OID 16541)
-- Name: job_requisition_status job_requisition_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition_status
    ADD CONSTRAINT job_requisition_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3624 (class 2606 OID 17075)
-- Name: offer offer_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.offer
    ADD CONSTRAINT offer_pkey PRIMARY KEY (offer_id);


--
-- TOC entry 3565 (class 2606 OID 16677)
-- Name: offer_status offer_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.offer_status
    ADD CONSTRAINT offer_status_name_key UNIQUE (name);


--
-- TOC entry 3567 (class 2606 OID 16675)
-- Name: offer_status offer_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.offer_status
    ADD CONSTRAINT offer_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3627 (class 2606 OID 17104)
-- Name: onboarding onboarding_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.onboarding
    ADD CONSTRAINT onboarding_pkey PRIMARY KEY (onboarding_id);


--
-- TOC entry 3569 (class 2606 OID 16698)
-- Name: onboarding_status onboarding_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.onboarding_status
    ADD CONSTRAINT onboarding_status_name_key UNIQUE (name);


--
-- TOC entry 3571 (class 2606 OID 16696)
-- Name: onboarding_status onboarding_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.onboarding_status
    ADD CONSTRAINT onboarding_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3533 (class 2606 OID 16435)
-- Name: organization organization_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.organization
    ADD CONSTRAINT organization_pkey PRIMARY KEY (organization_id);


--
-- TOC entry 3529 (class 2606 OID 16425)
-- Name: organization_status organization_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.organization_status
    ADD CONSTRAINT organization_status_name_key UNIQUE (name);


--
-- TOC entry 3531 (class 2606 OID 16423)
-- Name: organization_status organization_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.organization_status
    ADD CONSTRAINT organization_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3622 (class 2606 OID 17060)
-- Name: pipeline_stage pipeline_stage_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.pipeline_stage
    ADD CONSTRAINT pipeline_stage_pkey PRIMARY KEY (stage_id);


--
-- TOC entry 3545 (class 2606 OID 16512)
-- Name: position position_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."position"
    ADD CONSTRAINT position_pkey PRIMARY KEY (position_id);


--
-- TOC entry 3541 (class 2606 OID 16500)
-- Name: position_status position_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.position_status
    ADD CONSTRAINT position_status_name_key UNIQUE (name);


--
-- TOC entry 3543 (class 2606 OID 16498)
-- Name: position_status position_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.position_status
    ADD CONSTRAINT position_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3610 (class 2606 OID 16894)
-- Name: skill skill_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.skill
    ADD CONSTRAINT skill_pkey PRIMARY KEY (skill_id);


--
-- TOC entry 3652 (class 2606 OID 33009)
-- Name: state state_country_id_state_code_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.state
    ADD CONSTRAINT state_country_id_state_code_key UNIQUE (country_id, state_code);


--
-- TOC entry 3654 (class 2606 OID 33007)
-- Name: state state_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.state
    ADD CONSTRAINT state_pkey PRIMARY KEY (state_id);


--
-- TOC entry 3525 (class 2606 OID 16412)
-- Name: tenant tenant_domain_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.tenant
    ADD CONSTRAINT tenant_domain_key UNIQUE (domain);


--
-- TOC entry 3527 (class 2606 OID 16410)
-- Name: tenant tenant_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.tenant
    ADD CONSTRAINT tenant_pkey PRIMARY KEY (tenant_id);


--
-- TOC entry 3589 (class 2606 OID 16790)
-- Name: user user_email_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."user"
    ADD CONSTRAINT user_email_key UNIQUE (email);


--
-- TOC entry 3591 (class 2606 OID 16786)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3581 (class 2606 OID 16765)
-- Name: user_role user_role_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.user_role
    ADD CONSTRAINT user_role_name_key UNIQUE (name);


--
-- TOC entry 3583 (class 2606 OID 16763)
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (role_id);


--
-- TOC entry 3585 (class 2606 OID 16776)
-- Name: user_status user_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.user_status
    ADD CONSTRAINT user_status_name_key UNIQUE (name);


--
-- TOC entry 3587 (class 2606 OID 16774)
-- Name: user_status user_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.user_status
    ADD CONSTRAINT user_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3577 (class 2606 OID 16751)
-- Name: user_type user_type_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.user_type
    ADD CONSTRAINT user_type_name_key UNIQUE (name);


--
-- TOC entry 3579 (class 2606 OID 16749)
-- Name: user_type user_type_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.user_type
    ADD CONSTRAINT user_type_pkey PRIMARY KEY (type_id);


--
-- TOC entry 3593 (class 2606 OID 16788)
-- Name: user user_username_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."user"
    ADD CONSTRAINT user_username_key UNIQUE (username);


--
-- TOC entry 3563 (class 2606 OID 16651)
-- Name: vendor vendor_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.vendor
    ADD CONSTRAINT vendor_pkey PRIMARY KEY (vendor_id);


--
-- TOC entry 3559 (class 2606 OID 16641)
-- Name: vendor_status vendor_status_name_key; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.vendor_status
    ADD CONSTRAINT vendor_status_name_key UNIQUE (name);


--
-- TOC entry 3561 (class 2606 OID 16639)
-- Name: vendor_status vendor_status_pkey; Type: CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.vendor_status
    ADD CONSTRAINT vendor_status_pkey PRIMARY KEY (status_id);


--
-- TOC entry 3594 (class 1259 OID 16922)
-- Name: candidate_email_idx; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX candidate_email_idx ON hcm.candidate USING btree (email);


--
-- TOC entry 3595 (class 1259 OID 17182)
-- Name: candidate_email_idx1; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX candidate_email_idx1 ON hcm.candidate USING btree (email);


--
-- TOC entry 3634 (class 1259 OID 32844)
-- Name: idx_candidate_document_candidate; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX idx_candidate_document_candidate ON hcm.candidate_document USING btree (candidate_id);


--
-- TOC entry 3635 (class 1259 OID 32845)
-- Name: idx_candidate_document_type; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX idx_candidate_document_type ON hcm.candidate_document USING btree (document_type_id);


--
-- TOC entry 3638 (class 1259 OID 32846)
-- Name: idx_candidate_identity_candidate; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX idx_candidate_identity_candidate ON hcm.candidate_identity USING btree (candidate_id);


--
-- TOC entry 3639 (class 1259 OID 32847)
-- Name: idx_candidate_identity_type; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX idx_candidate_identity_type ON hcm.candidate_identity USING btree (id_type_id);


--
-- TOC entry 3642 (class 1259 OID 32848)
-- Name: idx_candidate_reference_candidate; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX idx_candidate_reference_candidate ON hcm.candidate_reference USING btree (candidate_id);


--
-- TOC entry 3600 (class 1259 OID 32842)
-- Name: idx_candidate_source; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX idx_candidate_source ON hcm.candidate USING btree (source);


--
-- TOC entry 3601 (class 1259 OID 32841)
-- Name: idx_candidate_status; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX idx_candidate_status ON hcm.candidate USING btree (status);


--
-- TOC entry 3602 (class 1259 OID 32843)
-- Name: idx_candidate_vendor; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX idx_candidate_vendor ON hcm.candidate USING btree (vendor_id);


--
-- TOC entry 3615 (class 1259 OID 17181)
-- Name: job_requisition_status_id_idx; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX job_requisition_status_id_idx ON hcm.job_requisition USING btree (status_id);


--
-- TOC entry 3616 (class 1259 OID 17180)
-- Name: job_requisition_tenant_id_idx; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX job_requisition_tenant_id_idx ON hcm.job_requisition USING btree (tenant_id);


--
-- TOC entry 3625 (class 1259 OID 17183)
-- Name: offer_status_id_idx; Type: INDEX; Schema: hcm; Owner: postgres
--

CREATE INDEX offer_status_id_idx ON hcm.offer USING btree (status_id);


--
-- TOC entry 3728 (class 2620 OID 16936)
-- Name: application_status trg_application_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_application_status_updated_at BEFORE UPDATE ON hcm.application_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3729 (class 2620 OID 16937)
-- Name: approval_status trg_approval_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_approval_status_updated_at BEFORE UPDATE ON hcm.approval_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3743 (class 2620 OID 16947)
-- Name: candidate_certification trg_candidate_certification_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_candidate_certification_updated_at BEFORE UPDATE ON hcm.candidate_certification FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3741 (class 2620 OID 16949)
-- Name: candidate_education trg_candidate_education_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_candidate_education_updated_at BEFORE UPDATE ON hcm.candidate_education FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3745 (class 2620 OID 16952)
-- Name: candidate_skill trg_candidate_skill_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_candidate_skill_updated_at BEFORE UPDATE ON hcm.candidate_skill FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3739 (class 2620 OID 16954)
-- Name: candidate trg_candidate_soft_delete; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_candidate_soft_delete BEFORE DELETE ON hcm.candidate FOR EACH ROW EXECUTE FUNCTION hcm.fn_soft_delete_candidate();


--
-- TOC entry 3740 (class 2620 OID 16948)
-- Name: candidate trg_candidate_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_candidate_updated_at BEFORE UPDATE ON hcm.candidate FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3742 (class 2620 OID 16950)
-- Name: candidate_work_history trg_candidate_work_history_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_candidate_work_history_updated_at BEFORE UPDATE ON hcm.candidate_work_history FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3723 (class 2620 OID 16932)
-- Name: department_status trg_department_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_department_status_updated_at BEFORE UPDATE ON hcm.department_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3724 (class 2620 OID 16931)
-- Name: department trg_department_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_department_updated_at BEFORE UPDATE ON hcm.department FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3734 (class 2620 OID 16942)
-- Name: interview_status trg_interview_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_interview_status_updated_at BEFORE UPDATE ON hcm.interview_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3727 (class 2620 OID 16935)
-- Name: job_requisition_status trg_job_requisition_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_job_requisition_status_updated_at BEFORE UPDATE ON hcm.job_requisition_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3732 (class 2620 OID 16940)
-- Name: offer_status trg_offer_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_offer_status_updated_at BEFORE UPDATE ON hcm.offer_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3733 (class 2620 OID 16941)
-- Name: onboarding_status trg_onboarding_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_onboarding_status_updated_at BEFORE UPDATE ON hcm.onboarding_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3721 (class 2620 OID 16930)
-- Name: organization_status trg_organization_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_organization_status_updated_at BEFORE UPDATE ON hcm.organization_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3722 (class 2620 OID 16929)
-- Name: organization trg_organization_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_organization_updated_at BEFORE UPDATE ON hcm.organization FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3725 (class 2620 OID 16934)
-- Name: position_status trg_position_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_position_status_updated_at BEFORE UPDATE ON hcm.position_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3726 (class 2620 OID 16933)
-- Name: position trg_position_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_position_updated_at BEFORE UPDATE ON hcm."position" FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3744 (class 2620 OID 16951)
-- Name: skill trg_skill_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_skill_updated_at BEFORE UPDATE ON hcm.skill FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3720 (class 2620 OID 16928)
-- Name: tenant trg_tenant_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_tenant_updated_at BEFORE UPDATE ON hcm.tenant FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3736 (class 2620 OID 16944)
-- Name: user_role trg_user_role_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_user_role_updated_at BEFORE UPDATE ON hcm.user_role FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3737 (class 2620 OID 16946)
-- Name: user_status trg_user_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_user_status_updated_at BEFORE UPDATE ON hcm.user_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3735 (class 2620 OID 16945)
-- Name: user_type trg_user_type_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_user_type_updated_at BEFORE UPDATE ON hcm.user_type FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3738 (class 2620 OID 16943)
-- Name: user trg_user_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_user_updated_at BEFORE UPDATE ON hcm."user" FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3730 (class 2620 OID 16939)
-- Name: vendor_status trg_vendor_status_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_vendor_status_updated_at BEFORE UPDATE ON hcm.vendor_status FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3731 (class 2620 OID 16938)
-- Name: vendor trg_vendor_updated_at; Type: TRIGGER; Schema: hcm; Owner: postgres
--

CREATE TRIGGER trg_vendor_updated_at BEFORE UPDATE ON hcm.vendor FOR EACH ROW EXECUTE FUNCTION hcm.fn_set_updated_at();


--
-- TOC entry 3693 (class 2606 OID 17010)
-- Name: application application_candidate_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.application
    ADD CONSTRAINT application_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES hcm.candidate(candidate_id);


--
-- TOC entry 3694 (class 2606 OID 17015)
-- Name: application application_requisition_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.application
    ADD CONSTRAINT application_requisition_id_fkey FOREIGN KEY (requisition_id) REFERENCES hcm.job_requisition(requisition_id);


--
-- TOC entry 3695 (class 2606 OID 17020)
-- Name: application application_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.application
    ADD CONSTRAINT application_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.application_status(status_id);


--
-- TOC entry 3696 (class 2606 OID 17042)
-- Name: approval approval_approver_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.approval
    ADD CONSTRAINT approval_approver_id_fkey FOREIGN KEY (approver_id) REFERENCES hcm."user"(user_id);


--
-- TOC entry 3697 (class 2606 OID 17037)
-- Name: approval approval_requisition_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.approval
    ADD CONSTRAINT approval_requisition_id_fkey FOREIGN KEY (requisition_id) REFERENCES hcm.job_requisition(requisition_id);


--
-- TOC entry 3698 (class 2606 OID 17047)
-- Name: approval approval_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.approval
    ADD CONSTRAINT approval_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.approval_status(status_id);


--
-- TOC entry 3681 (class 2606 OID 16879)
-- Name: candidate_certification candidate_certification_candidate_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_certification
    ADD CONSTRAINT candidate_certification_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES hcm.candidate(candidate_id);


--
-- TOC entry 3679 (class 2606 OID 16849)
-- Name: candidate_education candidate_education_candidate_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_education
    ADD CONSTRAINT candidate_education_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES hcm.candidate(candidate_id);


--
-- TOC entry 3677 (class 2606 OID 16833)
-- Name: candidate candidate_organization_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate
    ADD CONSTRAINT candidate_organization_id_fkey FOREIGN KEY (organization_id) REFERENCES hcm.organization(organization_id);


--
-- TOC entry 3684 (class 2606 OID 16912)
-- Name: candidate_skill candidate_skill_candidate_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_skill
    ADD CONSTRAINT candidate_skill_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES hcm.candidate(candidate_id);


--
-- TOC entry 3685 (class 2606 OID 16917)
-- Name: candidate_skill candidate_skill_skill_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_skill
    ADD CONSTRAINT candidate_skill_skill_id_fkey FOREIGN KEY (skill_id) REFERENCES hcm.skill(skill_id);


--
-- TOC entry 3678 (class 2606 OID 16828)
-- Name: candidate candidate_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate
    ADD CONSTRAINT candidate_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3680 (class 2606 OID 16865)
-- Name: candidate_work_history candidate_work_history_candidate_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_work_history
    ADD CONSTRAINT candidate_work_history_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES hcm.candidate(candidate_id);


--
-- TOC entry 3661 (class 2606 OID 16473)
-- Name: department department_organization_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.department
    ADD CONSTRAINT department_organization_id_fkey FOREIGN KEY (organization_id) REFERENCES hcm.organization(organization_id);


--
-- TOC entry 3662 (class 2606 OID 16478)
-- Name: department department_parent_department_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.department
    ADD CONSTRAINT department_parent_department_id_fkey FOREIGN KEY (parent_department_id) REFERENCES hcm.department(department_id);


--
-- TOC entry 3663 (class 2606 OID 16483)
-- Name: department department_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.department
    ADD CONSTRAINT department_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.department_status(status_id);


--
-- TOC entry 3664 (class 2606 OID 16468)
-- Name: department department_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.department
    ADD CONSTRAINT department_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3717 (class 2606 OID 32981)
-- Name: document_type document_type_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.document_type
    ADD CONSTRAINT document_type_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3715 (class 2606 OID 33034)
-- Name: candidate_document fkmtut80bi841iwvsfm0ce3s2cu; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_document
    ADD CONSTRAINT fkmtut80bi841iwvsfm0ce3s2cu FOREIGN KEY (document_type_id) REFERENCES hcm.document_type(document_type_id);


--
-- TOC entry 3716 (class 2606 OID 33039)
-- Name: candidate_identity fkp1pdopvg4p3ikp4ftxlqwgkvh; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.candidate_identity
    ADD CONSTRAINT fkp1pdopvg4p3ikp4ftxlqwgkvh FOREIGN KEY (id_type_id) REFERENCES hcm.id_type(id_type_id);


--
-- TOC entry 3719 (class 2606 OID 33029)
-- Name: id_type id_type_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.id_type
    ADD CONSTRAINT id_type_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3707 (class 2606 OID 17129)
-- Name: interview interview_application_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview
    ADD CONSTRAINT interview_application_id_fkey FOREIGN KEY (application_id) REFERENCES hcm.application(application_id);


--
-- TOC entry 3708 (class 2606 OID 17134)
-- Name: interview interview_candidate_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview
    ADD CONSTRAINT interview_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES hcm.candidate(candidate_id);


--
-- TOC entry 3712 (class 2606 OID 17175)
-- Name: interview_feedback interview_feedback_candidate_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview_feedback
    ADD CONSTRAINT interview_feedback_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES hcm.candidate(candidate_id);


--
-- TOC entry 3713 (class 2606 OID 17165)
-- Name: interview_feedback interview_feedback_interview_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview_feedback
    ADD CONSTRAINT interview_feedback_interview_id_fkey FOREIGN KEY (interview_id) REFERENCES hcm.interview(interview_id);


--
-- TOC entry 3714 (class 2606 OID 17170)
-- Name: interview_feedback interview_feedback_interviewer_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview_feedback
    ADD CONSTRAINT interview_feedback_interviewer_id_fkey FOREIGN KEY (interviewer_id) REFERENCES hcm."user"(user_id);


--
-- TOC entry 3709 (class 2606 OID 17144)
-- Name: interview interview_interviewer_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview
    ADD CONSTRAINT interview_interviewer_id_fkey FOREIGN KEY (interviewer_id) REFERENCES hcm."user"(user_id);


--
-- TOC entry 3710 (class 2606 OID 17139)
-- Name: interview interview_requisition_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview
    ADD CONSTRAINT interview_requisition_id_fkey FOREIGN KEY (requisition_id) REFERENCES hcm.job_requisition(requisition_id);


--
-- TOC entry 3711 (class 2606 OID 17149)
-- Name: interview interview_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.interview
    ADD CONSTRAINT interview_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.interview_status(status_id);


--
-- TOC entry 3686 (class 2606 OID 16980)
-- Name: job_requisition job_requisition_department_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition
    ADD CONSTRAINT job_requisition_department_id_fkey FOREIGN KEY (department_id) REFERENCES hcm.department(department_id);


--
-- TOC entry 3687 (class 2606 OID 16990)
-- Name: job_requisition job_requisition_hiring_manager_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition
    ADD CONSTRAINT job_requisition_hiring_manager_id_fkey FOREIGN KEY (hiring_manager_id) REFERENCES hcm."user"(user_id);


--
-- TOC entry 3688 (class 2606 OID 16970)
-- Name: job_requisition job_requisition_organization_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition
    ADD CONSTRAINT job_requisition_organization_id_fkey FOREIGN KEY (organization_id) REFERENCES hcm.organization(organization_id);


--
-- TOC entry 3689 (class 2606 OID 16975)
-- Name: job_requisition job_requisition_position_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition
    ADD CONSTRAINT job_requisition_position_id_fkey FOREIGN KEY (position_id) REFERENCES hcm."position"(position_id);


--
-- TOC entry 3690 (class 2606 OID 16985)
-- Name: job_requisition job_requisition_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition
    ADD CONSTRAINT job_requisition_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.job_requisition_status(status_id);


--
-- TOC entry 3691 (class 2606 OID 16965)
-- Name: job_requisition job_requisition_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition
    ADD CONSTRAINT job_requisition_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3692 (class 2606 OID 16995)
-- Name: job_requisition job_requisition_vendor_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.job_requisition
    ADD CONSTRAINT job_requisition_vendor_id_fkey FOREIGN KEY (vendor_id) REFERENCES hcm.vendor(vendor_id);


--
-- TOC entry 3700 (class 2606 OID 17076)
-- Name: offer offer_application_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.offer
    ADD CONSTRAINT offer_application_id_fkey FOREIGN KEY (application_id) REFERENCES hcm.application(application_id);


--
-- TOC entry 3701 (class 2606 OID 17081)
-- Name: offer offer_candidate_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.offer
    ADD CONSTRAINT offer_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES hcm.candidate(candidate_id);


--
-- TOC entry 3702 (class 2606 OID 17086)
-- Name: offer offer_requisition_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.offer
    ADD CONSTRAINT offer_requisition_id_fkey FOREIGN KEY (requisition_id) REFERENCES hcm.job_requisition(requisition_id);


--
-- TOC entry 3703 (class 2606 OID 17091)
-- Name: offer offer_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.offer
    ADD CONSTRAINT offer_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.offer_status(status_id);


--
-- TOC entry 3704 (class 2606 OID 17110)
-- Name: onboarding onboarding_candidate_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.onboarding
    ADD CONSTRAINT onboarding_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES hcm.candidate(candidate_id);


--
-- TOC entry 3705 (class 2606 OID 17105)
-- Name: onboarding onboarding_offer_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.onboarding
    ADD CONSTRAINT onboarding_offer_id_fkey FOREIGN KEY (offer_id) REFERENCES hcm.offer(offer_id);


--
-- TOC entry 3706 (class 2606 OID 17115)
-- Name: onboarding onboarding_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.onboarding
    ADD CONSTRAINT onboarding_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.onboarding_status(status_id);


--
-- TOC entry 3659 (class 2606 OID 16441)
-- Name: organization organization_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.organization
    ADD CONSTRAINT organization_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.organization_status(status_id);


--
-- TOC entry 3660 (class 2606 OID 16436)
-- Name: organization organization_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.organization
    ADD CONSTRAINT organization_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3699 (class 2606 OID 17061)
-- Name: pipeline_stage pipeline_stage_requisition_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.pipeline_stage
    ADD CONSTRAINT pipeline_stage_requisition_id_fkey FOREIGN KEY (requisition_id) REFERENCES hcm.job_requisition(requisition_id);


--
-- TOC entry 3665 (class 2606 OID 16523)
-- Name: position position_department_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."position"
    ADD CONSTRAINT position_department_id_fkey FOREIGN KEY (department_id) REFERENCES hcm.department(department_id);


--
-- TOC entry 3666 (class 2606 OID 16518)
-- Name: position position_organization_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."position"
    ADD CONSTRAINT position_organization_id_fkey FOREIGN KEY (organization_id) REFERENCES hcm.organization(organization_id);


--
-- TOC entry 3667 (class 2606 OID 16528)
-- Name: position position_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."position"
    ADD CONSTRAINT position_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.position_status(status_id);


--
-- TOC entry 3668 (class 2606 OID 16513)
-- Name: position position_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."position"
    ADD CONSTRAINT position_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3682 (class 2606 OID 16900)
-- Name: skill skill_organization_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.skill
    ADD CONSTRAINT skill_organization_id_fkey FOREIGN KEY (organization_id) REFERENCES hcm.organization(organization_id);


--
-- TOC entry 3683 (class 2606 OID 16895)
-- Name: skill skill_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.skill
    ADD CONSTRAINT skill_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3718 (class 2606 OID 33010)
-- Name: state state_country_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.state
    ADD CONSTRAINT state_country_id_fkey FOREIGN KEY (country_id) REFERENCES hcm.country(country_id);


--
-- TOC entry 3672 (class 2606 OID 16796)
-- Name: user user_organization_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."user"
    ADD CONSTRAINT user_organization_id_fkey FOREIGN KEY (organization_id) REFERENCES hcm.organization(organization_id);


--
-- TOC entry 3673 (class 2606 OID 16801)
-- Name: user user_role_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."user"
    ADD CONSTRAINT user_role_id_fkey FOREIGN KEY (role_id) REFERENCES hcm.user_role(role_id);


--
-- TOC entry 3674 (class 2606 OID 16811)
-- Name: user user_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."user"
    ADD CONSTRAINT user_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.user_status(status_id);


--
-- TOC entry 3675 (class 2606 OID 16791)
-- Name: user user_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."user"
    ADD CONSTRAINT user_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3676 (class 2606 OID 16806)
-- Name: user user_type_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm."user"
    ADD CONSTRAINT user_type_id_fkey FOREIGN KEY (type_id) REFERENCES hcm.user_type(type_id);


--
-- TOC entry 3669 (class 2606 OID 16657)
-- Name: vendor vendor_organization_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.vendor
    ADD CONSTRAINT vendor_organization_id_fkey FOREIGN KEY (organization_id) REFERENCES hcm.organization(organization_id);


--
-- TOC entry 3670 (class 2606 OID 16662)
-- Name: vendor vendor_status_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.vendor
    ADD CONSTRAINT vendor_status_id_fkey FOREIGN KEY (status_id) REFERENCES hcm.vendor_status(status_id);


--
-- TOC entry 3671 (class 2606 OID 16652)
-- Name: vendor vendor_tenant_id_fkey; Type: FK CONSTRAINT; Schema: hcm; Owner: postgres
--

ALTER TABLE ONLY hcm.vendor
    ADD CONSTRAINT vendor_tenant_id_fkey FOREIGN KEY (tenant_id) REFERENCES hcm.tenant(tenant_id);


--
-- TOC entry 3972 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA hcm; Type: ACL; Schema: -; Owner: hcm_admin
--

GRANT USAGE ON SCHEMA hcm TO hcm_readonly;
GRANT USAGE ON SCHEMA hcm TO hcm_writer;


--
-- TOC entry 3973 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE department; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.department TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.department TO hcm_writer;


--
-- TOC entry 3974 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE department_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.department_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.department_status TO hcm_writer;


--
-- TOC entry 3975 (class 0 OID 0)
-- Dependencies: 261
-- Name: TABLE active_departments; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.active_departments TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.active_departments TO hcm_writer;


--
-- TOC entry 3976 (class 0 OID 0)
-- Dependencies: 265
-- Name: TABLE application; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.application TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.application TO hcm_writer;


--
-- TOC entry 3978 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE application_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.application_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.application_status TO hcm_writer;


--
-- TOC entry 3980 (class 0 OID 0)
-- Dependencies: 267
-- Name: TABLE approval; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.approval TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.approval TO hcm_writer;


--
-- TOC entry 3982 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE approval_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.approval_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.approval_status TO hcm_writer;


--
-- TOC entry 3984 (class 0 OID 0)
-- Dependencies: 251
-- Name: TABLE candidate; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.candidate TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.candidate TO hcm_writer;


--
-- TOC entry 3985 (class 0 OID 0)
-- Dependencies: 257
-- Name: TABLE candidate_certification; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.candidate_certification TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.candidate_certification TO hcm_writer;


--
-- TOC entry 3988 (class 0 OID 0)
-- Dependencies: 253
-- Name: TABLE candidate_education; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.candidate_education TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.candidate_education TO hcm_writer;


--
-- TOC entry 3992 (class 0 OID 0)
-- Dependencies: 260
-- Name: TABLE candidate_skill; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.candidate_skill TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.candidate_skill TO hcm_writer;


--
-- TOC entry 3993 (class 0 OID 0)
-- Dependencies: 255
-- Name: TABLE candidate_work_history; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.candidate_work_history TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.candidate_work_history TO hcm_writer;


--
-- TOC entry 4000 (class 0 OID 0)
-- Dependencies: 275
-- Name: TABLE interview; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.interview TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.interview TO hcm_writer;


--
-- TOC entry 4001 (class 0 OID 0)
-- Dependencies: 277
-- Name: TABLE interview_feedback; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.interview_feedback TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.interview_feedback TO hcm_writer;


--
-- TOC entry 4004 (class 0 OID 0)
-- Dependencies: 243
-- Name: TABLE interview_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.interview_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.interview_status TO hcm_writer;


--
-- TOC entry 4006 (class 0 OID 0)
-- Dependencies: 263
-- Name: TABLE job_requisition; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.job_requisition TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.job_requisition TO hcm_writer;


--
-- TOC entry 4008 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE job_requisition_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.job_requisition_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.job_requisition_status TO hcm_writer;


--
-- TOC entry 4010 (class 0 OID 0)
-- Dependencies: 271
-- Name: TABLE offer; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.offer TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.offer TO hcm_writer;


--
-- TOC entry 4012 (class 0 OID 0)
-- Dependencies: 239
-- Name: TABLE offer_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.offer_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.offer_status TO hcm_writer;


--
-- TOC entry 4014 (class 0 OID 0)
-- Dependencies: 273
-- Name: TABLE onboarding; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.onboarding TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.onboarding TO hcm_writer;


--
-- TOC entry 4016 (class 0 OID 0)
-- Dependencies: 241
-- Name: TABLE onboarding_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.onboarding_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.onboarding_status TO hcm_writer;


--
-- TOC entry 4018 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE organization; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.organization TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.organization TO hcm_writer;


--
-- TOC entry 4019 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE organization_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.organization_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.organization_status TO hcm_writer;


--
-- TOC entry 4021 (class 0 OID 0)
-- Dependencies: 269
-- Name: TABLE pipeline_stage; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.pipeline_stage TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.pipeline_stage TO hcm_writer;


--
-- TOC entry 4023 (class 0 OID 0)
-- Dependencies: 228
-- Name: TABLE "position"; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm."position" TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm."position" TO hcm_writer;


--
-- TOC entry 4025 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE position_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.position_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.position_status TO hcm_writer;


--
-- TOC entry 4027 (class 0 OID 0)
-- Dependencies: 259
-- Name: TABLE skill; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.skill TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.skill TO hcm_writer;


--
-- TOC entry 4032 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE tenant; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.tenant TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.tenant TO hcm_writer;


--
-- TOC entry 4033 (class 0 OID 0)
-- Dependencies: 250
-- Name: TABLE "user"; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm."user" TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm."user" TO hcm_writer;


--
-- TOC entry 4034 (class 0 OID 0)
-- Dependencies: 247
-- Name: TABLE user_role; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.user_role TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.user_role TO hcm_writer;


--
-- TOC entry 4036 (class 0 OID 0)
-- Dependencies: 249
-- Name: TABLE user_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.user_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.user_status TO hcm_writer;


--
-- TOC entry 4038 (class 0 OID 0)
-- Dependencies: 245
-- Name: TABLE user_type; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.user_type TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.user_type TO hcm_writer;


--
-- TOC entry 4040 (class 0 OID 0)
-- Dependencies: 237
-- Name: TABLE vendor; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.vendor TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.vendor TO hcm_writer;


--
-- TOC entry 4041 (class 0 OID 0)
-- Dependencies: 236
-- Name: TABLE vendor_status; Type: ACL; Schema: hcm; Owner: postgres
--

GRANT SELECT ON TABLE hcm.vendor_status TO hcm_readonly;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hcm.vendor_status TO hcm_writer;


-- Completed on 2025-07-11 04:57:57

--
-- PostgreSQL database dump complete
--

