package com.mejesticpay.apigateway;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class STPRScoketClientService
{
    private RSocket rSocket;

    @PostConstruct
    private void init()
    {
        rSocket = RSocketFactory.connect().keepAlive(Duration.ofSeconds(10),Duration.ofSeconds(10),3)
                .mimeType(MimeTypeUtils.APPLICATION_JSON_VALUE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                .transport(TcpClientTransport.create("localhost",8000))
                .start()
                .doOnError(Throwable::printStackTrace)
                .block();
    }

    public void sendToSTP(String paymentMessage)
    {
        if(rSocket != null && rSocket.availability() == 1)
        {
            rSocket.fireAndForget(DefaultPayload.create(paymentMessage,"RoutePayment")).block();
            System.out.println("Send to STP Engine");
        }
        else
        {
            System.out.println("Houstan, we have a problem");
            throw new RuntimeException("Connection to STP Engine is not available");
        }
    }
}
