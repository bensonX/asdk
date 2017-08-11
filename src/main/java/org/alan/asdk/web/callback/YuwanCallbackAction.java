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

/**
 * 鱼丸充值回调
 *
 * @author Lance Chow
 * @create 2016-07-06 9:58
 */
@Controller
@Scope("prototype")
@Namespace("/pay/yuwan")
public class YuwanCallbackAction extends UActionSupport {
    private String serverid;
    private String custominfo;
    private String openid;
    private String ordernum;
    private String status;
    private String paytype;
    private String amount;
    private String errdesc;
    private String paytime;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback(){
        Log.i("<鱼丸>充值回调开始 : " +"serverid=" + serverid + ", custominfo=" + custominfo + ", openid=" + openid + ", ordernum=" + ordernum + ", status=" + status + ", paytype=" + paytype + ", amount=" + amount + ", errdesc=" + errdesc + ", paytime=" + paytime , "sign="+sign);
        try{
            long orderID = Long.parseLong(custominfo);
            UOrder order = orderManager.getOrder(orderID);
            if (order == null || order.getChannel() == null){
                Log.i("<鱼丸>订单["+custominfo+"]充值错误 , 订单不存在!");
                renderState(false);
                return;
            }
            //验签
            if(!isValid(order.getChannel())){
                renderState(false);
                return;
            }
            if (!"1".equals(status)){
                Log.i("<鱼丸>订单["+custominfo+"]充值错误 ,订单状态错误 status = " + status);
                renderState(false);
                return;
            }
            if (order.getState() == PayState.STATE_COMPLETE){
                Log.i("<鱼丸>订单["+custominfo+"]充值错误 ,订单已经完成");
                renderState(true);
                return;
            }
            //验证金额
            int money = Integer.parseInt(amount);
            if (order.getMoney()!= money){
                Log.i("<鱼丸>订单["+custominfo+"]充值错误 , 金额不一致 money = " + money+ ", order.getMoney = "+ order.getMoney());
                renderState(false);
                return;
            }
            renderState(true);
            order.setChannelOrderID(ordernum);
            SendAgent.sendCallbackToServer(this.orderManager, order);
            Log.i("<鱼丸>订单["+custominfo+"]充值成功");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void renderState(boolean b) {
        try {
            response.getWriter().write(b?"1":"0");
        }catch (Exception e){
            Log.e("错误!",e);
        }
    }

    private boolean isValid(UChannel channel) {
        String str = new StringBuilder()
                .append(serverid) .append("|")
                .append(custominfo) .append("|")
                .append(openid) .append("|")
                .append(ordernum) .append("|")
                .append(status) .append("|")
                .append(paytype) .append("|")
                .append(amount) .append("|")
                .append(errdesc) .append("|")
                .append(paytime).append("|").append(channel.getCpConfig()).toString();
        String md5 = EncryptUtils.md5(str);
        if (!sign.equalsIgnoreCase(md5)){
            Log.i("<鱼丸>订单["+custominfo+"]签名验证不成功 , str = " +str +" , md5 = "+ md5 + " sign = " + sign);
        }
        return sign.equalsIgnoreCase(md5);
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getCustominfo() {
        return custominfo;
    }

    public void setCustominfo(String custominfo) {
        this.custominfo = custominfo;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getErrdesc() {
        return errdesc;
    }

    public void setErrdesc(String errdesc) {
        this.errdesc = errdesc;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
