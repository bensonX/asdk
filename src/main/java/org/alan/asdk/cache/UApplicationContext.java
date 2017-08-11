package org.alan.asdk.cache;

import org.alan.asdk.common.Log;
import org.alan.asdk.dao.logic.UChannelDao;
import org.alan.asdk.dao.logic.UChannelMasterDao;
import org.alan.asdk.dao.logic.UGameDao;
import org.alan.asdk.sdk.txmsdk.TXMSDKManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 运行时获取Spring Bean
 * Created by ant on 2015/10/15.
 */
@Component
@Scope("singleton")
public class UApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Autowired
    private UGameDao gameDao;
    @Autowired
    private UChannelDao channelDao;
    @Autowired
    private UChannelMasterDao channelMasterDao;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        UApplicationContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {

        return applicationContext;
    }

    public static Object getBean(String name) {

        return applicationContext.getBean(name);
    }

    public static Object getBean(String name, Class type) {
        return applicationContext.getBean(name, type);
    }

    public static boolean containBean(String name) {

        return applicationContext.containsBean(name);
    }


    @PreDestroy
    public void onDestory() {
        Log.d("Now to clean ...");
//        FailOrderSelector.getInstance().shutDown();
        TXMSDKManager.getInstance().destory();
    }

}
