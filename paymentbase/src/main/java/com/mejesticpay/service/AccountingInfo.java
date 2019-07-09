package com.mejesticpay.service;

import com.mejesticpay.paymentbase.ServiceData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by nhebbar on 11/8/2018.
 */
public class AccountingInfo  implements ServiceData
{

    private List<Entry> entries;

    private class Entry
    {
        /* Valid Values are (D)DEBIT, (C)CREDIT */
        private String type;
        private String office;
        private String region;
        private String account;
        private String accountType;
        private BigDecimal amount;
        private String currency;
        private LocalDate valueDate;
        private LocalDate entryDate;
        private String domain;
        private String family;
        private String subFamily;
        private String status;
        private String customerParty;
        private String customerNumber;
        private String entity;
    }

}
