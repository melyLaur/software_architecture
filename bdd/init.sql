CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

/* Table employees */
CREATE TABLE "employees"
(
    "employee_id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    "first_name"  varchar NOT NULL,
    "last_name"   varchar NOT NULL,
    "role"        varchar NOT NULL DEFAULT 'EMPLOYEE',
    "email"       varchar NOT NULL
);

/* Table places */
CREATE TABLE "places"
(
    "place_id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    "row"      char    NOT NULL,
    "number"   smallint     NOT NULL,
    "type"     varchar NOT NULL DEFAULT 'NORMAL',
    "status"   varchar NOT NULL DEFAULT 'AVAILABLE'
);


/* Table reservations */
CREATE TABLE "reservations"
(
    "reservation_id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    "employee_id"    uuid    NOT NULL,
    "place_id"       uuid    NOT NULL,
    "start_date"     date    NOT NULL,
    "end_date"       date    NOT NULL,
    "is_checked_in"  boolean NOT NULL,
    FOREIGN KEY ("employee_id") REFERENCES "employees" ("employee_id"),
    FOREIGN KEY ("place_id") REFERENCES "places" ("place_id")
);

