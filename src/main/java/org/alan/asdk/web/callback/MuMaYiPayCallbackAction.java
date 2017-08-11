package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.mumayi.MMYPayResult;
import org.alan.asdk.sdk.mumayi.MuMaYiSDK;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.JsonUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 木蚂蚁支付回调
 * Created by ant on 2015/9/14.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/mumayi")
public class MuMaYiPayCallbackAction extends UActionSupport {

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<木蚂蚁>支付回调开始：");

            BufferedReader br = this.request.getReader();
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }

            Log.d("<木蚂蚁>支付回调 . request params:" + sb.toString());

            MMYPayResult result = (MMYPayResult) JsonUtils.decodeJson(sb.toString(), MMYPayResult.class);

            if (result == null) {
                renderState(false);
                return;
            }

            long orderID = Long.parseLong(result.getOrderID());

            UOrder order = orderManager.getOrder(orderID);

            if (order == null || order.getChannel() == null) {
                Log.d("<木蚂蚁>订单或渠道不存在.");
                this.renderState(false);
                return;
            }

            UChannel channel = order.getChannel();
            if (channel == null) {

                this.renderState(false);
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.d("<木蚂蚁>订单完成状态：" + order.getState());
                this.renderState(true);
                return;
            }

            if (!MuMaYiSDK.verify(result.getTradeSign(), channel.getCpAppKey(), result.getOrderID())) {
                Log.d("<木蚂蚁>签名错误.sign:%s;appKey:%s;orderID:%s", result.getTradeSign(), channel.getCpAppKey(), result.getOrderID());
                this.renderState(true);
                return;
            }

            //TODO:这里价格单位需要和木蚂蚁技术沟通确认下
            float money = Float.parseFloat(result.getProductPrice());
            int moneyInt = (int) (money * 100);  //以分为单位

            order.setMoney(moneyInt);
            order.setChannelOrderID("");        //渠道orderID貌似没有值
            order.setState(PayState.STATE_SUC);

            orderManager.saveOrder(order);

            SendAgent.sendCallbackToServer(this.orderManager, order);


        } catch (Exception e) {
            try {
                renderState(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void renderState(boolean suc) throws IOException {

        String res = "success";
        if (!suc) {
            res = "fail";
        }

        this.response.getWriter().write(res);
    }

}
