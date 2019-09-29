package com.mejesticpay.commands;

import com.mejesticpay.paymentbase.Genesis;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePaymentCommand implements Command
{
    private Genesis genesis;
    private String source;
    private String subSource;
    private String branch;

}
