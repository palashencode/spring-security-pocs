
create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);

delete from authorities a
delete from users u

SELECT * from authorities a
select * from users u


insert into users values ('user', '{noop}manage', '1');
insert into authorities values ('user', 'read');

insert into users values ('admin', '{bcrypt}$2a$12$E/4SOkD5hdOWb.PUfY3vhuR1RHqPtfhOe7T8if/njkpAiG3ig8StO', '1');
insert into authorities values ('admin', 'admin');