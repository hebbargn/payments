package com.mejesticpay.mysqlstore.config;

import com.mejesticpay.mysqlstore.ignite.DBAdaptor;
import com.mejesticpay.mysqlstore.model.PaymentWrapper;
import com.mejesticpay.mysqlstore.mysql.PaymentRepository;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.cache.configuration.Factory;
import java.io.Serializable;

@Service
public class DBAdaptorFactory implements Factory<CacheStoreAdapter<String, PaymentWrapper>>, Serializable{


    private ApplicationContext context;



    public DBAdaptorFactory(ApplicationContext context)
    {
        this.context = context;
    }
    @Override
    public CacheStoreAdapter<String, PaymentWrapper> create()
    {
        PaymentRepository repository = context.getBean(PaymentRepository.class);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + repository);
        DBAdaptor adapter = new DBAdaptor(repository);
        return adapter;
    }
}