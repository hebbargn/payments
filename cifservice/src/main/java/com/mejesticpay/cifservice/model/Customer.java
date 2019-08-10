package com.mejesticpay.cifservice.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
public class Customer
{

    @Id
    private String customerNumber;
    private String status;
    private String name;
    private String street;
    private String buildingNumber;
    private String postalCode;
    private String townName;
    private String country;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="customer_account",
            joinColumns = @JoinColumn(name="customerNumber", referencedColumnName = "customerNumber"),
            inverseJoinColumns =@JoinColumn(name="accountNumber", referencedColumnName = "accountNumber"))
    private Set<Account> accounts = new HashSet<>();

    public void addAccount(Account... accountLists)
    {
        for(Account acc: accountLists)
        {
            this.accounts.add(acc);
        }
    }

}
