package com.mejesticpay.paymentrouter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Track
{
    String name;
    Map<String,Station> stations = new HashMap<>();

    public void addStation(String stationName, Station station)
    {
        stations.put(stationName, station);
    }
}
