package com.mejesticpay.mysqlstore.mysql;

import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.paymentbase.Genesis;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.CreditEnrichment;
import com.mejesticpay.service.DebitEnrichment;
import com.mejesticpay.service.FraudCheckInfo;
import com.mejesticpay.util.JSONHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name="payment")
public class PaymentWrapper
{
    @Id
    private String paymentRef;

    //Transaction
    private String state;
    private String status;
    private String station;
    private String track;
    private int version;

    private String source;
    private String branch;


    //Service Data
    private String json_genesis;
    private String json_debit_enrich;
    private String json_credit_enrich;
    private String json_fraud_check;

    // System Data
    private Instant created_time;
    private Instant last_updated_time;
    private String  created_by;
    private String  last_updated_by;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="audit_entries", joinColumns = @JoinColumn(name="paymentid"))
    private Set<Audit> auditEntries = new HashSet<>();

    public PaymentWrapper( Payment payment)
    {
        paymentRef = payment.getPaymentIdentifier();
        state = payment.getState();
        status = payment.getStatus();
        station = payment.getStation();
        track = payment.getTrack();
        version = payment.getVersion();
        source = payment.getSource();
        branch = payment.getBranch();
        if(payment.getCreatedTime() == null)
            created_time = Instant.now();
        created_by = payment.getCreatedBy();

        try
        {
            json_genesis = JSONHelper.convertToStringFromObject(payment.getGenesis());
            json_debit_enrich = JSONHelper.convertToStringFromObject(payment.getDebitEnrichment());
            json_credit_enrich = JSONHelper.convertToStringFromObject(payment.getCreditEnrichment());
            json_fraud_check = JSONHelper.convertToStringFromObject(payment.getFraudCheckInfo());
            addAudits(payment.getAuditEntries());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void addAudits(List<AuditEntry> audits)
    {
        if(audits != null)
        {
            for(AuditEntry ae: audits)
            {
                if(ae.isProcessed())
                    continue;

                Audit audit = new Audit(ae.getServiceName(),ae.getInstant(), ae.getMessage(),ae.getJsonData());
                audit.setProcessed(true);
                auditEntries.add(audit);
            }
        }
    }
    public Payment getPayment()
    {
        PaymentImpl payment = new PaymentImpl();
        payment.setPaymentIdentifier(paymentRef);
        payment.setState(state);
        payment.setStatus(status);
        payment.setTrack(track);
        payment.setStation(station);
        payment.setSource(source);
        payment.setBranch(branch);

        try
        {
            payment.setGenesis(JSONHelper.convertToObjectFromJson(json_genesis, Genesis.class));
            payment.setDebitEnrichment(JSONHelper.convertToObjectFromJson(json_debit_enrich, DebitEnrichment.class));
            payment.setCreditEnrichment(JSONHelper.convertToObjectFromJson(json_credit_enrich, CreditEnrichment.class));
            payment.setFraudCheckInfo(JSONHelper.convertToObjectFromJson(json_fraud_check, FraudCheckInfo.class));

            for(Audit audit: auditEntries)
            {
                AuditEntry auditEntry = new AuditEntry(audit.getInstant(), audit.getServiceName(),audit.getMessage(),audit.getJsonData());
                auditEntry.setProcessed(audit.isProcessed());
                payment.addAuditEntry(auditEntry);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return payment;
    }


    public void updatePayment(PaymentImpl payment)
    {
        state = payment.getState();
        status = payment.getStatus();
        station = payment.getStation();
        track = payment.getTrack();
        version = version+1;
        last_updated_time = Instant.now();
        last_updated_by = payment.getLastUpdatedBy();
        created_by = payment.getCreatedBy();
        source = payment.getSource();
        branch = payment.getBranch();

        try
        {
            if(payment.getGenesis() != null)
                json_genesis = JSONHelper.convertToStringFromObject(payment.getGenesis());
            if(payment.getDebitEnrichment() != null)
                json_debit_enrich = JSONHelper.convertToStringFromObject(payment.getDebitEnrichment());
            if(payment.getCreditEnrichment() != null)
                json_credit_enrich = JSONHelper.convertToStringFromObject(payment.getCreditEnrichment());
            if(payment.getFraudCheckInfo() != null)
                json_fraud_check = JSONHelper.convertToStringFromObject(payment.getFraudCheckInfo());

            addAudits(payment.getAuditEntries());

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
