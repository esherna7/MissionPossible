-- created users, households, household_members tables

--create users table
CREATE TABLE users (
    id uuid PRIMARY KEY,
    email varchar(255) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- create households table
CREATE TABLE households (
    id uuid PRIMARY KEY,
    created_by_user_id uuid NOT NULL REFERENCES users(id),
    name varchar(100) NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_households_creator
    ON households(created_by_user_id);

-- create household_members table
CREATE TABLE household_members (
    id uuid PRIMARY KEY,
    household_id uuid NOT NULL REFERENCES households(id),
    user_id uuid REFERENCES users(id),
    display_name varchar(100) NOT NULL,
    role varchar(20) NOT NULL CHECK (role IN ('ADMIN', 'MEMBER')),
    is_active boolean NOT NULL DEFAULT TRUE,
    joined_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (household_id, user_id)
);

CREATE INDEX idx_members_user
    ON household_members(user_id);