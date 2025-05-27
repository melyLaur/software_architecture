CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

/* Table employee */
CREATE TABLE "employees"
(
    "employee_id"               UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    "first_name"              varchar        NOT NULL,
    "last_name"               varchar        NOT NULL
);