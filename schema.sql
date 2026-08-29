-- Create spatial extension for cross-border fleet tracking
CREATE EXTENSION IF NOT EXISTS postgis;

-- Enum Types for Country Code and Escrow Status
CREATE TYPE country_code AS ENUM ('ZW', 'ZM', 'MZ', 'MW');
CREATE TYPE escrow_status AS ENUM ('HELD_IN_ESCROW', 'RELEASED_TO_SELLER', 'REFUNDED_TO_BUYER');

-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    country country_code NOT NULL,
    role VARCHAR(50) NOT NULL, -- ROLE_BUYER, ROLE_SELLER, ROLE_DRIVER
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Orders Table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    buyer_id BIGINT REFERENCES users(id),
    seller_id BIGINT REFERENCES users(id),
    total_amount NUMERIC(12, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    escrow_state VARCHAR(50) DEFAULT 'HELD_IN_ESCROW',
    origin_country VARCHAR(10) NOT NULL,
    destination_country VARCHAR(10) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Fleet & Border Clearance Tracking
CREATE TABLE waybill_tracking (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id),
    driver_id BIGINT REFERENCES users(id),
    current_location GEOMETRY(Point, 4326),
    border_post_location VARCHAR(100), -- Chirundu, Forbes/Machipanda, Nyamapanda, Beira
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);