package com.mejesticpay.paymentbase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString @NoArgsConstructor
public class ServiceFeed
{
    private InFlightTransactionInfo inFlightTransactionInfo;
    private ServiceData serviceData;
    private List<AuditEntry> auditEntries = new ArrayList<>();
    private PROCESSING_RESULT result;

    public ServiceFeed(InFlightTransactionInfo inFlightTransactionInfo, ServiceData serviceData)
    {
        this.inFlightTransactionInfo = inFlightTransactionInfo;
        this.serviceData = serviceData;
    }
    public ServiceFeed(InFlightTransactionInfo inFlightTransactionInfo)
    {
        this.inFlightTransactionInfo = inFlightTransactionInfo;
    }

    public  void addAuditEntry(AuditEntry auditEntry)
    {
        auditEntries.add(auditEntry);
    }

    public enum PROCESSING_RESULT
    {
        SUCCESS,
        FAILURE,
        HOLD,
        STATECHANGE
    }
}
