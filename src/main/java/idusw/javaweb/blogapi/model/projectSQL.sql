drop table t_prjb202012055;

create table t_prjb202012055 (
                                 pid bigint auto_increment primary key,
                                 project_name varchar(30) not null,
                                 project_description varchar(200),
                                 status varchar(15),
                                 client_company varchar(30),
                                 project_leader varchar(30),
                                 estimated_budget bigint,
                                 total_amount_spent bigint,
                                 estimated_project_duration bigint,
                                 project_image varchar(50),
                                 reg_timestamp TIMESTAMP DEFAULT NOW(),
                                 rev_timestamp TIMESTAMP DEFAULT NOW()
);

/* C.R.U.D - insert, select, update, delete 구문으로 처리 */
insert into t_prjb202012055(project_name, status) values('DCT-3 Project', 'On Hold'); /* On Hold, Canceled, Success */
insert into t_prjb202012055(project_name, status, project_leader) values('Comso Project', 'Success', 'b202012055 조경준');
insert into t_prjb202012055(project_name, status, project_leader) values('Induk Project', 'On Hold', 'induk');
insert into t_prjb202012055(project_name, status, project_leader) values('Seoul Project', 'Canceled', 'seoul');
insert into t_prjb202012055(project_name, status, project_leader) values('SW Project', 'On Hold', 'SW');

drop table if exists t_prjb202012055;
select * from t_prjb202012055 where pid = 1;
select * from t_prjb202012055;
update t_prjb202012055 set project_image = '02.jpg' where pid = 6;
delete from t_prjb202012055 where pid = 1;