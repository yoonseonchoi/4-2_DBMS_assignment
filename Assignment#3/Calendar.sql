create table users (
uid varchar(50) primary key,
pw varchar(50) not null,
name varchar(100) not null);

create sequence eid_seq;

create table events (
eid varchar(10) primary key default 'e' || nextval('eid_seq')::text,
title varchar(50) not null,
s_date date not null,
e_date date not null,
s_time time,
e_time time,
location varchar(50));

create table generates (
uid varchar(50) not null,
eid varchar(10) not null,
foreign key (uid) references users(uid),
foreign key (eid) references events(eid));

create sequence rid_seq;

create table reminds (
rid varchar(10) primary key default 'r' || nextval('rid_seq')::text,
frame integer not null,
interval integer not null,
eid varchar(10) not null.
foreign key (eid) references events(eid));