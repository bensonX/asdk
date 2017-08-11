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
 * 搜狗SDK
 *
 * @author Lance Chow
 * @create 2016-06-29 16:26
 */
@Controller
@Scope("prototype")
@Namespace("/pay/sougo")
public class SougoPayCallbackAction extends UActionSupport {

    private String gid;
    private String sid;
    private String uid;
    private String role;
    private String oid;
    private String date;
    private String amount1;
    private String amount2;
    private String time;
    private String appdata;
    private String realAmount;
    private String auth;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void  payCallback(){
        Log.i("<搜狗>充值回调开始： gid=" +gid+ ", sid=" +sid+ ", uid=" +uid+ ", role=" +role+ ", oid=" +oid+ ", date=" +date+ ", amount1=" +amount1+ ", amount2=" +amount2+ ", time=" +time+ ", appdata=" +appdata+ ", realAmount=" +realAmount+ ", auth=" +auth);

        //Log.i("<搜狗>订单["+appdata+"]回调开始");

        long orderID = Long.parseLong(appdata);
        UOrder order = orderManager.getOrder(orderID);
        UChannel channel = order.getChannel();
        //验签
        if (!isValid(channel)){
            renderText("ERR_200");//验证失败
            return;
        }
        //判断订单是否重复
        if (order.getState() == PayState.STATE_COMPLETE){
            renderText("OK");
            Log.i("<搜狗>订单["+appdata+"]:充值失败 , 订单重复!");
            return;
        }
        //判断金额是否相等
        int realAmount = Integer.parseInt(this.realAmount);
        if (realAmount*100 != order.getMoney()){
            renderText("ERR_200");//验证失败
            Log.i("<搜狗>订单["+appdata+"]:充值失败 , 金额不一致! realAmount = "+ this.realAmount + ", money = " + order.getMoney());
            return;
        }
        //验证gid
        if (!gid.equals(channel.getCpAppID())){
            renderText("ERR_200");//验证失败
            Log.i("<搜狗>订单["+appdata+"]:充值失败 , GID不一致! gid = "+ this.gid + ", cpAppID = " + channel.getCpAppID());
            return;
        }
        //通知发货
        renderText("OK");
        order.setChannelOrderID(oid);
        SendAgent.sendCallbackToServer(orderManager,order);
        Log.i("<搜狗>订单["+appdata+"]:充值成功!");

    }

    private boolean isValid(UChannel channel) {
        String str = "amount1=" + amount1 +
                "&amount2=" + amount2 +
                "&appdata=" + appdata +
                "&date=" + date +
                "&gid=" + gid +
                "&oid=" + oid +
                "&realAmount=" + realAmount +
                "&role=" + role +
                "&sid=" + sid +
                "&time=" + time +
                "&uid=" + uid +"&"+ channel.getCpPayKey();
        String md5 = EncryptUtils.md5(str);
        boolean isValid = md5.equalsIgnoreCase(auth);
        if (!isValid){
            Log.i("<搜狗>订单["+appdata+"]验签失败 , 字符 = "+str +", md5 ="+md5 +" , auth = "+auth);
        }
        return isValid;
    }

    @Override
    protected void renderText(String text){
        try {
            this.response.getWriter().write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount1() {
        return amount1;
    }

    public void setAmount1(String amount1) {
        this.amount1 = amount1;
    }

    public String getAmount2() {
        return amount2;
    }

    public void setAmount2(String amount2) {
        this.amount2 = amount2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppdata() {
        return appdata;
    }

    public void setAppdata(String appdata) {
        this.appdata = appdata;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
