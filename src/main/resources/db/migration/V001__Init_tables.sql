create table users(
  id varchar(100) primary key,
  username varchar(50) unique not null,
  password varchar(100) not null,
  enable bit(1) default 0
);

create table authorities(
  userId varchar(50) not null,
  authority varchar(10) not null
);