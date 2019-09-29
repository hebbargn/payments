package com.mejesticpay.mysqlstore;

import com.mejesticpay.mysqlstore.jpa.BaseJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseJpaRepositoryImpl.class)

public class PaymentStoreStartup
{

    public static void main(String []args)
        {
            SpringApplication.run(PaymentStoreStartup.class,args);
        }

}
