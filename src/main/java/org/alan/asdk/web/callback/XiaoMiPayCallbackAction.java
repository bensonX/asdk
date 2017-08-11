package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.HmacSHA1Encryption;
import org.alan.asdk.web.SendAgent;
import org.apache.http.util.TextUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * 小米SDK充值回调接口
 * Created by ant on 2015/4/21.
 */

@Controller
@Scope("prototype")
@Namespace("/pay/xiaomi")
public class XiaoMiPayCallbackAction extends UActionSupport {

    private String appId;               //必须 	游戏ID
    private String cpOrderId;           //必须 	开发商订单ID
    private String cpUserInfo;            //可选 	开发商透传信息
    private String uid;                //必须 	用户ID
    private String orderId;            //必须 	游戏平台订单ID
    private String orderStatus;        //必须 	订单状态，TRADE_SUCCESS 代表成功
    private String payFee;                //必须 	支付金额,单位为分,即0.01 米币。
    private String productCode;        //必须 	商品代码
    private String productName;        //必须 	商品名称
    private String productCount;        //必须 	商品数量
    private String payTime;            //必须 	支付时间,格式 yyyy-MM-dd HH:mm:ss
    private String orderConsumeType;    //可选 	订单类型：10：普通订单11：直充直消订单
    private String partnerGiftConsume;    //可选 	使用游戏券金额 （如果订单使用游戏券则有,long型），如果有则参与签名
    private String signature;            //必须 	签名,签名方法见后面说明

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<小米>充值回调开始： appId=" +appId+ ", cpOrderId=" +cpOrderId+ ", cpUserInfo=" +cpUserInfo+ ", uid=" +uid+ ", orderId=" +orderId+ ", orderStatus=" +orderStatus+ ", payFee=" +payFee+ ", productCode=" +productCode+ ", productName=" +productName+ ", productCount=" +productCount+ ", payTime=" +payTime+ ", orderConsumeType=" +orderConsumeType+ ", partnerGiftConsume=" +partnerGiftConsume+ ", signature=" +signature);

            //Log.i("<小米>回调开始 cpOrderId="+cpOrderId);
            long orderID = Long.parseLong(cpOrderId);

            UOrder order = orderManager.getOrder(orderID);

            if (order == null || order.getChannel() == null) {
                Log.i("<小米>["+orderID+"]无法获取channel或order");
                this.renderState(1506, "cpOrderId 错误");
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.i("<小米>["+orderID+"]订单已经完成 state="+order.getState());
                this.renderState(200, "成功");
                return;
            }

            if (!order.getChannel().getCpAppID().equals(this.appId)) {
                Log.i("<小米>["+orderID+"] appid[" + this.appId + "]与设定appid[" + order.getChannel().getCpAppID()+"]不一致");
                this.renderState(1515, "AppId错误");
                return;
            }

            if (!"TRADE_SUCCESS".equals(orderStatus)) {
                Log.i("<小米>["+orderID+"] 返回的订单状态是失败状态 , orderStatus="+orderStatus);
                this.renderState(1525, "返回的订单状态是失败状态");
                return;
            }

            if (isValid(order.getChannel())) {
                order.setMoney(Float.valueOf(payFee).intValue());
                order.setChannelOrderID(this.orderId);
                order.setState(PayState.STATE_SUC);
                orderManager.saveOrder(order);
                SendAgent.sendCallbackToServer(this.orderManager, order);
                this.renderState(200, "成功");
            } else {
                order.setChannelOrderID(this.orderId);
                order.setState(PayState.STATE_FAILED);
                orderManager.saveOrder(order);
                this.renderState(1525, "signature 错误");
            }


        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.renderState(1525, "未知异常");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private boolean isValid(UChannel channel) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("appId=").append(this.appId).append("&")
                .append("cpOrderId=").append(this.cpOrderId).append("&");

        if (!TextUtils.isEmpty(this.cpUserInfo)) {
            sb.append("cpUserInfo=").append(this.cpUserInfo).append("&");
        }

        if (!TextUtils.isEmpty(this.orderConsumeType)) {
            sb.append("orderConsumeType=").append(this.orderConsumeType).append("&");
        }

        sb.append("orderId=").append(this.orderId).append("&")
                .append("orderStatus=").append(this.orderStatus).append("&");

        if (!TextUtils.isEmpty(partnerGiftConsume)) {
            sb.append("partnerGiftConsume=").append(this.partnerGiftConsume).append("&");
        }

        sb.append("payFee=").append(this.payFee).append("&")
                .append("payTime=").append(this.payTime).append("&")
                .append("productCode=").append(this.productCode).append("&")
                .append("productCount=").append(this.productCount).append("&")
                .append("productName=").append(this.productName).append("&")
                .append("uid=").append(this.uid);

        String vCode = HmacSHA1Encryption.HmacSHA1Encrypt(sb.toString(), channel.getCpAppSecret());

        Log.i("the valid str :: " + sb.toString());

        Log.i("The signature is " + signature + ";the vCode is " + vCode);
        return vCode.equals(signature);
    }

    private void renderState(int resultCode, String resultMsg) throws IOException {

        JSONObject json = new JSONObject();
        json.put("errcode", resultCode);
        json.put("errMsg", resultMsg);

        Log.d("The result to sdk is " + json.toString());

        this.response.getWriter().write(json.toString());


    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCpOrderId() {
        return cpOrderId;
    }

    public void setCpOrderId(String cpOrderId) {
        this.cpOrderId = cpOrderId;
    }

    public String getCpUserInfo() {
        return cpUserInfo;
    }

    public void setCpUserInfo(String cpUserInfo) {
        this.cpUserInfo = cpUserInfo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayFee() {
        return payFee;
    }

    public void setPayFee(String payFee) {
        this.payFee = payFee;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOrderConsumeType() {
        return orderConsumeType;
    }

    public void setOrderConsumeType(String orderConsumeType) {
        this.orderConsumeType = orderConsumeType;
    }

    public String getPartnerGiftConsume() {
        return partnerGiftConsume;
    }

    public void setPartnerGiftConsume(String partnerGiftConsume) {
        this.partnerGiftConsume = partnerGiftConsume;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
