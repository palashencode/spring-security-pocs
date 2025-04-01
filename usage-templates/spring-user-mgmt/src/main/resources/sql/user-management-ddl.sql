
show tables;
create table users(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username varchar(50) not null UNIQUE,  	-- email
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

-- password reset table
create table password_reset(
	id INT AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL UNIQUE,
	token VARCHAR(255) NOT NULL UNIQUE,
	created_date TIMESTAMP NOT NULL,
	expiry_date TIMESTAMP NOT NULL
);


create table roles (
	role varchar(50) not null PRIMARY KEY,
    description varchar(500),
    active boolean not null
    );

create table user_role_mapping(
	mapping_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT not null,
    role varchar(50) not null
    );

-- persistent remember-me token table
create table persistent_logins (
    username varchar(64) NOT NULL,          -- The username of the authenticated user
    series varchar(64) NOT NULL PRIMARY KEY, -- A unique identifier for the series of tokens
    token varchar(64) NOT NULL,             -- The token value for authentication
    last_used TIMESTAMP NOT NULL            -- Timestamp for the last usage of the token
);

-- general audit logs
create table auditlogs(
	audit_id INT AUTO_INCREMENT PRIMARY KEY,
	user_id INT,
	event_type varchar(100),
	event_data varchar(500),
	event_time TIMESTAMP
) AUTO_INCREMENT = 100;

-- Next two tables are optional for RBAC
create table permissions (
	permission varchar(100) not null PRIMARY KEY,
    description varchar(500),
    active boolean not null
    );

create table role_permission_mapping (
	role varchar(50) not null,
	permission varchar(100) not null,
	FOREIGN KEY (permission) REFERENCES permissions(permission),
	FOREIGN KEY (role) REFERENCES roles(role)
    );

create table users_data_para(
    entry_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    data_entry varchar(500),
    last_modified_at TIMESTAMP
    );

create table users_data_list(
    entry_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    sort_order INT,
    entry varchar(255),
    created_at TIMESTAMP
    );

--- Register, Pending Validation
create table login_magic_link(
    magic_link_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT not null UNIQUE,
    username varchar(50) not null UNIQUE,  	-- email
    login_token VARCHAR(255) NOT NULL UNIQUE,
	created_date TIMESTAMP NOT NULL,
	expiry_date TIMESTAMP NOT NULL
    ) AUTO_INCREMENT = 100;

--- Register, Pending Validation
create table pending_user_verification(
    register_user_id INT AUTO_INCREMENT PRIMARY KEY,
    username varchar(50) not null UNIQUE,  	-- email
    password varchar(500) not null,
    verification_token VARCHAR(255) NOT NULL UNIQUE,
    registration_time TIMESTAMP,
    verification_time TIMESTAMP, 
    verified boolean not null
    ) AUTO_INCREMENT = 100;


insert into users (username,password, firstname, lastname, active) values ('user', '{noop}manage','John','Baker', true);
insert into users (username,password, firstname, lastname, active) values ('admin', '{noop}manage','Jane','Brown', true);
insert into users (username,password, firstname, lastname, active) values ('joe@example.com', '{noop}manage','Joe','Brooks', true);

insert into users_meta values (10000, "+986755433234", "aws:path/tp/image.jpg", "this is a bio");
insert into users_meta values (10001, "+919766544234", "aws:path/tp/image.jpg", "this is a bio");
insert into users_meta values (10002, "+919766544234", "aws:path/tp/image.jpg", "this is a bio");

insert into roles values ('ROLE_USER', 'Basic role so that user can read secure pages.', true);
insert into roles values ('ROLE_ADMIN', 'Admin role for secure administration pages.', true);

insert into user_role_mapping(user_id, role) values (10000, 'ROLE_USER');
insert into user_role_mapping(user_id, role) values (10001, 'ROLE_ADMIN');
insert into user_role_mapping(user_id, role) values (10001, 'ROLE_USER');
insert into user_role_mapping(user_id, role) values (10002, 'ROLE_USER');

update users set username='john@example.com' where user_id=10000;
update users set username='jane@example.com' where user_id=10001;
update users set password='{noop}manage' where user_id=10002;

insert into users_data_list (user_id, sort_order, entry, created_at) values (10000, 0,'This John\'s data',NOW());
insert into users_data_list (user_id, sort_order, entry, created_at) values (10000, 1,'John\'s reminder 1',NOW());
insert into users_data_list (user_id, sort_order, entry, created_at) values (10000, 2,'John\'s reminder 2',NOW());
insert into users_data_list (user_id, sort_order, entry, created_at) values (10000, 3,'John, don\'t forget groceries 3',NOW());

insert into users_data_list (user_id, sort_order, entry, created_at) values (10001, 0,'This Jane\'s data',NOW());
insert into users_data_list (user_id, sort_order, entry, created_at) values (10001, 1,'Jane\'s reminder 1',NOW());
insert into users_data_list (user_id, sort_order, entry, created_at) values (10001, 2,'Jane\'s reminder 2',NOW());
insert into users_data_list (user_id, sort_order, entry, created_at) values (10001, 3,'Jane, don\'t forget groceries 3',NOW());

insert into users_data_list (user_id, sort_order, entry, created_at) values (10002, 0,'This Joe\'s data',NOW());
insert into users_data_list (user_id, sort_order, entry, created_at) values (10002, 1,'Joe\'s reminder 1',NOW());
insert into users_data_list (user_id, sort_order, entry, created_at) values (10002, 2,'Joe\'s reminder 2',NOW());
insert into users_data_list (user_id, sort_order, entry, created_at) values (10002, 3,'Joe, don\'t forget groceries 3',NOW());

select * from users_data_list where user_id=10002

select * from users;
select * from users_meta;
select * from roles;
select * from user_role_mapping;
select * from persistent_logins;
select * from password_reset;
select * from users_data_list;
select * from login_magic_link;

select * from users;
select * from user_role_mapping;
select * from pending_user_verification;

delete from login_magic_link;
delete from pending_user_verification;
delete from users where username = 'pnandi@gmail.com'

delete from users where user_id = 10006;
delete from user_role_mapping where user_id = 10006;

update user_role_mapping set role="ROLE_ADMIN" where user_id=10007

describe password_reset;



show tables;

select * from login_magic_link;
select * from persistent_logins pl; 
select * from users_meta;
select * from role_permission_mapping rpm ;
select * from users;
select * from users_data_list udl ;
select * from permissions;


