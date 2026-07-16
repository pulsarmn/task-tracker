--liquibase formatted sql

--changeset pulsarmn:create-tasks-table
CREATE TABLE tasks
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    due_date    TIMESTAMP WITH TIME ZONE,
    status      VARCHAR(32)  NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE,
    updated_at  TIMESTAMP WITH TIME ZONE
);

--changeset pulsarmn:create-index-on-due-date-field
CREATE INDEX idx_tasks_due_date ON tasks (due_date);

--changeset pulsarmn:create-index-on-status-field
CREATE INDEX idx_tasks_status ON tasks (status);

--changeset pulsarmn:create-index-on-status-and-due-date-fields
CREATE INDEX idx_tasks_status_due_data ON tasks (status, due_date);
