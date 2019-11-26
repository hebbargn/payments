package com.mejesticpay.services.creditenrich;

import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.paymentbase.Party;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.CreditEnrichment;
import com.mejesticpay.stp.STPInboundData;
import com.mejesticpay.util.JSONHelper;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;

@Service
public class CreditEnrichService
{
    private RSocket rSocket;
    private DataHandler dataHandler;
    @Value("${InboundCTCreditEnrichment:InboundCTCreditEnrichment}")
    public final String serviceName ="InboundCTCreditEnrichment";

    @PostConstruct
    private void connect()
    {
        rSocket = RSocketFactory.connect().keepAlive(Duration.ofSeconds(10),Duration.ofSeconds(10),3)
                .mimeType(MimeTypeUtils.APPLICATION_JSON_VALUE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                .transport(TcpClientTransport.create("localhost",8000))
                .start()
                .doOnError(Throwable::printStackTrace)
                .block();
        dataHandler = new DataHandler();
        rSocket.requestChannel(Flux.from(dataHandler))
                .doOnNext(dataHandler::processMessage)
                .doOnError(Throwable::printStackTrace)
                .subscribe();
        dataHandler.sendMessage("!!CLIENT_CHANNEL_REGISTER!!");
        System.out.println("Started Credit Enrich Service");
    }

    private class DataHandler implements Publisher<Payload>
    {
        private Subscriber<? super Payload> subscribe;

        @Override
        public void subscribe(Subscriber<? super Payload> subscriber) {
            this.subscribe = subscriber;
        }

        public void processMessage(Payload payload)
        {
            try {
                System.out.println(" Message " + payload.getDataUtf8());

                Payment payment = JSONHelper.convertToObjectFromJson(payload.getDataUtf8(), PaymentImpl.class);
                STPInboundData stpInboundData = new STPInboundData(payment, serviceName);

                CreditEnrichment creditEnrich = new CreditEnrichment();
                Party creditParty = new Party();
                creditParty.setName("Test User");
                creditParty.setAccountNumber("1212121");
                creditParty.setAccountType("Savings");
                creditEnrich.setCreditParty(creditParty);
                creditEnrich.setSettlementDate(LocalDate.now());
                creditEnrich.setClearingDate(LocalDate.now());
                stpInboundData.setServiceData(creditEnrich);

                String auditMessage = "Successfully enriched credit party from CIF.";
                AuditEntry auditEntry = new AuditEntry(serviceName, auditMessage, "test details");
                stpInboundData.addAuditEntry(auditEntry);
                stpInboundData.setResult(STPInboundData.PROCESSING_RESULT.SUCCESS);

                sendMessage(JSONHelper.convertToStringFromObject(stpInboundData));
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message)
        {
            subscribe.onNext(DefaultPayload.create(message,serviceName));
            System.out.println("serviceName = " + serviceName + "Message Sent " + message);
        }

        public void dispose()
        {
            subscribe.onComplete();
        }
    }
}
