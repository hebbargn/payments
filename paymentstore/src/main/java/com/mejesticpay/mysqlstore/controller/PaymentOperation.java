package com.mejesticpay.mysqlstore.controller;


import com.mejesticpay.mysqlstore.mysql.PaymentRepository;
import com.mejesticpay.mysqlstore.mysql.PaymentWrapper;
import com.mejesticpay.paymentbase.Payment;
import com.mejesticpay.paymentfactory.PaymentImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class PaymentOperation
{

    @Autowired
    private PaymentRepository repository;

    @GetMapping(path="/payments")
    public List<Object[]> getPayments()
    {
        return  repository.findPaymentList();
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
    public Payment createPayment(@RequestBody PaymentImpl payment)
    {
        // When creating payment, start with version = 1.
        payment.setVersion(1);
        PaymentWrapper pw = new PaymentWrapper(payment);
        repository.save(pw);
        return payment;
    }

    @PutMapping("/payments/{id}")
    public Payment updatePayment(@RequestBody PaymentImpl payment, @PathVariable String id)
    {
        Optional<PaymentWrapper>optional = repository.findById(id);
        if(optional.isPresent() && payment != null)
        {
            PaymentWrapper pw = optional.get();
            pw.updatePayment(payment);
            pw = repository.save(pw);
            return pw.getPayment();
        }

        return null;
    }


}
