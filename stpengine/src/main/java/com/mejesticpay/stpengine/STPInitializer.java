package com.mejesticpay.stpengine;

import com.mejesticpay.stpengine.controller.STPServiceClientManager;
import com.mejesticpay.stpengine.controller.STPServiceRequestProcessor;
import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class STPInitializer
{
    @Autowired
    private STPServiceClientManager stpServiceClientManager;

    @Autowired
    private STPServiceRequestProcessor stpServiceRequestProcessor;

    private Disposable server;


    @PostConstruct
    public void initialize()
    {
        this.server = RSocketFactory.receive()
                .acceptor((setupPayload, reactiveSocket) -> Mono.just(new STPClientConnectionHandler()))
                .transport(TcpServerTransport.create("localhost", 8000))
                .start()
                .doOnError(Throwable::printStackTrace)
                .doOnNext(x -> System.out.println("STPInitializer started"))
                .subscribe();
    }

    private class STPClientConnectionHandler extends AbstractRSocket
    {
        @Override
        public Flux<Payload> requestChannel(Publisher<Payload> payload)
        {
            STPClientRequestHandler handler = new STPClientRequestHandler(stpServiceClientManager,stpServiceRequestProcessor);
            Flux.from(payload)
                    .doOnTerminate(handler::unSubscribe)
                    .doOnError(Throwable::printStackTrace)
                    .doAfterTerminate(handler::unSubscribe)
                    .subscribe(handler::processPayload);
            System.out.println("Client Channel success");
            return Flux.from(handler);
        }

        @Override
        public Mono<Void> fireAndForget(Payload payload)
        {
            stpServiceRequestProcessor.getRequestProcessor(payload.getMetadataUtf8()).process(payload.getDataUtf8());
            payload.release();
            return Mono.empty();
        }
    }

}
