package com.mejesticpay.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mejesticpay.service.*;

import java.io.IOException;

public class JSONHelper
{
    private static ObjectMapper mapper = null;
    static
    {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerSubtypes(new NamedType(DebitEnrichment.class,"DebitEnrichment"));
        mapper.registerSubtypes(new NamedType(CreditEnrichment.class,"CreditEnrichment"));
        mapper.registerSubtypes(new NamedType(FraudCheckInfo.class,"FraudCheckInfo"));
        mapper.registerSubtypes(new NamedType(SanctionsCheckInfo.class,"SanctionsCheckInfo"));
        mapper.registerSubtypes(new NamedType(AccountingInfo.class,"AccountingInfo"));
        mapper.registerModule(new JavaTimeModule());

    }


    public static String convertToStringFromObject(Object object) throws JsonProcessingException
    {
        return mapper.writeValueAsString(object);
    }

    public static <T> T convertToObjectFromJson(String json, Class<T> theClass)throws IOException
    {
        return mapper.readValue(json,theClass);
    }

    public static String convertToPrettyStringFromObject(Object object)throws JsonProcessingException
    {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}
