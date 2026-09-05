-- Extension pour generer des UUID aléatoires
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

CREATE TABLE categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    slug VARCHAR(90) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    age_range VARCHAR(50) NOT NULL,
    material VARCHAR(100) not null ,
    image_url VARCHAR(255),
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
                        CHECK (status IN ('ACTIVE', 'INACTIVE')),
    category_id BIGINT NOT NULL REFERENCES categories(id),
    product_type VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' CHECK (product_type IN ('ACTIVE', 'INACTIVE')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index sur les colonnes fréquemment utilisées pour les recherches
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_product_type ON products(product_type);
CREATE INDEX idx_products_name_trgm ON products USING gin (name gin_trgm_ops);


