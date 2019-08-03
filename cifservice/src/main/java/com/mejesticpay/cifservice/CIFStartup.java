package com.mejesticpay.cifservice;

import com.mejesticpay.cifservice.model.Account;
import com.mejesticpay.cifservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
public class CIFStartup implements CommandLineRunner
{
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    public static void main(String [] args)
    {
        SpringApplication.run(CIFStartup.class, args);
    }

    @Override
    @Transactional
    public void run(String... strings)
    {
      /*  Account a1 = new Account();
        a1.setAccountNumber("001");
        a1.setState("Active");
        a1.setStatus("Open");
        a1.setType("Savings");

        Customer c1 = new Customer();
        c1.setCustomerNumber("100");
        c1.setAddress("111 sfo stert, GA, USA");
        c1.setName("Alfred Cheese");
        c1.addAccount(a1);


        customerRepository.save(c1);

        List<Customer> customers = customerRepository.findAll();
        for(Customer c: customers)
            System.out.println(c);*/
    }
}
