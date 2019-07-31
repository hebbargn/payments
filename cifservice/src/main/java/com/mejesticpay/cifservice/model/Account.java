package com.mejesticpay.cifservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
public class Account
{
    @Id
    private String accountNumber;
    private String type;
    private String state;
    private String status;
    private String description;

    @ManyToMany(mappedBy="accounts")
    private Set<Customer> customers = new HashSet<>();

    public void addCustomer(Customer... customers)
    {
        for(Customer acc: customers)
        {
            this.customers.add(acc);
        }
    }

}
