package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.RSAUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * 华为支付回调通知接口
 * Created by ant on 2015/4/27.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/huawei")
public class HuaWeiPayCallbackAction extends UActionSupport {


    private String result;        //string(3)  支付结果：“0”：支付成功“1”：退款成功（暂未启用）
    private String userName;        //string  开发者社区用户名或联盟用户编号；终端sdk上报了联盟用户编号
    private String productName;        //string(100)  商品名称；对应APK填写的productName
    private String payType;        //int
    private String amount;        //string(10)  商品支付金额 (格式为：元.角分，最小精确到分，例如：20.01)
    private String orderId;        //string(50)  华为订单号，为华为支付平台生成
    private String notifyTime;        //string(15)  通知时间，自1970年1月1日0时起的毫秒数
    private String requestId;        //string(30)  开发者支付请求ID
    private String bankId;        //string(50)  银行编码-支付通道信息，传递条件如下：只有在有具体取值时才传递
    private String orderTime;        //string  下单时间 yyyy-MM-dd hh:mm:ss；仅在sdk中指定了urlver为2 时有效。
    private String tradeTime;        //string  交易/退款时间 yyyy-MM-dd hh:mm:ss；仅在sdk中指定了urlver为2时有效。
    private String accessMode;        //string  接入方式：仅在sdk 中指定了urlver为2时有效。
    private String spending;        //string  渠道开销，保留两位小数，单位元。仅在sdk中指定了urlver为2时有效。
    private String extReserved;        //string  商户侧保留信息，原样返回商户调用支付sdk
    private String sign;        //string  RSA签名。

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<华为>充值回调开始： result=" +result+ ", userName=" +userName+ ", productName=" +productName+ ", payType=" +payType+ ", amount=" +amount+ ", orderId=" +orderId+ ", notifyTime=" +notifyTime+ ", requestId=" +requestId+ ", bankId=" +bankId+ ", orderTime=" +orderTime+ ", tradeTime=" +tradeTime+ ", accessMode=" + accessMode+ ", spending=" +spending+ ", extReserved=" +extReserved+ ", sign=" +sign);

            long localOrderID = Long.parseLong(requestId);

            UOrder order = orderManager.getOrder(localOrderID);

            if (order == null || order.getChannel() == null) {
                Log.d("<华为>订单或渠道不存在.");
                this.renderState(3, "notifyId 错误");
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.d("<华为>订单完成状态：" + order.getState());
                this.renderState(0, "订单已经被处理,或者CP订单号重复");
                return;
            }

            if (!"0".equals(result)) {
                Log.d("<华为>订单返回失败. " + this.result);
                this.renderState(3, "支付平台返回的是失败订单");
                return;
            }

            if (isValid(order.getChannel())) {
                order.setChannelOrderID(orderId);
                order.setState(PayState.STATE_SUC);
                orderManager.saveOrder(order);
                SendAgent.sendCallbackToServer(this.orderManager, order);
                this.renderState(0, "");
            } else {
                order.setChannelOrderID(orderId);
                order.setState(PayState.STATE_FAILED);
                orderManager.saveOrder(order);
                this.renderState(1, "Sign错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.renderState(94, "未知错误");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean isValid(UChannel channel) {

        StringBuilder sb = new StringBuilder();
        sb.append("accessMode=").append(accessMode).append("&")
                .append("amount=").append(amount).append("&")
                .append("bankId=").append(bankId).append("&")
                .append("extReserved=").append(extReserved).append("&")
                .append("notifyTime=").append(notifyTime).append("&")
                .append("orderId=").append(orderId).append("&")
                .append("orderTime=").append(orderTime).append("&")
                .append("payType=").append(payType).append("&")
                .append("productName=").append(productName).append("&")
                .append("requestId=").append(requestId).append("&")
                .append("result=").append(result).append("&")
                .append("spending=").append(spending).append("&")
                .append("tradeTime=").append(tradeTime).append("&")
                .append("userName=").append(userName);


        return RSAUtils.verify(sb.toString(), this.sign, channel.getCpPayKey(), "UTF-8");

    }

    private void renderState(int code, String msg) throws IOException {

        this.response.getWriter().write(code);

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public String getSpending() {
        return spending;
    }

    public void setSpending(String spending) {
        this.spending = spending;
    }

    public String getExtReserved() {
        return extReserved;
    }

    public void setExtReserved(String extReserved) {
        this.extReserved = extReserved;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
