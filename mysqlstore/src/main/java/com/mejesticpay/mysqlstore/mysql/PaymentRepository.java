package com.mejesticpay.mysqlstore.mysql;


import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentWrapper,String>
{
}

