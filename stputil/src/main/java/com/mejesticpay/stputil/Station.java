package com.mejesticpay.stputil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Station
{
    private String name;
    private String onSuccess;
    private String onHold;
    private String onFailure;
    private String onStateChange;
}
