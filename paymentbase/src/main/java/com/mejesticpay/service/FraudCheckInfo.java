package com.mejesticpay.service;

import com.mejesticpay.paymentbase.ServiceData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString @NoArgsConstructor
public class FraudCheckInfo  implements ServiceData
{
    private String fraudCheckStatus;
    private String fraudCheckData;
    private LocalDate fraudCheckDate;

}
