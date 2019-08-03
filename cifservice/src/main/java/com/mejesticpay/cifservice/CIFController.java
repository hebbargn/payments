package com.mejesticpay.cifservice;

import com.mejesticpay.cifservice.model.Account;
import com.mejesticpay.cifservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CIFController
{
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomer(@RequestBody Customer customer)
    {
        customerRepository.save(customer);
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody Account account)
    {
        accountRepository.save(account);
    }

    @GetMapping("/customer/id")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer getCustomer(@PathVariable("id") String customerNumber)
    {
        return customerRepository.findById(customerNumber).get();
    }

    @GetMapping("/account/id")
    @ResponseStatus(HttpStatus.CREATED)
    public Account getAccount(@PathVariable("id") String accountNumber)
    {
        return accountRepository.findById(accountNumber).get();
    }



}
