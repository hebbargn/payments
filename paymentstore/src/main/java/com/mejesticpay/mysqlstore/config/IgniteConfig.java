package com.mejesticpay.mysqlstore.config;

import com.mejesticpay.mysqlstore.ignite.DBAdaptor;
import com.mejesticpay.mysqlstore.model.PaymentWrapper;
import com.mejesticpay.mysqlstore.mysql.PaymentRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.IgniteReflectionFactory;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.FactoryBuilder;
import java.util.Collections;

@Configuration
public class IgniteConfig
{
    @Autowired
    private PaymentRepository repository;

    @Autowired
    private ApplicationContext context;

    @Bean
    public Ignite igniteInstance()
    {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        CacheConfiguration cache = new CacheConfiguration("PaymentsCache");
        cache.setIndexedTypes(String.class, PaymentWrapper.class);
        cache.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cache.setCacheMode(CacheMode.REPLICATED);
        cache.setCacheStoreFactory(new DBAdaptorFactory(context));
        cache.setWriteThrough(true);
        cache.setReadThrough(true);
        igniteConfiguration.setCacheConfiguration(cache);
        return Ignition.start(igniteConfiguration);

        //igniteConfiguration.setClientMode(true);
        //igniteConfiguration.setPeerClassLoadingEnabled(true);

        //TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        //tcpDiscoverySpi.setLocalPort(48500);
        /*TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        //TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder=new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        tcpDiscoverySpi.setIpFinder(ipFinder);*/

        //igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);

    }
}
