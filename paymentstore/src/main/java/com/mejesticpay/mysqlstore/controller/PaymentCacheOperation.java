package com.mejesticpay.mysqlstore.controller;

import com.mejesticpay.mysqlstore.model.PaymentWrapper;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentfactory.PaymentImpl;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.concurrent.locks.Lock;

@RestController
public class PaymentCacheOperation
{

    private static final String debitenrich = "json_genesis";
    private String json_debit_enrich;
    private String json_credit_enrich;
    private String json_fraud_check;

    @Autowired
    private Ignite ignite;

    @Value("${paymentCache:PaymentsCache}")
    private String paymentCacheName;

    private IgniteCache paymentCache;
    @PostConstruct
    public void init()
    {
         paymentCache = ignite.getOrCreateCache(paymentCacheName);
    }

    @PostMapping("/paymentcache")
    public Payment createPayment(@RequestBody PaymentImpl payment)
    {
        // When creating payment, start with version = 1.
        payment.setVersion(1);
        PaymentWrapper pw = new PaymentWrapper(payment);
        paymentCache.put(pw.getPaymentRef(),pw);
        return payment;
    }

    @GetMapping(path="/paymentcache/{id}")
    public Payment getPayment(@PathVariable("id") String paymentID)
    {
        return ((PaymentWrapper)paymentCache.get(paymentID)).getPayment();
    }


    @PutMapping("/paymentcache/{service}/{id}")
    public Payment updatePayment(@RequestBody PaymentImpl payment, @PathVariable String service, @PathVariable String id)
    {

        Lock lock = paymentCache.lock(id);
        try
        {
            // Acquire the lock
            lock.lock();
            payment.incrementVersion();
            PaymentWrapper pw = new PaymentWrapper(payment);
            paymentCache.put(id, pw);
        }
        finally
        {
            // Release the lock
            lock.unlock();
        }

        return payment;
    }
}
