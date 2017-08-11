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
 * 当乐渠道的支付回调请求处理
 * Created by ant on 2015/2/9.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/downjoy")
public class DownjoyPayCallbackAction extends UActionSupport {

    private int result; //支付结果，固定值。“1”代表成功，“0”代表失败
    private String money;  //支付金额，单位：元。//这里money使用String类型，不要使用float类型，否则导致精度丢失，生成md5可能出错
    private String order;//本次支付的订单号。
    private String mid;   //本次支付用户的乐号，既登录后返回的 mid 参数。
    private String time;//时间戳，格式：yyyymmddHH24mmss 月日小时分秒小于 10 前面补充 0
    private String signature;//MD5 验证串，用于与接口生成的验证串做比较，保证计费通知的合法性。
    private String ext;      //发起支付请求时传递的 eif 参数，此处原样返回。

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {

        try {
            Log.i("<当乐>充值回调开始： result=" +result+ ", money=" +money+ ", order=" +order+ ", mid=" +mid+ ", time=" +time+ ", signature=" +signature+ ", ext=" +ext);

            Log.i("<当乐> 订单["+ext+"] 回调成功");

            long orderID = Long.parseLong(ext);

            UOrder order = orderManager.getOrder(orderID);

            if (order == null || order.getChannel() == null) {
                Log.i("<当乐> 订单["+ext+"] 不存在");
                this.renderState(false);
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.i("<当乐> 订单["+ext+"] 重复");
                this.renderState(true);
                return;
            }

            if (this.result != 1) {
                Log.i("<当乐> 订单["+ext+"] 支付结果错误:"+result);
                this.renderState(true);
                return;
            }
            if (isValid(order.getChannel())) {
                order.setMoney((int) (Float.valueOf(money) * 100));
                order.setChannelOrderID(this.order);
                Log.i("<当乐> 订单["+ext+"] 充值成功");
                SendAgent.sendCallbackToServer(this.orderManager, order);
                this.renderState(true);
            } else {
                order.setChannelOrderID(this.order);
                order.setState(PayState.STATE_FAILED);
                orderManager.saveOrder(order);
                this.renderState(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.renderState(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private boolean isValid(UChannel channel) {
        StringBuilder sb = new StringBuilder();
        sb.append("order=").append(order)
                .append("&money=").append(money)
                .append("&mid=").append(mid)
                .append("&time=").append(time)
                .append("&result=").append(result)
                .append("&ext=").append(ext)
                .append("&key=").append(channel.getCpPayKey());

        String vCode = EncryptUtils.md5(sb.toString());

        if(!vCode.equals(signature)){
            Log.i("<当乐> 订单["+ext+"] 签名("+sb.toString()+")["+signature+"] 错误:"+vCode);
        }
        return vCode.equals(signature);
    }

    private void renderState(boolean suc) throws IOException {

        if (suc) {
            this.response.getWriter().write("success");
        } else {
            this.response.getWriter().write("failure");
        }


    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
