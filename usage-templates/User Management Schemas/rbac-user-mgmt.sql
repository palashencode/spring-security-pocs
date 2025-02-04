
show tables;
create table users(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username varchar(50) not null UNIQUE,
    password varchar(500) not null,
    firstname varchar(100),
    lastname varchar(100),
    active boolean not null
    ) AUTO_INCREMENT = 10000;

create table users_meta(
    user_id INT PRIMARY KEY,
    phone_no varchar(50),
    profile_pic_url VARCHAR(500),
    bio varchar(500)
    );

create table users_resetlink(
    user_id INT PRIMARY KEY,
    reset_code varchar(500),
    issue_time TIMESTAMP,
    expire_time TIMESTAMP,
    active boolean not null
    );

create table roles (
	role varchar(50) not null PRIMARY KEY,
    description varchar(500),
    active boolean not null
    );

create table user_role_mapping(
    user_id INT not null,
    role varchar(50) not null
    );

-- general audit logs
create table auditlogs(
	audit_id INT AUTO_INCREMENT PRIMARY KEY,
	user_id INT,
	event_type varchar(100),
	event_data varchar(500),
	event_time TIMESTAMP
) AUTO_INCREMENT = 100;


insert into users (username,password, firstname, lastname, active) values ('user', '{noop}manage','John','Baker', true);
insert into users (username,password, firstname, lastname, active) values ('admin', '{noop}manage','Jane','Brown', true);

insert into users_meta values (10000, "+986755433234", "aws:path/tp/image.jpg", "this is a bio")
insert into users_meta values (10001, "+919766544234", "aws:path/tp/image.jpg", "this is a bio")


insert into roles values ('ROLE_USER', 'Basic role so that user can read secure pages.', true);
insert into roles values ('ROLE_ADMIN', 'Admin role for secure administration pages.', true);

insert into user_role_mapping values (10000, 'ROLE_USER');
insert into user_role_mapping values (10001, 'ROLE_ADMIN');

update users set username='john@example.com' where user_id=10000;
update users set username='jane@example.com' where user_id=10001;

select * from users;
select * from users_meta;
select * from roles;
select * from user_role_mapping;
select * from users_resetlink;
