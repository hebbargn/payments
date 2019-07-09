package com.mejesticpay.apigateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mejesticpay.paymentbase.Genesis;
import com.mejesticpay.paymentbase.InFlightTransactionInfo;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentbase.ServiceFeed;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.RoutePayment;
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

        HttpEntity<Payment> request = new HttpEntity(origination);
        Payment payment = restTemplate.postForObject("http://localhost:8080/payments", request, PaymentImpl.class);

        ServiceFeed feed = new ServiceFeed(new InFlightTransactionInfo(payment,"New"), new RoutePayment());
        feed.setResult(ServiceFeed.PROCESSING_RESULT.SUCCESS);

        try
        {
            kafkaTemplate.send("PaymentRouter", payment.getPaymentIdentifier(), JSONHelper.convertToStringFromObject(feed));
        }catch(JsonProcessingException joe)
        {
            joe.printStackTrace();
        }
        return payment;
    }

}
