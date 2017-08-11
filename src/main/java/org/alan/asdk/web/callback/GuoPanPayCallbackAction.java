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
 * 叉叉助手 支付回调
 * Created by ant on 2015/9/14.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/guopan")
public class GuoPanPayCallbackAction extends UActionSupport {

    private String trade_no;            //果盘唯一订单号
    private String serialNumber;        //游戏方订单序列号
    private String money;               //消费金额。单位是元，精确到分，如10.00
    private String status;              //状态；0=失败；1=成功；2=失败，原因是余额不足。
    private String t;                   //时间戳
    private String sign;                //加密串 sign=md5(serialNumber +money+status+t+SERVER_KEY) 是五个变量值拼接后经md5后的值，其中SERVER_KEY在果盘开放平台上获得。
    private String appid;
    private String item_id;
    private String item_price;
    private String item_count;
    private String reserved;            //扩展参数，SDK发起支付时有传递，则这里会回传。

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<叉叉助手>充值回调开始： trade_no=" +trade_no+ ", serialNumber=" +serialNumber+ ", money=" +money+ ", status=" +status+ ", t=" +t+ ", sign=" +sign+ ", appid=" +appid+ "item_id=" +item_id+ "item_price=" +item_price+ ", item_count=" +item_count+ ", reserved=" +reserved);

            //Log.i("<叉叉助手>回调["+serialNumber+"]开始!");
            long orderID = Long.parseLong(serialNumber);

            UOrder order = orderManager.getOrder(orderID);

            if (order == null || order.getChannel() == null) {
                Log.i("<叉叉助手>订单["+serialNumber+"]为空或者channel为空!");
                this.renderState(false);
                return;
            }

            UChannel channel = order.getChannel();
            if (channel == null) {
                Log.i("<叉叉助手>订单["+serialNumber+"]channel为空!");
                this.renderState(false);
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.i("<叉叉助手>订单["+serialNumber+"]重复!");
                this.renderState(false);
                return;
            }

            if (!isSignOK(channel)) {
                this.renderState(false);
                return;
            }
            float money = Float.parseFloat(this.money);
            int moneyInt = (int) (money * 100);  //以分为单位
            if (order.getMoney() != moneyInt){
                Log.i("<叉叉助手>订单["+serialNumber+"]充值失败 , 金额不一致 orderMoney = "+ order.getMoney() + ", 实际金额 = " + moneyInt);
                this.renderState(false);
                return;
            }
            if ("1".equals(this.status)) {
                order.setChannelOrderID(this.trade_no);
                order.setState(PayState.STATE_SUC);
                orderManager.saveOrder(order);
                Log.i("<叉叉助手>订单["+serialNumber+"]充值成功");
                SendAgent.sendCallbackToServer(this.orderManager, order);
                this.renderState(true);
            } else {
                order.setChannelOrderID(this.trade_no);
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
        String sb = this.serialNumber +
                this.money +
                this.status +
                this.t +
                channel.getCpAppKey();

        String md5 = EncryptUtils.md5(sb).toLowerCase();
        boolean flag = md5.equals(this.sign);
        if (!flag){
            Log.i("<叉叉助手>订单["+serialNumber+"]验签失败! str = "+sb +" , md5 = " + md5 + ", sign = " + this.sign);
        }
        return flag;
    }

    private void renderState(boolean suc) throws IOException {

        String res = "success";
        if (!suc) {
            res = "fail";
        }

        this.response.getWriter().write(res);
    }


    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }
}
