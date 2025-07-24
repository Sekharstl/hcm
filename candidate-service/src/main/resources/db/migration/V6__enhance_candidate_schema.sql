-- Enhanced Candidate Schema - Focused Approach
-- Migration: V6__enhance_candidate_schema.sql

-- 0. Drop existing tables if they exist
DROP TABLE IF EXISTS hcm.state CASCADE;
DROP TABLE IF EXISTS hcm.country CASCADE;
DROP TABLE IF EXISTS hcm.id_type CASCADE;
DROP TABLE IF EXISTS hcm.document_type CASCADE;

-- 1. Add essential fields to candidate table
ALTER TABLE hcm.candidate 
ADD COLUMN IF NOT EXISTS middle_name VARCHAR(100),
ADD COLUMN IF NOT EXISTS city VARCHAR(100),
ADD COLUMN IF NOT EXISTS state VARCHAR(100),
ADD COLUMN IF NOT EXISTS country VARCHAR(100),
ADD COLUMN IF NOT EXISTS postal_code VARCHAR(20),
ADD COLUMN IF NOT EXISTS linkedin_url VARCHAR(255),
ADD COLUMN IF NOT EXISTS current_salary DECIMAL(12,2),
ADD COLUMN IF NOT EXISTS expected_salary DECIMAL(12,2),
ADD COLUMN IF NOT EXISTS notice_period INTEGER, -- in days
ADD COLUMN IF NOT EXISTS availability_date DATE,
ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'ACTIVE',
ADD COLUMN IF NOT EXISTS source VARCHAR(100), -- recruitment source
ADD COLUMN IF NOT EXISTS vendor_id UUID, -- recruitment vendor/agency
ADD COLUMN IF NOT EXISTS notes TEXT;

-- 2. Create document types lookup table
CREATE TABLE IF NOT EXISTS hcm.document_type (
    document_type_id SERIAL PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES hcm.tenant(tenant_id),
    type_name VARCHAR(100) NOT NULL,
    description TEXT,
    is_required BOOLEAN DEFAULT false,
    max_file_size INTEGER DEFAULT 10485760, -- 10MB default
    allowed_extensions VARCHAR(500), -- comma separated
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL,
    UNIQUE(tenant_id, type_name)
);

-- 3. Create candidate documents table
CREATE TABLE IF NOT EXISTS hcm.candidate_document (
    document_id SERIAL PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES hcm.candidate(candidate_id) ON DELETE CASCADE,
    document_type_id INTEGER NOT NULL REFERENCES hcm.document_type(document_type_id),
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100),
    upload_date TIMESTAMPTZ NOT NULL DEFAULT now(),
    expiry_date DATE,
    is_verified BOOLEAN DEFAULT false,
    verification_date TIMESTAMPTZ,
    verified_by UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
);

-- 4. Create countries table
CREATE TABLE IF NOT EXISTS hcm.country (
    country_id SERIAL PRIMARY KEY,
    country_code VARCHAR(3) NOT NULL UNIQUE,
    country_name VARCHAR(100) NOT NULL,
    phone_code VARCHAR(10),
    currency_code VARCHAR(3),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
);

-- 5. Create states/provinces table
CREATE TABLE IF NOT EXISTS hcm.state (
    state_id SERIAL PRIMARY KEY,
    country_id INTEGER NOT NULL REFERENCES hcm.country(country_id),
    state_code VARCHAR(10) NOT NULL,
    state_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL,
    UNIQUE(country_id, state_code)
);

-- 6. Create ID types lookup table
CREATE TABLE IF NOT EXISTS hcm.id_type (
    id_type_id SERIAL PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES hcm.tenant(tenant_id),
    type_name VARCHAR(100) NOT NULL,
    description TEXT,
    is_required BOOLEAN DEFAULT false,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL,
    UNIQUE(tenant_id, type_name)
);

