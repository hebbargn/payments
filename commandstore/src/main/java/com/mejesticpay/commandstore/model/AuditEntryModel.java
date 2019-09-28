package com.mejesticpay.commandstore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name="audit_entries")
@NoArgsConstructor
public class AuditEntryModel  implements Persistable<Long>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paymentRef;
    private String message;
    private Instant instant;
    private String jsonData;
    private String serviceName;

    public boolean isNew()
    {
        return true;
    }

    public Long getId()
    {
        return id;
    }

    public  AuditEntryModel(String serviceName, String paymentRef,String message,String jsonData)
    {
        this.serviceName = serviceName;
        this.paymentRef = paymentRef;
        this.message = message;
        this.jsonData = jsonData;
        this.instant = Instant.now();

    }
}
