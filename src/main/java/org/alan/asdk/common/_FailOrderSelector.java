package org.alan.asdk.common;

import org.alan.asdk.entity.TFailOrderInfo;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.service._FailOrdeNoticeThread;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 失败订单筛选器
 *
 * @author Lance Chow
 * @create 2015-12-21 16:19
 */
public class _FailOrderSelector implements Runnable{

    /**
     * 最大订单失败数
     */
    private static int failOrderCount = 100000;

    private static _FailOrderSelector instance;

    private Set<TFailOrderInfo> failOrders;
    /**
     * 通知线程
     */
    private _FailOrdeNoticeThread noticeThread;

    public void loadFailOrders(List<UOrder> orders , _FailOrdeNoticeThread thread){
        //如果orders里面有内容则return;
        if (noticeThread==null){
            noticeThread = thread;
        }
        if (failOrders!=null&&!failOrders.isEmpty()){
            return;
        }
        failOrders = new CopyOnWriteArraySet<>();
        for (UOrder order : orders){
            TFailOrderInfo info = new TFailOrderInfo();
            info.setOrder(order);
            info.setCount(1);
            info.setNext(new Date().getTime());
            failOrders.add(info);
        }
    }

    public void addOrder(UOrder order){
        int size = failOrders.size();
        if (size == failOrderCount){
            Log.i("错误订单超过最大限额:"+failOrderCount);
        }
        TFailOrderInfo orderInfo = new TFailOrderInfo();
        /**
         * 第一次是延时3分钟后执行
         */
        orderInfo.setNext(new Date().getTime() + 3 * 60 * 1000);
        orderInfo.setOrder(order);
        orderInfo.setCount(1);
        if(failOrders.contains(orderInfo))
        failOrders.add(orderInfo);
    }

    public static _FailOrderSelector getInstance(){
        if ( instance == null){
            instance = new _FailOrderSelector();
        }
        return instance;
    }

    public void romeve(TFailOrderInfo info){
        failOrders.remove(info);
    }

    private boolean isRun = true;

    public void shutDown(){
        isRun = false;
    }

    @Override
    public void run() {
        while (isRun){

            Iterator<TFailOrderInfo> it = failOrders.iterator();

            while (it.hasNext()){
                TFailOrderInfo info = it.next();
                //判断是否该删除
                if(info.getCount()>7){
                    Log.i("侦测到重复订单["+info.getOrder().getOrderID()+"]已过期删除...");
                    failOrders.remove(info);
                }
                //判断是否到时间当前时间
                long now = new Date().getTime();

                if (info.getNext() < now && !info.isRuning()){
                    info.setRuning(true);
                    noticeThread.sendCallback(info);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
