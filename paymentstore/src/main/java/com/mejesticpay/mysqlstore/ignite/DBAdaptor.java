package com.mejesticpay.mysqlstore.ignite;

import com.mejesticpay.mysqlstore.model.CommonBase;
import com.mejesticpay.mysqlstore.model.PaymentWrapper;
import com.mejesticpay.mysqlstore.mysql.PaymentRepository;
import org.apache.ignite.cache.store.CacheStoreAdapter;


import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

public class DBAdaptor extends CacheStoreAdapter<String, PaymentWrapper>
{
    private  PaymentRepository repository;
    public DBAdaptor(PaymentRepository repository)
    {
        this.repository = repository;
    }

    public PaymentWrapper load(String key) throws CacheLoaderException
    {
        System.out.println("Loading from DB");
        return repository.findById(key).get();
    }

   public void write(Cache.Entry<? extends String, ? extends PaymentWrapper> entry) throws CacheWriterException
   {
       System.out.println("Writing into from DB");
        repository.save(entry.getValue());
   }

    public void delete(Object key) throws CacheWriterException
    {
        System.out.println("Deleting from DB");
        repository.deleteById(key.toString());
    }
}