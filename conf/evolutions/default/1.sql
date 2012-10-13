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

create sequence color_survey_id_seq;
create table color_survey (
    id integer not null default nextval('color_survey_id_seq') primary key,
    user_id integer not null,
    a1 varchar(15) not null,
    a2 varchar(400) not null,
    foreign key (user_id) references user(id)

);

# --- !Downs
drop table user;
drop sequence user_id_seq;
drop table color_survey;
drop sequence color_survey_id_seq;