-- 7. Create candidate identities table
CREATE TABLE IF NOT EXISTS hcm.candidate_identity (
    identity_id SERIAL PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES hcm.candidate(candidate_id) ON DELETE CASCADE,
    id_type_id INTEGER NOT NULL REFERENCES hcm.id_type(id_type_id),
    id_number VARCHAR(100) NOT NULL,
    issuing_country VARCHAR(100),
    issue_date DATE,
    expiry_date DATE,
    is_verified BOOLEAN DEFAULT false,
    verification_date TIMESTAMPTZ,
    verified_by UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
);

-- 8. Create candidate references table
CREATE TABLE IF NOT EXISTS hcm.candidate_reference (
    reference_id SERIAL PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES hcm.candidate(candidate_id) ON DELETE CASCADE,
    reference_name VARCHAR(200) NOT NULL,
    relationship VARCHAR(100),
    company VARCHAR(200),
    position VARCHAR(200),
    email VARCHAR(255),
    phone VARCHAR(20),
    is_verified BOOLEAN DEFAULT false,
    verification_date TIMESTAMPTZ,
    verified_by UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_by UUID NOT NULL
);

-- 9. Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_candidate_status ON hcm.candidate(status);
CREATE INDEX IF NOT EXISTS idx_candidate_source ON hcm.candidate(source);
CREATE INDEX IF NOT EXISTS idx_candidate_vendor ON hcm.candidate(vendor_id);
CREATE INDEX IF NOT EXISTS idx_candidate_document_candidate ON hcm.candidate_document(candidate_id);
CREATE INDEX IF NOT EXISTS idx_candidate_document_type ON hcm.candidate_document(document_type_id);
CREATE INDEX IF NOT EXISTS idx_candidate_identity_candidate ON hcm.candidate_identity(candidate_id);
CREATE INDEX IF NOT EXISTS idx_candidate_identity_type ON hcm.candidate_identity(id_type_id);
CREATE INDEX IF NOT EXISTS idx_candidate_reference_candidate ON hcm.candidate_reference(candidate_id);

