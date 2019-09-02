package com.mejesticpay.mysqlstore.mysql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.time.Instant;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Audit
{
    private String message;
    private Instant instant;
    private String jsonData;
    private String serviceName;
    private boolean processed;

    Audit(String service,Instant instant, String message, String jsonData)
    {
        this.serviceName = service;
        this.message = message;
        this.instant = instant;
        this.jsonData = jsonData;
    }

}
