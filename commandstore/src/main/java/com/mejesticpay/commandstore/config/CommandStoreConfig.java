package com.mejesticpay.commandstore.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mejesticpay.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CommandStoreConfig
{
    @Bean
    @Primary
    public ObjectMapper getObjectMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerSubtypes(new NamedType(DebitEnrichment.class,"DebitEnrichment"));
        mapper.registerSubtypes(new NamedType(CreditEnrichment.class,"CreditEnrichment"));
        mapper.registerSubtypes(new NamedType(FraudCheckInfo.class,"FraudCheckInfo"));
        mapper.registerSubtypes(new NamedType(SanctionsCheckInfo.class,"SanctionsCheckInfo"));
        mapper.registerSubtypes(new NamedType(AccountingInfo.class,"AccountingInfo"));
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
