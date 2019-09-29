package com.mejesticpay.mysqlstore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@MappedSuperclass
public abstract class CommonBase
{
    public enum DBACTION
    {
        INSERT,
        UPDATE
    }
    public static final String VERSION_COL = "version";

    @Version
    protected int version;
    private Instant createdTime;
    private Instant lastUpdatedTime;

    @Transient
    private Map<String,Object> updatedFields = new HashMap<>();

    @Transient
    private DBACTION dbAction;

    public void addUpdatedField(String fieldName, String fieldValue)
    {
        updatedFields.put(fieldName, fieldValue);
    }

}
