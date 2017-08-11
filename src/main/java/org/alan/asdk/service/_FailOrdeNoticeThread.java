package org.alan.asdk.service;

import org.alan.asdk.common.Log;
import org.alan.asdk.common._FailOrderSelector;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.TFailOrderInfo;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.web.SendAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 失败订单通知线程池
 *
 * @author Lance Chow
 * @create 2015-12-21 17:55
 */
@Component
public class _FailOrdeNoticeThread {

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private UOrderManager orderManager;

    public void sendCallback(final TFailOrderInfo info) {


        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.i("侦测到发送失败的订单: " + info.getOrder().getOrderID());
                int count = info.getCount();
                if (SendAgent.sendCallbackToServer(orderManager, info)) {
                    UOrder order = info.getOrder();
                    order.setState(PayState.STATE_COMPLETE);
                    orderManager.saveOrder(order);
                    _FailOrderSelector.getInstance().romeve(info);
                    Log.i("重新发送订单[" + info.getOrder().getOrderID() + "](" + info.getCount() + ")次 成功");
                } else {
                    info.setNext((long) (new Date().getTime() + Math.pow(3, count) * 60 * 1000));
                    info.setCount(count + 1);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Log.i("发送订单[" + info.getOrder().getOrderID() + "]失败 , 下次发送时间 : " + format.format(new Date(info.getNext())));
                }
                info.setRuning(false);
            }
        });
    }
}
