package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.EncryptUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * Created by ant on 2015/9/15.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/paojiao")
public class PaojiaoPayCallbackAction extends UActionSupport {


    private String uid;        //付款用户在泡椒网的唯一标识
    private String orderNo;        //付款订单在泡椒网的订单号
    private String price;        //订单金额，浮点类型，单位为：RMB元
    private String status;        //订单状态固定为5，支付失败的情况不会通知
    private String remark;        //订单的备注
    private String subject;        //订单标题
    private String gameId;        //接入时获取的appId或gamId
    private String payTime;        //付款时间，格式为：2014-07-21 08:28:26
    private String ext;        //自定义扩展数据，合作方在订单创建时传入，通知时原样返回
    private String sign;        //根据请求内容产生的加密串，用于合法性校验

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<paojiao>充值回调开始： uid=" +uid+ ", orderNo=" +orderNo+ ", price=" +price+ ", status=" +status+ ", remark=" +remark+ ", subject=" +subject+ ", gameId=" +gameId+ ", payTime=" +payTime+ ", ext=" +ext+ ", sign=" +sign);

            long orderID = Long.parseLong(ext);

            UOrder order = orderManager.getOrder(orderID);

            if (order == null || order.getChannel() == null) {
                Log.d("<paojiao>订单或渠道不存在.");
                this.renderState(false);
                return;
            }

            UChannel channel = order.getChannel();
            if (channel == null) {

                this.renderState(false);
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.d("<paojiao>订单完成状态：" + order.getState());
                this.renderState(true);
                return;
            }

            if (!isSignOK(channel)) {
                Log.d("<paojiao>签名错误.sign:%s;appKey:%s;orderID:%s", sign, channel.getCpPayKey(), ext);
                this.renderState(true);
                return;
            }

            if ("5".equals(this.status)) {

                float money = Float.parseFloat(this.price);
                int moneyInt = (int) (money * 100);  //以分为单位

                order.setMoney(moneyInt);
                order.setChannelOrderID(this.orderNo);
                order.setState(PayState.STATE_SUC);

                orderManager.saveOrder(order);

                SendAgent.sendCallbackToServer(this.orderManager, order);

            } else {
                order.setChannelOrderID(this.orderNo);
                order.setState(PayState.STATE_FAILED);
                orderManager.saveOrder(order);
            }


        } catch (Exception e) {
            try {
                renderState(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private boolean isSignOK(UChannel channel) {
        StringBuilder sb = new StringBuilder();
        sb.append("uid=").append(this.uid)
                .append("price=").append(this.price)
                .append("orderNo=").append(this.orderNo)
                .append("remark=").append(this.remark)
                .append("status=").append(this.status)
                .append("subject=").append(this.subject)
                .append("gameId=").append(this.gameId)
                .append("payTime=").append(this.payTime)
                .append("ext=").append(this.ext)
                .append(channel.getCpAppKey());

        String md5 = EncryptUtils.md5(sb.toString()).toLowerCase();

        return md5.equals(this.sign);
    }

    private void renderState(boolean suc) throws IOException {

        String res = "success";
        if (!suc) {
            res = "fail";
        }

        this.response.getWriter().write(res);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
