select host, user from mysql.user; /*mysql DB 안에 존재하는 user Table 대상*/

create database db_b202012055;
show tables;



/*SQL DDL*/
create table t_mb202012055(
                              mid bigint auto_increment PRIMARY KEY not null,
                              email varchar(30) not null ,
                              full_name varchar(30) not null,
                              pw varchar(30) not null,
                              zipcode varchar(5)
);

insert into t_mb202012055(fullname, email, pw) values ('ckj', 'ckj@induk.ac.kr', 'cometrue');

select * from t_mb202012055;

delete from t_mb202012055 where mid = 1;
select host,user from mysql.user;

show tables;
drop table t_mb202012055;
drop database db_b202012055;

create user 'u_b202012055'@'%' identified by 'cometrue';

/* db_b202012055라는 DB만(상위 mysql DB도 불가능) 사용 가능한 권한 할당*/
grant all privileges on db_b202012055.* to 'u_b202012055'@'%';
flush privileges;

show tables;
/*Data Definition Language, 데이터베이스, 테이블을 정의하는데 사용하는 언어*/
create table t_mb202012055(
                              mid bigint auto_increment PRIMARY KEY not null,
                              full_name varchar(30) not null,
                              email varchar(30) not null ,
                              pw varchar(30) not null,
                              zipcode varchar(5)
);

insert into t_mb202012055(full_name, email, pw) values ('Admin', 'admin@induk.ac.kr', 'cometrue');
insert into t_mb202012055(full_name, email, pw) values ('ckj', 'ckj@induk.ac.kr', 'cometrue');
insert into t_mb202012055(full_name, email, pw) values ('asd', 'asd@induk.ac.kr', 'cometrue');
select * from t_mb202012055;

update t_mb202012055 SET mid not null;