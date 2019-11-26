package com.mejesticpay.services.debitenrich;

import com.mejesticpay.paymentbase.AuditEntry;
import com.mejesticpay.paymentbase.Party;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.service.DebitEnrichment;
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
public class DebitEnrichService
{

    private RSocket rSocket;
    private DataHandler dataHandler;
    @Value("${InboundCTCreditEnrichment:InboundCTDebitEnrichment}")
    public final String serviceName ="InboundCTDebitEnrichment";

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
        System.out.println("Started Debit Enrich Service");

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

                DebitEnrichment debitEnrich = new DebitEnrichment();
                Party debitParty = new Party();
                debitParty.setName("Debit User");
                debitParty.setAccountNumber("55555");
                debitParty.setAccountType("GL");
                debitEnrich.setDebitParty(debitParty);
                debitEnrich.setSettlementDate(LocalDate.now());
                debitEnrich.setSettlementDate(LocalDate.now());
                stpInboundData.setServiceData(debitEnrich);

                String auditMessage = "Successfully enriched debit party from CIF.";
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
            System.out.println("serviceName = " + serviceName + "Message Sent " + message);        }

        public void dispose()
        {
            subscribe.onComplete();
        }
    }
}
