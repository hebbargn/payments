create table customer (customer_number varchar(255) not null, address varchar(255), name varchar(100),
status varchar(100), primary key (customer_number)
);
create table account (account_number varchar(255) not null, state varchar(100), status varchar(100),
type varchar(100), description varchar(255), primary key (account_number)
);

create table customer_account ( customer_number varchar(255) not null, account_number varchar(255) not null,
primary key (customer_number,account_number) );


