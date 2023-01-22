create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint not null references client(id)
);

create table address
(
    id   bigserial not null primary key,
    street varchar(50),
    client_id bigint not null references client(id)
);
