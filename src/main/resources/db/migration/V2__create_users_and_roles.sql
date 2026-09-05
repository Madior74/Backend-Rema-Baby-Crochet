-- Rôles
CREATE TABLE roles (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE  -- ex: ROLE_ADMIN
);

-- Utilisateurs
CREATE TABLE users (
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,  -- hash BCrypt
    enabled    BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- Case-insensitive uniqueness matches UserRepository.findByEmailIgnoreCase().
CREATE UNIQUE INDEX ux_users_email_lower ON users (LOWER(email));

-- Relation many-to-many
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Données initiales
INSERT INTO roles (name)
VALUES ('ROLE_CLIENT'), ('ROLE_ADMIN')
ON CONFLICT (name) DO NOTHING;

    -- Mot de passe : Admin@2026! (à changer en prod)
-- Générer avec : new BCryptPasswordEncoder().encode("Admin@2026!")


