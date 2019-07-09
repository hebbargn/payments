package com.mejesticpay.paymentbase;

import java.util.List;

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
