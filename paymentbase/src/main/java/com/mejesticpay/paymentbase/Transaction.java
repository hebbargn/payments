package com.mejesticpay.paymentbase;

import java.time.Instant;

public interface Transaction
{
    public String getState();

    public String getStatus();

    public Instant getCreatedTime();

    public Instant getLastUpdatedTime();

    public String getCreatedBy();

    public String getLastUpdatedBy();

    public int getVersion();

    public String getTrack();

    public String getStation();

    public void setStation(String station);

    public void incrementVersion();


}
