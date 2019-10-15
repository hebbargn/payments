create database commandstore;
create user 'paymenttest' identified by 'secret';
grant all on commandstore.* to paymenttest;

drop table commandstore.payment_events;
drop table commandstore.audit_entries;
drop table commandstore.credit_enrich;
drop table commandstore.debit_enrich;
drop table commandstore.payment_origination;



create table commandstore.payment_events (
 payment_ref varchar(255) not null
,version int not null
,branch varchar(255) not null
,station varchar(255) not null
,state varchar(255) not null
,status varchar(255) not null
,track varchar(255) not null
,created_time timestamp
,PRIMARY KEY (payment_ref,version)
);

create table commandstore.audit_entries(
 id int NOT NULL AUTO_INCREMENT
,payment_ref varchar(255) not null
,service_name varchar(255)
,message varchar(255)
,instant timestamp
,json_data varchar(1024)
,created_time timestamp
,PRIMARY KEY(id)
);

create table commandstore.credit_enrich(
 payment_ref varchar(255) not null
 ,version int
 ,credit_value_date date
 ,credit_account varchar(255)
 ,json_credit_enrich varchar(1024)
 ,credit_settlement_date date
 ,created_time timestamp
 ,PRIMARY KEY (payment_ref,version)
);

create table commandstore.debit_enrich(
 payment_ref varchar(255) not null
 ,version int
 ,debit_value_date date
 ,debit_account varchar(255)
 ,json_debit_enrich varchar(1024)
 ,debit_settlement_date date
 ,created_time timestamp
 ,PRIMARY KEY (payment_ref,version)
);

create table commandstore.payment_origination(
 payment_reference varchar(255) not null
 ,version int
 ,json_genesis varchar(2018)
 ,source varchar(254)
 ,created_time timestamp
 ,PRIMARY KEY (payment_reference)
);

create table commandstore.complete_payment(
 payment_ref varchar(255) not null
 ,version int
 ,complete_date date
 ,json_complete_payment varchar(1024)
 ,created_time timestamp
 ,PRIMARY KEY (payment_ref,version)
);


create table commandstore.fraud_check(
 payment_ref varchar(255) not null
 ,version int
 ,fraud_check_date date
 ,fraud_check_status varchar(255)
 ,json_fraud_check varchar(1024)
 ,created_time timestamp
 ,PRIMARY KEY (payment_ref,version)
);
