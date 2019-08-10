package com.mejesticpay.mysqlstore.mysql;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<PaymentWrapper,String>
{
    public static final String FIND_PAYMENT_HEADERS = "select payment_ref as paymentReference,source, branch, JSON_EXTRACT(json_genesis,\"$.settlementAmount\") as amount,JSON_EXTRACT(json_genesis,\"$.settlementCurrency\") as currency, station as service, \n" +
            "JSON_EXTRACT(json_genesis,\"$.settlementDate\") as settlementDate,\n" +
            "JSON_EXTRACT(json_genesis,\"$.debtorAgent.clrSysMmbId\") as debtorBank,\n" +
            "JSON_EXTRACT(json_genesis,\"$.debtorAccount\") as debtorAccount,\n" +
            "JSON_EXTRACT(json_genesis,\"$.creditorAgent.clrSysMmbId\") as creditorBank,\n" +
            "JSON_EXTRACT(json_genesis,\"$.creditorAccount\") as creditorAccount from payment;";

    @Query(value = FIND_PAYMENT_HEADERS, nativeQuery = true)
    public List<Object[]> findPaymentList();

}

