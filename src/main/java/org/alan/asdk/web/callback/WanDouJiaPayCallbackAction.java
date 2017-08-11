package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.JsonUtils;
import org.alan.asdk.utils.RSAUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * 豌豆荚支付回调通知接口
 * Created by ant on 2015/4/25.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/wandoujia")
public class WanDouJiaPayCallbackAction extends UActionSupport {

    private String content;     //内容
    private String signType;    //RSA
    private String sign;        //签名


    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<豌豆荚>充值回调开始： content=" +content+ ", signType=" +signType+ ", sign=" +sign);

            WDJPayCallbackContent data = (WDJPayCallbackContent) JsonUtils.decodeJson(content, WDJPayCallbackContent.class);

            if (data == null) {
                Log.e("<豌豆荚>内容解析错误...");
                return;
            }

            long localOrderID = Long.parseLong(data.getOut_trade_no());

            UOrder order = orderManager.getOrder(localOrderID);

            if (order == null || order.getChannel() == null) {
                Log.d("<豌豆荚>订单或渠道不存在.");
                this.renderState(false, "notifyId 错误");
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.d("<豌豆荚>订单完成状态" + order.getState());
                this.renderState(true, "该订单已经被处理,或者CP订单号重复");
                return;
            }

            int orderMoney = Integer.valueOf(data.getMoney());      //转换为分


            if (order.getMoney() != orderMoney) {
                Log.e("<豌豆荚>订单金额不一致! local orderID:" + localOrderID + "; money returned:" + data.getMoney() + "; order money:" + order.getMoney());
                this.renderState(false, "订单金额不一致");
                return;
            }

            if (isValid(order.getChannel())) {
                order.setChannelOrderID(data.getOrderId());
                order.setState(PayState.STATE_SUC);
                orderManager.saveOrder(order);
                SendAgent.sendCallbackToServer(this.orderManager, order);
                this.renderState(true, "");
            } else {
                order.setChannelOrderID(data.getOrderId());
                order.setState(PayState.STATE_FAILED);
                orderManager.saveOrder(order);
                this.renderState(false, "sign 错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.renderState(false, "未知错误");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean isValid(UChannel channel) {

        return RSAUtils.verify(this.content, this.sign, channel.getCpPayKey(), "UTF-8");

    }

    private void renderState(boolean suc, String msg) throws IOException {

        if (suc) {
            this.response.getWriter().write("success");
        } else {
            this.response.getWriter().write("fail");
        }

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class WDJPayCallbackContent {

        private String timeStamp;           //时间戳
        private String orderId;             //豌豆荚订单号
        private String money;               //订单金额，单位分
        private String chargeType;          //支付方式
        private String appKeyId;            //appKey
        private String buyerId;             //购买者的userID
        private String out_trade_no;        //本地订单号
        private String cardNo;              //只有充值卡充值的时候才不为空

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getChargeType() {
            return chargeType;
        }

        public void setChargeType(String chargeType) {
            this.chargeType = chargeType;
        }

        public String getAppKeyId() {
            return appKeyId;
        }

        public void setAppKeyId(String appKeyId) {
            this.appKeyId = appKeyId;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }
    }

}
