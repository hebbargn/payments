package com.mejesticpay.iso20022.file.monitor;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

//@SpringBootApplication
public class FileMonitorApp
{
//    public static String inputFolder = "C:/gateway/USRTP/input";
    public static String inputFolder = "/Users/kpatel/gateway/USRTP/input";

    private static final Logger logger = LogManager.getLogger(FileMonitorApp.class);

    public static void main(String []args)
    {
//        SpringApplication.run(FileMonitorApp.class,args);
        startMonitor();
    }

    public static void startMonitor()
    {
        System.out.println("Starting Monitoring");
        final File inputDirectory = new File(inputFolder);
        FileAlterationObserver observer = new FileAlterationObserver(inputDirectory);
        FileAlterationMonitor monitor = new FileAlterationMonitor(5000);

        observer.addListener(new FileAlterationListenerImpl());
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            logger.error(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            @Override
            public void run() {
                try {
                    logger.debug("Stopping monitor.");
                    monitor.stop();
                } catch (Exception ignored) {
                    logger.error(ignored);
                }
            }
        }));
    }
}