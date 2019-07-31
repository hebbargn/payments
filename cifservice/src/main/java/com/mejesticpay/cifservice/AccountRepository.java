package com.mejesticpay.cifservice;

import com.mejesticpay.cifservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
