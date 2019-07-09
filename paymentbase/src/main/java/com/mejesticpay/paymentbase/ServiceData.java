package com.mejesticpay.paymentbase;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mejesticpay.service.*;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use=NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoutePayment.class, name="RoutePayment"),
        @JsonSubTypes.Type(value = CreditEnrichment.class, name="CreditEnrichment"),
        @JsonSubTypes.Type(value = DebitEnrichment.class, name="DebitEnrichment"),
        @JsonSubTypes.Type(value = FraudCheckInfo.class, name="FraudCheckInfo"),
        @JsonSubTypes.Type(value = SanctionsCheckInfo.class, name="SanctionsCheckInfo")

}

)
public interface ServiceData
{
}
