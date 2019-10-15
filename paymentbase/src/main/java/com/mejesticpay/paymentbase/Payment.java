package com.mejesticpay.paymentbase;



import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mejesticpay.paymentfactory.PaymentImpl;

import java.util.List;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.EXISTING_PROPERTY, defaultImpl = PaymentImpl.class)

public interface Payment extends Transaction
{
    Genesis getGenesis();

    String getSource();

    String getSubSource();

    String getPaymentIdentifier();

    String getBranch();

    ServiceData getCreditEnrichment();

    ServiceData getDebitEnrichment();

    ServiceData getRouteData();

    ServiceData getFundServiceInfo();

    ServiceData getSanctionsCheckInfo();

    ServiceData getAccountingInfo();

    ServiceData getFraudCheckInfo();

    List<AuditEntry> getAuditEntries();
}
