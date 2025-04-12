ALTER TABLE projects ADD COLUMN image_url VARCHAR(255);
ALTER TABLE projects ADD COLUMN short_description VARCHAR(500);

CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    publish_date TIMESTAMP WITH TIME ZONE NOT NULL,
    likes_count INTEGER NOT NULL DEFAULT 0,
    project_id BIGINT REFERENCES projects(id),
    author_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    project_id BIGINT REFERENCES projects(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (user_id, project_id)
);

CREATE TABLE project_admins (
    project_id BIGINT REFERENCES projects(id),
    user_id BIGINT REFERENCES users(id),
    PRIMARY KEY (project_id, user_id)
);