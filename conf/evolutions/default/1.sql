# Tasks schema

# --- !Ups
create sequence user_id_seq;
create table user (
    id integer not null default nextval('user_id_seq') primary key,
    fname varchar(20) not null,
    lname varchar(20) not null,
    email varchar(30) not null unique,
    password varchar(20) not null
);

# --- !Downs
drop table if exists user;
