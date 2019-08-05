create database configuration;
create user 'configuration_user' identified by 'secret';
grant all on configuration.* to configuration_user;

CREATE TABLE configuration.properties (application varchar(200),
  profile varchar(200) ,
  label varchar(200) ,
  keyname varchar(200) not null,
  keyvalue varchar(200) not null );

INSERT INTO configuration.properties (application, profile, label,keyname, keyvalue )
VALUES ('STPEngineService','dev','latest','SendPaymentToSTPEngine','PaymentRouter');