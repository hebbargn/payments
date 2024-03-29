package com.mejesticpay.paymentbase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuditEntry
{
    private String serviceName;
    private String message;
    private Instant instant;
    private String jsonData;
    private boolean processed;

    public AuditEntry(String serviceName, String message, String details)
    {
        this.serviceName = serviceName;
        instant = Instant.now();
        this.message = message;
        jsonData = details;
    }

    public AuditEntry(Instant instant,String serviceName, String message, String details)
    {
        this.serviceName = serviceName;
        this.instant = instant;
        this.message = message;
        jsonData = details;
    }
}
