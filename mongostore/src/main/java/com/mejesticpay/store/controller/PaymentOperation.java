package com.mejesticpay.store.controller;

import com.mejesticpay.paymentbase.Genesis;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentfactory.PaymentImpl;
import com.mejesticpay.store.mongo.PaymentRepository;
import com.mejesticpay.store.mongo.PaymentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;


@RestController
public class PaymentOperation
{

    @Autowired
    private PaymentRepository repository;

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping(path="/payments")
    public List<Payment> getPayments()
    {
        List<Payment> paymentList = new ArrayList<>();
        for(PaymentWrapper pw : repository.findAll())
        {
            paymentList.add(pw.getPayment());
        }
        return paymentList;
    }

    @GetMapping(path="/payments/{id}")
    public Payment getPayment(@PathVariable("id") String paymentID)
    {
        return  repository.findById(paymentID).get().getPayment();
    }

    @DeleteMapping(path="/payments/{id}")
    public ResponseEntity deletePayment(@PathVariable("id") String paymentID)
    {
        try {
            PaymentWrapper pw = repository.findById(paymentID).get();
            repository.delete(pw);
            return new ResponseEntity("Successfully deleted payment", HttpStatus.OK);
        }catch(NoSuchElementException noe)
        {
            return new ResponseEntity("No Payment found for " + paymentID, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/payments")
    public Payment createPayment(@RequestBody Genesis origination)
    {
        Payment payment = generatePayment(origination);
        repository.insert((new PaymentWrapper(payment.getPaymentIdentifier(),payment)));
        return payment;
    }

    @PutMapping("/payments/{id}")
    public Payment updatePayment(@RequestBody PaymentImpl payment, @PathVariable String id)
    {
        Optional<PaymentWrapper>optional = repository.findById(id);
        if(optional.isPresent() && payment != null)
        {
            PaymentWrapper pw = optional.get();
            payment.setLastUpdatedTime(Instant.now());
            pw.setPayment(payment);
            pw = repository.save(pw);
            return pw.getPayment();
        }

        return null;
    }

    public Payment generatePayment(Genesis origination)
    {
        PaymentImpl payment = new PaymentImpl(origination);
        payment.incrementVersion();
        payment.setState("Active");
        payment.setSource("ISO");
        payment.setBranch("US");

        payment.setTrack("OutboundPayment");
        payment.setStation("New");

        payment.setPaymentIdentifier(UUID.randomUUID().toString());
        return payment;
    }

}
