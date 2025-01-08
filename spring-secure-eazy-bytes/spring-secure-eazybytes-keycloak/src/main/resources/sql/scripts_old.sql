-- If you is using Dbeaver you can select you script and press ALT+X

show tables;
describe users;
describe authorities;
describe customer;

create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);

CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `pwd` varchar(200) NOT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `customerdetails` (
  `email` varchar(45) NOT NULL,
  `city` varchar(50),
  `desc` varchar(200),
  PRIMARY KEY (`email`)
);


delete from authorities a;
delete from users u ;
delete from customer c ;

SELECT * from authorities a;
select * from users u ;
select * from customer c ;
select * from customerdetails c ;

update users set password = '{noop}manage' where username = 'user'
update users set password = '$2a$12$VFcMWuiAD6y1Ikv2WvHaj.lVXByJZnGduin1KnuOR7z1ijmLq4tIW' where username = 'admin'

insert into users values ('user', '{noop}manage', '1');
insert into authorities values ('user', 'read');

insert into users values ('admin', '{bcrypt}$2a$12$E/4SOkD5hdOWb.PUfY3vhuR1RHqPtfhOe7T8if/njkpAiG3ig8StO', '1');
insert into authorities values ('admin', 'admin');

insert into customer (`email`, `pwd`, `role`) values ('admin@example.com','{bcrypt}$2a$12$E/4SOkD5hdOWb.PUfY3vhuR1RHqPtfhOe7T8if/njkpAiG3ig8StO','admin');
insert into customer (`email`, `pwd`, `role`) values ('happy@example.com','{noop}managehappy','read');

insert into customerdetails (`email`, `city`, `desc`) values ('admin@example.com','New York','customer buys a lot');
insert into customerdetails (`email`, `city`, `desc`) values ('happy@example.com','London','customer buys less');