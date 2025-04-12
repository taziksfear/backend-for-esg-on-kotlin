-- Create tables for ESG crowdfunding platform

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- User roles table (many-to-many relationship)
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id),
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role)
);

-- Badges table
CREATE TABLE badges (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500) NOT NULL,
    icon_url VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- User badges table (many-to-many relationship)
CREATE TABLE user_badges (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    badge_id BIGINT NOT NULL REFERENCES badges(id),
    awarded_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (user_id, badge_id)
);

-- Projects table
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(5000) NOT NULL,
    goal_amount NUMERIC(19, 2) NOT NULL,
    current_amount NUMERIC(19, 2) NOT NULL DEFAULT 0,
    deadline DATE NOT NULL,
    status VARCHAR(30) NOT NULL,
    creator_user_id BIGINT NOT NULL REFERENCES users(id),
    esg_rating_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- ESG ratings table
CREATE TABLE esg_ratings (
    id BIGSERIAL PRIMARY KEY,
    environment_score INTEGER NOT NULL,
    social_score INTEGER NOT NULL,
    governance_score INTEGER NOT NULL,
    assessment_notes TEXT,
    assessed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Add foreign key constraint for esg_rating_id in projects table
ALTER TABLE projects 
ADD CONSTRAINT fk_projects_esg_rating 
FOREIGN KEY (esg_rating_id) REFERENCES esg_ratings(id);

-- Investments table
CREATE TABLE investments (
    id BIGSERIAL PRIMARY KEY,
    investor_user_id BIGINT NOT NULL REFERENCES users(id),
    project_id BIGINT NOT NULL REFERENCES projects(id),
    amount NUMERIC(19, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    transaction_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Indexes for better performance
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_creator ON projects(creator_user_id);
CREATE INDEX idx_investments_investor ON investments(investor_user_id);
CREATE INDEX idx_investments_project ON investments(project_id);
CREATE INDEX idx_user_roles_user ON user_roles(user_id);
CREATE INDEX idx_user_badges_user ON user_badges(user_id);