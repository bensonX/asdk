package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.anzhi.Des3Util;
import org.alan.asdk.service.UChannelManager;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.JsonUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * 安智支付回调
 * Created by ant on 2015/4/29.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/anzhi")
public class AnzhiPayCallbackAction extends UActionSupport {

    private String data;

    @Autowired
    private UOrderManager orderManager;

    @Autowired
    private UChannelManager channelManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<安智>充值回调开始： data=" +data);
            List<UChannel> channels = channelManager.getChannelBySDKName("anzhi");
            for (UChannel channel : channels) {
                String json = Des3Util.decrypt(this.data, channel.getCpAppSecret());
                if (json == null){
                    continue;
                }
                AnzhiPayContent data = (AnzhiPayContent) JsonUtils.decodeJson(json, AnzhiPayContent.class);

                if (data == null) {
                    Log.e("<安智>内容解析错误...");
                    this.renderState(false);
                    return;
                }

                long localOrderID = Long.parseLong(data.getCpInfo());

                UOrder order = orderManager.getOrder(localOrderID);

                if (order == null || order.getChannel() == null) {
                    Log.d("<安智>订单或渠道不存在.");
                    this.renderState(false);
                    return;
                }

                if (order.getState() == PayState.STATE_COMPLETE) {
                    Log.d("<安智>订单完成状态：" + order.getState());
                    this.renderState(true);
                    return;
                }

                if (!"1".equals(data.getCode())) {
                    Log.e("<安智>平台支付失败 local orderID:" + localOrderID + "; order id:" + data.getOrderId());
                    this.renderState(false);
                    return;
                }
                int money = Integer.parseInt(data.getOrderAmount());
                int gift = 0;
                if (!StringUtils.isEmpty(data.getRedBagMoney())){
                    gift = Integer.parseInt(data.getRedBagMoney());
                }
                if ((money+gift) != order.getMoney()){
                    Log.i("<安智>充值错误! 金额不等 money =" +money +" , order.getMoney = "+ order.getMoney());
                    this.renderState(true);
                    return;
                }
                order.setChannelOrderID(data.getOrderId());
                order.setState(PayState.STATE_SUC);
                orderManager.saveOrder(order);
                SendAgent.sendCallbackToServer(this.orderManager, order);
                this.renderState(true);
                return;
            }
            Log.i("<安智>充值失败 : data = " + data);
            this.renderState(false);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.renderState(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void renderState(boolean suc) throws IOException {

        if (suc) {
            this.response.getWriter().write("success");
        } else {
            this.response.getWriter().write("failure");
        }

    }

    public static class AnzhiPayContent {

        private String payAmount;
        private String uid;
        private String notifyTime;
        private String cpInfo;
        private String memo;
        private String orderAmount;
        private String orderAccount;
        private String code;
        private String orderTime;
        private String msg;
        private String orderId;
        private String redBagMoney;

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNotifyTime() {
            return notifyTime;
        }

        public void setNotifyTime(String notifyTime) {
            this.notifyTime = notifyTime;
        }

        public String getCpInfo() {
            return cpInfo;
        }

        public void setCpInfo(String cpInfo) {
            this.cpInfo = cpInfo;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderAccount() {
            return orderAccount;
        }

        public void setOrderAccount(String orderAccount) {
            this.orderAccount = orderAccount;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getRedBagMoney() {
            return redBagMoney;
        }

        public void setRedBagMoney(String redBagMoney) {
            this.redBagMoney = redBagMoney;
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
