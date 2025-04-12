-- Insert initial roles
INSERT INTO user_roles (user_id, role) VALUES 
(1, 'ADMIN'),
(1, 'USER'),
(1, 'PROJECT_CREATOR'),
(2, 'USER'),
(2, 'PROJECT_CREATOR'),
(3, 'USER');

-- Insert some badges
INSERT INTO badges (name, description, icon_url, created_at, updated_at) VALUES 
('Eco Warrior', 'Awarded for supporting 5+ environmental projects', '/badges/eco-warrior.png', NOW(), NOW()),
('Social Hero', 'Awarded for supporting 5+ social projects', '/badges/social-hero.png', NOW(), NOW()),
('Governance Guardian', 'Awarded for supporting 5+ governance projects', '/badges/governance-guardian.png', NOW(), NOW()),
('Early Supporter', 'Awarded for supporting projects in their first week', '/badges/early-supporter.png', NOW(), NOW());