-- 10. Insert comprehensive list of all recognized countries
INSERT INTO hcm.country (country_code, country_name, phone_code, currency_code, created_at, created_by, updated_at, updated_by) VALUES
-- North America
('USA', 'United States', '+1', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CAN', 'Canada', '+1', 'CAD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MEX', 'Mexico', '+52', 'MXN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GTM', 'Guatemala', '+502', 'GTQ', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BLZ', 'Belize', '+501', 'BZD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SLV', 'El Salvador', '+503', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('HND', 'Honduras', '+504', 'HNL', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NIC', 'Nicaragua', '+505', 'NIO', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CRI', 'Costa Rica', '+506', 'CRC', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PAN', 'Panama', '+507', 'PAB', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CUB', 'Cuba', '+53', 'CUP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('JAM', 'Jamaica', '+1876', 'JMD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('HTI', 'Haiti', '+509', 'HTG', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('DOM', 'Dominican Republic', '+1809', 'DOP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PRI', 'Puerto Rico', '+1787', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BRB', 'Barbados', '+1246', 'BBD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GRD', 'Grenada', '+1473', 'XCD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LCA', 'Saint Lucia', '+1758', 'XCD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('VCT', 'Saint Vincent and the Grenadines', '+1784', 'XCD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ATG', 'Antigua and Barbuda', '+1268', 'XCD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('DMA', 'Dominica', '+1767', 'XCD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('KNA', 'Saint Kitts and Nevis', '+1869', 'XCD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TTO', 'Trinidad and Tobago', '+1868', 'TTD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BHS', 'Bahamas', '+1242', 'BSD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),

-- Europe
('GBR', 'United Kingdom', '+44', 'GBP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('DEU', 'Germany', '+49', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('FRA', 'France', '+33', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ITA', 'Italy', '+39', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ESP', 'Spain', '+34', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NLD', 'Netherlands', '+31', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CHE', 'Switzerland', '+41', 'CHF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SWE', 'Sweden', '+46', 'SEK', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NOR', 'Norway', '+47', 'NOK', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('DNK', 'Denmark', '+45', 'DKK', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('FIN', 'Finland', '+358', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('POL', 'Poland', '+48', 'PLN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('AUT', 'Austria', '+43', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BEL', 'Belgium', '+32', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('IRL', 'Ireland', '+353', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PRT', 'Portugal', '+351', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GRC', 'Greece', '+30', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CZE', 'Czech Republic', '+420', 'CZK', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('HUN', 'Hungary', '+36', 'HUF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ROU', 'Romania', '+40', 'RON', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BGR', 'Bulgaria', '+359', 'BGN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('HRV', 'Croatia', '+385', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SVN', 'Slovenia', '+386', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SVK', 'Slovakia', '+421', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LTU', 'Lithuania', '+370', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LVA', 'Latvia', '+371', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('EST', 'Estonia', '+372', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LUX', 'Luxembourg', '+352', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MLT', 'Malta', '+356', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CYP', 'Cyprus', '+357', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ISL', 'Iceland', '+354', 'ISK', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ALB', 'Albania', '+355', 'ALL', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MKD', 'North Macedonia', '+389', 'MKD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MNE', 'Montenegro', '+382', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BIH', 'Bosnia and Herzegovina', '+387', 'BAM', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SRB', 'Serbia', '+381', 'RSD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('KOS', 'Kosovo', '+383', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MDA', 'Moldova', '+373', 'MDL', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('UKR', 'Ukraine', '+380', 'UAH', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BLR', 'Belarus', '+375', 'BYN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('RUS', 'Russia', '+7', 'RUB', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GEO', 'Georgia', '+995', 'GEL', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ARM', 'Armenia', '+374', 'AMD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('AZE', 'Azerbaijan', '+994', 'AZN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('AND', 'Andorra', '+376', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MCO', 'Monaco', '+377', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LIE', 'Liechtenstein', '+423', 'CHF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SMR', 'San Marino', '+378', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('VAT', 'Vatican City', '+379', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),

-- Asia
('CHN', 'China', '+86', 'CNY', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('JPN', 'Japan', '+81', 'JPY', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('IND', 'India', '+91', 'INR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('KOR', 'South Korea', '+82', 'KRW', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SGP', 'Singapore', '+65', 'SGD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MYS', 'Malaysia', '+60', 'MYR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('THA', 'Thailand', '+66', 'THB', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('IDN', 'Indonesia', '+62', 'IDR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PHL', 'Philippines', '+63', 'PHP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('VNM', 'Vietnam', '+84', 'VND', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TWN', 'Taiwan', '+886', 'TWD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('HKG', 'Hong Kong', '+852', 'HKD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ISR', 'Israel', '+972', 'ILS', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TUR', 'Turkey', '+90', 'TRY', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SAU', 'Saudi Arabia', '+966', 'SAR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ARE', 'United Arab Emirates', '+971', 'AED', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('QAT', 'Qatar', '+974', 'QAR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('KWT', 'Kuwait', '+965', 'KWD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BHR', 'Bahrain', '+973', 'BHD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('OMN', 'Oman', '+968', 'OMR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('YEM', 'Yemen', '+967', 'YER', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('JOR', 'Jordan', '+962', 'JOD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LBN', 'Lebanon', '+961', 'LBP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SYR', 'Syria', '+963', 'SYP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('IRQ', 'Iraq', '+964', 'IQD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('IRN', 'Iran', '+98', 'IRR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('AFG', 'Afghanistan', '+93', 'AFN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PAK', 'Pakistan', '+92', 'PKR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BGD', 'Bangladesh', '+880', 'BDT', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NPL', 'Nepal', '+977', 'NPR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BTN', 'Bhutan', '+975', 'BTN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MMR', 'Myanmar', '+95', 'MMK', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LAO', 'Laos', '+856', 'LAK', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('KHM', 'Cambodia', '+855', 'KHR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MNG', 'Mongolia', '+976', 'MNT', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('KAZ', 'Kazakhstan', '+7', 'KZT', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('UZB', 'Uzbekistan', '+998', 'UZS', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TJK', 'Tajikistan', '+992', 'TJS', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('KGZ', 'Kyrgyzstan', '+996', 'KGS', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TKM', 'Turkmenistan', '+993', 'TMT', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MAC', 'Macau', '+853', 'MOP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BRN', 'Brunei', '+673', 'BND', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TLS', 'Timor-Leste', '+670', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PNG', 'Papua New Guinea', '+675', 'PGK', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('FJI', 'Fiji', '+679', 'FJD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SLB', 'Solomon Islands', '+677', 'SBD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('VUT', 'Vanuatu', '+678', 'VUV', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NCL', 'New Caledonia', '+687', 'XPF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('WSM', 'Samoa', '+685', 'WST', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TON', 'Tonga', '+676', 'TOP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('KIR', 'Kiribati', '+686', 'AUD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TUV', 'Tuvalu', '+688', 'AUD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NRU', 'Nauru', '+674', 'AUD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PLW', 'Palau', '+680', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('FSM', 'Micronesia', '+691', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MHL', 'Marshall Islands', '+692', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('COK', 'Cook Islands', '+682', 'NZD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NIU', 'Niue', '+683', 'NZD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TKL', 'Tokelau', '+690', 'NZD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ASM', 'American Samoa', '+1684', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GUM', 'Guam', '+1671', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MNP', 'Northern Mariana Islands', '+1670', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PYF', 'French Polynesia', '+689', 'XPF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('WLF', 'Wallis and Futuna', '+681', 'XPF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PCN', 'Pitcairn Islands', '+64', 'NZD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NFK', 'Norfolk Island', '+672', 'AUD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CXR', 'Christmas Island', '+61', 'AUD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CCK', 'Cocos Islands', '+61', 'AUD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('HMD', 'Heard and McDonald Islands', '+672', 'AUD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('AUS', 'Australia', '+61', 'AUD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NZL', 'New Zealand', '+64', 'NZD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),

-- South America
('BRA', 'Brazil', '+55', 'BRL', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ARG', 'Argentina', '+54', 'ARS', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CHL', 'Chile', '+56', 'CLP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('COL', 'Colombia', '+57', 'COP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PER', 'Peru', '+51', 'PEN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('VEN', 'Venezuela', '+58', 'VES', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ECU', 'Ecuador', '+593', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BOL', 'Bolivia', '+591', 'BOB', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('PRY', 'Paraguay', '+595', 'PYG', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('URY', 'Uruguay', '+598', 'UYU', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GUY', 'Guyana', '+592', 'GYD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SUR', 'Suriname', '+597', 'SRD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GUF', 'French Guiana', '+594', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('FLK', 'Falkland Islands', '+500', 'FKP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),

-- Africa
('ZAF', 'South Africa', '+27', 'ZAR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('EGY', 'Egypt', '+20', 'EGP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NGA', 'Nigeria', '+234', 'NGN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('KEN', 'Kenya', '+254', 'KES', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MAR', 'Morocco', '+212', 'MAD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TUN', 'Tunisia', '+216', 'TND', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GHA', 'Ghana', '+233', 'GHS', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ETH', 'Ethiopia', '+251', 'ETB', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('DZA', 'Algeria', '+213', 'DZD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SDN', 'Sudan', '+249', 'SDG', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SSD', 'South Sudan', '+211', 'SSP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LBY', 'Libya', '+218', 'LYD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TCD', 'Chad', '+235', 'XAF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NER', 'Niger', '+227', 'XOF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MLI', 'Mali', '+223', 'XOF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BFA', 'Burkina Faso', '+226', 'XOF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CIV', 'Ivory Coast', '+225', 'XOF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SEN', 'Senegal', '+221', 'XOF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GMB', 'Gambia', '+220', 'GMD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GNB', 'Guinea-Bissau', '+245', 'XOF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GIN', 'Guinea', '+224', 'GNF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SLE', 'Sierra Leone', '+232', 'SLL', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LBR', 'Liberia', '+231', 'LRD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CMR', 'Cameroon', '+237', 'XAF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CAF', 'Central African Republic', '+236', 'XAF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('COG', 'Republic of the Congo', '+242', 'XAF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('COD', 'Democratic Republic of the Congo', '+243', 'CDF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GAB', 'Gabon', '+241', 'XAF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('GNQ', 'Equatorial Guinea', '+240', 'XAF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('STP', 'São Tomé and Príncipe', '+239', 'STN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('AGO', 'Angola', '+244', 'AOA', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ZMB', 'Zambia', '+260', 'ZMW', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ZWE', 'Zimbabwe', '+263', 'ZWL', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BWA', 'Botswana', '+267', 'BWP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('NAM', 'Namibia', '+264', 'NAD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SWZ', 'Eswatini', '+268', 'SZL', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('LSO', 'Lesotho', '+266', 'LSL', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MDG', 'Madagascar', '+261', 'MGA', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MUS', 'Mauritius', '+230', 'MUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SYC', 'Seychelles', '+248', 'SCR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('COM', 'Comoros', '+269', 'KMF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('DJI', 'Djibouti', '+253', 'DJF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SOM', 'Somalia', '+252', 'SOS', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ERI', 'Eritrea', '+291', 'ERN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('BDI', 'Burundi', '+257', 'BIF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('RWA', 'Rwanda', '+250', 'RWF', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('UGA', 'Uganda', '+256', 'UGX', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('TZA', 'Tanzania', '+255', 'TZS', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MOZ', 'Mozambique', '+258', 'MZN', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MWI', 'Malawi', '+265', 'MWK', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('CPV', 'Cape Verde', '+238', 'CVE', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('SHN', 'Saint Helena', '+290', 'SHP', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('IOT', 'British Indian Ocean Territory', '+246', 'USD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('MYT', 'Mayotte', '+262', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('REU', 'Réunion', '+262', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ATF', 'French Southern Territories', '+262', 'EUR', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('ESH', 'Western Sahara', '+212', 'MAD', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid);

-- 11. Insert comprehensive states/provinces for major countries
-- United States States (Top 20 by population)
INSERT INTO hcm.state (country_id, state_code, state_name, created_at, created_by, updated_at, updated_by)
SELECT c.country_id, 'CA', 'California', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'TX', 'Texas', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'FL', 'Florida', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'NY', 'New York', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'IL', 'Illinois', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'PA', 'Pennsylvania', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'OH', 'Ohio', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'GA', 'Georgia', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'NC', 'North Carolina', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'MI', 'Michigan', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'NJ', 'New Jersey', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'VA', 'Virginia', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'WA', 'Washington', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'AZ', 'Arizona', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'MA', 'Massachusetts', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'TN', 'Tennessee', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'IN', 'Indiana', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'MO', 'Missouri', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'MD', 'Maryland', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'CO', 'Colorado', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA'
UNION ALL
SELECT c.country_id, 'WI', 'Wisconsin', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'USA';

-- India States (Major states)
INSERT INTO hcm.state (country_id, state_code, state_name, created_at, created_by, updated_at, updated_by)
SELECT c.country_id, 'MH', 'Maharashtra', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'UP', 'Uttar Pradesh', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'WB', 'West Bengal', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'BR', 'Bihar', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'MP', 'Madhya Pradesh', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'TN', 'Tamil Nadu', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'KA', 'Karnataka', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'GJ', 'Gujarat', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'AP', 'Andhra Pradesh', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'TG', 'Telangana', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'DL', 'Delhi', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'HR', 'Haryana', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'RJ', 'Rajasthan', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'KL', 'Kerala', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND'
UNION ALL
SELECT c.country_id, 'PB', 'Punjab', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'IND';

-- Canada Provinces and Territories
INSERT INTO hcm.state (country_id, state_code, state_name, created_at, created_by, updated_at, updated_by)
SELECT c.country_id, 'ON', 'Ontario', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'QC', 'Quebec', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'BC', 'British Columbia', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'AB', 'Alberta', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'NS', 'Nova Scotia', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'NB', 'New Brunswick', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'MB', 'Manitoba', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'SK', 'Saskatchewan', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'PE', 'Prince Edward Island', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'NL', 'Newfoundland and Labrador', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'NT', 'Northwest Territories', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'NU', 'Nunavut', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN'
UNION ALL
SELECT c.country_id, 'YT', 'Yukon', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'CAN';

-- Australia States and Territories
INSERT INTO hcm.state (country_id, state_code, state_name, created_at, created_by, updated_at, updated_by)
SELECT c.country_id, 'NSW', 'New South Wales', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'AUS'
UNION ALL
SELECT c.country_id, 'VIC', 'Victoria', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'AUS'
UNION ALL
SELECT c.country_id, 'QLD', 'Queensland', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'AUS'
UNION ALL
SELECT c.country_id, 'WA', 'Western Australia', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'AUS'
UNION ALL
SELECT c.country_id, 'SA', 'South Australia', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'AUS'
UNION ALL
SELECT c.country_id, 'TAS', 'Tasmania', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'AUS'
UNION ALL
SELECT c.country_id, 'ACT', 'Australian Capital Territory', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'AUS'
UNION ALL
SELECT c.country_id, 'NT', 'Northern Territory', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'AUS';

-- UK Countries/Regions
INSERT INTO hcm.state (country_id, state_code, state_name, created_at, created_by, updated_at, updated_by)
SELECT c.country_id, 'ENG', 'England', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'GBR'
UNION ALL
SELECT c.country_id, 'SCT', 'Scotland', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'GBR'
UNION ALL
SELECT c.country_id, 'WLS', 'Wales', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'GBR'
UNION ALL
SELECT c.country_id, 'NIR', 'Northern Ireland', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'GBR';

-- Germany States (Bundesländer)
INSERT INTO hcm.state (country_id, state_code, state_name, created_at, created_by, updated_at, updated_by)
SELECT c.country_id, 'BY', 'Bavaria', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'NW', 'North Rhine-Westphalia', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'BW', 'Baden-Württemberg', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'HE', 'Hesse', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'NI', 'Lower Saxony', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'RP', 'Rhineland-Palatinate', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'BE', 'Berlin', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'HH', 'Hamburg', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'SH', 'Schleswig-Holstein', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'BB', 'Brandenburg', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'MV', 'Mecklenburg-Vorpommern', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'SN', 'Saxony', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'ST', 'Saxony-Anhalt', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'TH', 'Thuringia', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'SL', 'Saarland', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU'
UNION ALL
SELECT c.country_id, 'HB', 'Bremen', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid
FROM hcm.country c WHERE c.country_code = 'DEU';

-- 12. Insert default document types
INSERT INTO hcm.document_type (tenant_id, type_name, description, is_required, allowed_extensions, created_at, created_by, updated_at, updated_by) VALUES
('00000000-0000-0000-0000-000000000001'::uuid, 'RESUME', 'Professional resume/CV', true, 'pdf,doc,docx', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'PHOTO', 'Profile photo', false, 'jpg,jpeg,png', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'COVER_LETTER', 'Cover letter', false, 'pdf,doc,docx', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'PORTFOLIO', 'Work portfolio', false, 'pdf,zip,rar', now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid);

-- 13. Insert default ID types
INSERT INTO hcm.id_type (tenant_id, type_name, description, is_required, created_at, created_by, updated_at, updated_by) VALUES
('00000000-0000-0000-0000-000000000001'::uuid, 'PASSPORT', 'Passport', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'NATIONAL_ID', 'National ID Card', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'DRIVERS_LICENSE', 'Drivers License', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'SSN', 'Social Security Number', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'AADHAR', 'Aadhar Card (India)', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'PAN', 'PAN Card (India)', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'VOTER_ID', 'Voter ID Card', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'BIRTH_CERTIFICATE', 'Birth Certificate', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'WORK_PERMIT', 'Work Permit', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'GREEN_CARD', 'Green Card (US)', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'VISA', 'Visa', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'EMPLOYEE_ID', 'Employee ID', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'STUDENT_ID', 'Student ID', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid),
('00000000-0000-0000-0000-000000000001'::uuid, 'MILITARY_ID', 'Military ID', false, now(), '00000000-0000-0000-0000-000000000001'::uuid, now(), '00000000-0000-0000-0000-000000000001'::uuid); 