package com.mydisc.MyDisc.helper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public final class AutowireHelper implements ApplicationContextAware {
    private static final AutowireHelper INSTANCE = new AutowireHelper();

    @Autowired
    private static ApplicationContext applicationContext;

    private AutowireHelper() {
    }

    public static void autowire(Object classToAutowire, Object... beansToAutowireInClass) {
        for (Object bean : beansToAutowireInClass) {
            if (bean == null) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(classToAutowire);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AutowireHelper.applicationContext = applicationContext;
    }

    public static AutowireHelper getInstance() {
        return INSTANCE;
    }
}
