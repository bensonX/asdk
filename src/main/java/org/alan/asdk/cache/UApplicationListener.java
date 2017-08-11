package org.alan.asdk.cache;

import org.alan.asdk.cache.impl.logic.*;
import org.alan.asdk.cache.impl.manager.TAdminCache;
import org.alan.asdk.cache.impl.manager.TModuleCache;
import org.alan.asdk.common.Log;
import org.alan.asdk.dao.logic.*;
import org.alan.asdk.dao.manager.TAdminDao;
import org.alan.asdk.dao.manager.TModuleDao;
import org.alan.asdk.utils.LanguageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 在Spring启动的时候，加载需要缓存的数据
 */
@Repository
@Transactional(readOnly = true)
public class UApplicationListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private UGameDao gameDao;
    @Autowired
    private UChannelDao channelDao;
    @Autowired
    private UChannelMasterDao channelMasterDao;
    @Autowired
    private UGoodsDao goodsDao;
    @Autowired
    private UGoodsConfigDao goodsConfigDao;
    @Autowired
    private TModuleDao moduleDao;
    @Autowired
    private TAdminDao adminDao;
    @Autowired
    private UChargeChannelDao chargeChannelDao;
    @Autowired
    private SelfChannelDao selfChannelDao;
    @Autowired
    UApplicationContext context;

    private static boolean loaded = false;
    @Value("${config.fail.order.sender}")
    private boolean isFailOrderSender;


    @Override
    public void onApplicationEvent(ApplicationEvent contextStartedEvent) {
        try {
            if (!loaded) {
                WebCacheManager.getInstance().loadMailCaptcha();
                if (isFailOrderSender){
                    Log.i("----该服务器为失败订单加载服务器 , 会加载所有失败订单!----");
//                    FailOrderSelector.getInstance().loadFailOrders(orderManager.getOrderByState(PayState.STATE_FAILED,new Date()),thread);
                }else{
                    //由于使用集群会导致数据错误 所以这里预加载的时候不加载任何错误数据
                    Log.i("----该服务器不会加载任何失败订单!----");
//                    FailOrderSelector.getInstance().loadFailOrders(new ArrayList<UOrder>(),thread);
                }
//                Thread thread = new Thread(FailOrderSelector.getInstance());
                LanguageHelper.INS.loadLanguage();
//                Log.i("----失败订单筛选器 线程已启动----");
//                thread.start();
                loaded = true;
            }

        } catch (Exception e) {
            Log.e("Load Data on com.u8.server inited error.", e);
        }
    }
    //每分钟更新一次数据库配置
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void loadConfig(){
        Log.i("----更新配置到内存----");
        UGameCache.getInstance().load(gameDao.findAll());
        UChannelMasterCache.getInstance().load(channelMasterDao.findAll());
        UChannelCache.getInstance().load(channelDao.findAll());
        UGoodsCache.getInstance().load(goodsDao.findAll());
        UGoodsConfigCache.getInstance().load(goodsConfigDao.findAll());
        TModuleCache.getInstance().load(moduleDao.findAll());
        TAdminCache.getInstance().load(adminDao.findAll());
        UChargeChannelCache.getInstance().load(chargeChannelDao.findAll());
        SelfChannelCache.getInstance().load(selfChannelDao.findAll());
        Log.i("----配置更新完成----");
    }

}
