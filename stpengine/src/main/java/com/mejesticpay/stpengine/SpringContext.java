package com.mejesticpay.stpengine;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContext implements ApplicationContextAware
{
        private static ApplicationContext context;
        public ApplicationContext getContext() {
            return context;
        }
        @Override
        public void setApplicationContext(ApplicationContext context)
                throws BeansException
        {
            this.context=context;
        }

        public static <T extends Object> T getBean(Class<T> beanClass)
        {
            return context.getBean(beanClass);
        }

}
