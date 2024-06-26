SET schema 'coffee';

DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS volume CASCADE;
DROP TABLE IF EXISTS drink CASCADE;
DROP TABLE IF EXISTS cake CASCADE;
DROP TABLE IF EXISTS coffee_point CASCADE;
DROP TABLE IF EXISTS upcoming_order CASCADE;
DROP TABLE IF EXISTS history_order CASCADE;

create table coffee.role (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    role VARCHAR
);

INSERT INTO role(role)
VALUES ('ROLE_CLIENT'), ('ROLE_MANAGER'), ('ROLE_OWNER');

create table coffee.person (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR,
    surname VARCHAR,
    telephone_number VARCHAR,
    password VARCHAR,
    id_role INT REFERENCES role(id)
);

INSERT INTO coffee.person(name, surname, telephone_number, password, id_role)
VALUES ('Антон', 'Чугунов', '89779735740', 'anton', 1),
       ('Татьяна', 'Ершова', '81231231212', 'tanya', 3),
       ('Иван', 'Иванов', '80000000000', 'man', 2),
       ('Алексей', 'Алексеев', '89999999999', 'alesha', 1);

create table coffee.volume (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    volume VARCHAR
);

INSERT INTO volume(volume) VALUES ('200'), ('300'), ('400');

create table coffee.drink (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR,
    id_volume INT REFERENCES volume(id),
    price FLOAT
);

INSERT INTO drink(name, id_volume, price)
VALUES ('Латте', '1', '110'), ('Латте', '2', '210'), ('Латте', '3', '310'),
       ('Капучино', '1', '100'), ('Капучино', '2', '200'), ('Капучино', '3', '300'),
       ('Американо', '1', '90'), ('Американо', '2', '190'), ('Американо', '3', '290');

create table coffee.cake (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR,
    price FLOAT
);

INSERT INTO cake(name, price)
VALUES ('Маффин шоколадный', 120.0), ('Тарталетка с малиной', 150.0),
       ('Миндальный круассан', 130.0), ('Пончик', 70.0);

CREATE TABLE coffee.coffee_point (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    address VARCHAR,
    manager_name VARCHAR,
    telephone_number VARCHAR
);

INSERT INTO coffee_point(address, manager_name, telephone_number)
VALUES ('улица Институтская, д.1', 'Андрей', '81234567890'), ('улица Бауманская, д.2', 'Иван', '81234567890');

CREATE TABLE coffee.history_order (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    id_person INT REFERENCES person(id),
    id_coffee_point INT REFERENCES coffee_point(id),
    id_drink INT REFERENCES drink(id),
    id_cake INT REFERENCES cake(id),
    order_amount FLOAT,
    date timestamp
);

INSERT INTO history_order(id_person, id_coffee_point, id_drink, id_cake, order_amount, date)
VALUES (1, 1, 1, 1, 120.0, '1-1-2024'), (2, 2, 2, 2, 150.0, '1-1-2024');

CREATE TABLE coffee.upcoming_order (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    id_person INT REFERENCES person(id),
    id_coffee_point INT REFERENCES coffee_point(id),
    id_drink INT REFERENCES drink(id),
    id_cake INT REFERENCES cake(id),
    order_amount FLOAT,
    date timestamp
);

INSERT INTO upcoming_order(id_person, id_coffee_point, id_drink, id_cake, order_amount, date)
VALUES (2, 2, 2, 2, 150.0, '1-1-2024');

INSERT INTO upcoming_order(id_person, id_coffee_point, id_drink, id_cake, order_amount, date)
VALUES (2, 2, null, 2, 200.0, '1-1-2024');
