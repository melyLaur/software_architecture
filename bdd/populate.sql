INSERT INTO "employees" (employee_id, first_name, last_name)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'Jean', 'Dumont'),
    ('00000000-0000-0000-0000-000000000002','Laura','Dumont'),
    ('00000000-0000-0000-0000-000000000003','Luc','Morel'),
    ('00000000-0000-0000-0000-000000000004','Elise','Barbier'),
    ('00000000-0000-0000-0000-000000000005','Nora','Roche'),
    ('00000000-0000-0000-0000-000000000006','Etienne','Giraud'),
    ('00000000-0000-0000-0000-000000000007','Julie','Masson'),
    ('00000000-0000-0000-0000-000000000008','Pascal','Garnier'),
    ('00000000-0000-0000-0000-000000000009','Lina','Perrot'),
    ('00000000-0000-0000-0000-000000000010','Henri','Vidal');


-- Places row A (ELECTRICAL)
INSERT INTO places ("row", "number", "type")
VALUES ('A', 1, 'ELECTRICAL'),
       ('A', 2, 'ELECTRICAL'),
       ('A', 3, 'ELECTRICAL'),
       ('A', 4, 'ELECTRICAL'),
       ('A', 5, 'ELECTRICAL'),
       ('A', 6, 'ELECTRICAL'),
       ('A', 7, 'ELECTRICAL'),
       ('A', 8, 'ELECTRICAL'),
       ('A', 9, 'ELECTRICAL'),
       ('A', 10, 'ELECTRICAL');

-- Places row B (NORMAL)
INSERT INTO places ("row", "number")
VALUES ('B', 1),
       ('B', 2),
       ('B', 3),
       ('B', 4),
       ('B', 5),
       ('B', 6),
       ('B', 7),
       ('B', 8),
       ('B', 9),
       ('B', 10);

-- Places row C (NORMAL)
INSERT INTO places ("row", "number")
VALUES ('C', 1),
       ('C', 2),
       ('C', 3),
       ('C', 4),
       ('C', 5),
       ('C', 6),
       ('C', 7),
       ('C', 8),
       ('C', 9),
       ('C', 10);

-- Places row D (NORMAL)
INSERT INTO places ("row", "number")
VALUES ('D', 1),
       ('D', 2),
       ('D', 3),
       ('D', 4),
       ('D', 5),
       ('D', 6),
       ('D', 7),
       ('D', 8),
       ('D', 9),
       ('D', 10);

-- Places row E (NORMAL)
INSERT INTO places ("row", "number")
VALUES ('E', 1),
       ('E', 2),
       ('E', 3),
       ('E', 4),
       ('E', 5),
       ('E', 6),
       ('E', 7),
       ('E', 8),
       ('E', 9),
       ('E', 10);

-- Places row F (ELECTRICAL)
INSERT INTO places ("row", "number", "type")
VALUES ('F', 1, 'ELECTRICAL'),
       ('F', 2, 'ELECTRICAL'),
       ('F', 3, 'ELECTRICAL'),
       ('F', 4, 'ELECTRICAL'),
       ('F', 5, 'ELECTRICAL'),
       ('F', 6, 'ELECTRICAL'),
       ('F', 7, 'ELECTRICAL'),
       ('F', 8, 'ELECTRICAL'),
       ('F', 9, 'ELECTRICAL'),
       ('F', 10, 'ELECTRICAL');
