package com.mejesticpay.apigateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mejesticpay.commands.CreatePaymentCommand;
import com.mejesticpay.paymentbase.Genesis;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentbase.ServiceFeed;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.RoutePayment;
import com.mejesticpay.stp.STPInboundData;
import com.mejesticpay.util.JSONHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PaymentOriginationGateway
{
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    RestTemplate restTemplate = new RestTemplate();


    @PostMapping("/PaymentGateway")
    public Payment processPayment(@RequestBody Genesis origination)
    {
        CreatePaymentCommand command = new CreatePaymentCommand();
        command.setGenesis(origination);
        command.setBranch("US");
        command.setSubSource("East");
        HttpEntity<CreatePaymentCommand> request = new HttpEntity(command);

        Payment payment = restTemplate.postForObject("http://localhost:8096/commands/createpayment", request, PaymentImpl.class);

        STPInboundData inbound = new STPInboundData(payment, new RoutePayment(), "New");
        inbound.setResult(STPInboundData.PROCESSING_RESULT.SUCCESS);

        try
        {
            kafkaTemplate.send("SendToSTPEngine", payment.getPaymentIdentifier(), JSONHelper.convertToStringFromObject(inbound));
        }catch(JsonProcessingException joe)
        {
            joe.printStackTrace();
        }
        return payment;
    }

}
