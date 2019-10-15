package com.mejesticpay.stp;

import com.mejesticpay.paymentbase.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class STPInboundData
{
    /** Same payment which was part of InboundService Data need to be passed **/

    private Payment payment;

    private ServiceData serviceData;
    private String serviceStation;
    private List<AuditEntry> auditEntries = new ArrayList<>();
    private PROCESSING_RESULT result;

    public STPInboundData(Payment payment, ServiceData serviceData,String serviceStation)
    {
        this.payment = payment;
        this.serviceData = serviceData;
        this.serviceStation = serviceStation;
    }
    public STPInboundData(Payment payment, String serviceStation)
    {
        this.payment = payment;
        this.serviceStation = serviceStation;
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
