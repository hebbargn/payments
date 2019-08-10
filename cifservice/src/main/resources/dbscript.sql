create database cif;
create user 'cif_user' identified by 'secret';
grant all on cif.* to cif_user;


create table cif.customer (customer_number varchar(255) not null,  name varchar(100),
status varchar(100), street varchar(100) ,building_number varchar(25),postal_code varchar(10),town_name varchar(100),country varchar(2) , primary key (customer_number)
);
create table cif.account (name varchar(100),account_number varchar(255) not null, state varchar(100), status varchar(100),
type varchar(100), description varchar(255), primary key (account_number)
);

create table cif.customer_account ( customer_number varchar(255) not null, account_number varchar(255) not null,
primary key (customer_number,account_number) );
