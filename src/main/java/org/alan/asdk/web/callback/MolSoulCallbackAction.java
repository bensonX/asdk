package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.cache.impl.logic.UChannelCache;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.SDKVerifyResult;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.service.UUserManager;
import org.alan.asdk.utils.EncryptUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * mol疾风剑魂回调地址
 *
 * @author Lance Chow
 * @create 2016-08-19 10:42
 */
@Controller
@Scope("prototype")
@Namespace("/pay/molSoul")
public class MolSoulCallbackAction extends UActionSupport {

    private String orderID;
    private int amount;
    private String userID;
    private String roleID;
    private int channelID;
    private String serverName;
    private String serverID;
    private int diamond;
    private String channelOrderID;
    private String extension;
    private String sign;

    @Autowired
    private UOrderManager orderManager;
    @Autowired
    private UUserManager userManager;


    @Action("payCallback")
    public void payCallback(){
        UChannel channel = UChannelCache.getInstance().get(channelID);
        if (channel == null){
            Log.i("疾风剑魂MOL充值 orderID= "+orderID+" channelID =" +channelID +"不存在!");
            renderState(0,"channelID不存在");
            return;
        }
        if(!valid(channel)){
            renderState(0,"签名验证不成功!");
            return;
        }
        UUser user = userManager.getUserByCpID(channel.getAppID(),channel.getChannelID(),userID);
        if (user == null){
            SDKVerifyResult result = new SDKVerifyResult();
            result.setUserID(userID);
            result.setUserName(userID);
            result.setNickName(userID);
            user = userManager.generateUser(channel, result);
            Log.i("Soul MOL orderID= "+orderID+" userID =" +userID +"not define , created!");
        }
        //创建订单
        UOrder order = orderManager.generateOrderByID(user,amount,roleID,"#unknow#",serverID,serverName,serverName,diamond,Long.parseLong(orderID));
        order.setOrderID(Long.parseLong(orderID));
        order.setChannelOrderID(channelOrderID);
        order.setExtension(extension);
        //回调游戏
        SendAgent.sendCallbackToServer(this.orderManager, order);
        Log.i("Soul MOL charge complate orderID= "+orderID);
        renderState(1,"充值完成!");
    }

    private boolean valid(UChannel channel){
        String sb = "state=" + 1 +
                "&channelID=" + channel.getChannelID() +
                "&money=" + amount +
                "&orderID=" + orderID +
                "&userID=" + userID +
                "&aa13dac8701292b48764f0d8aa3c93f8";
        String sign =  EncryptUtils.md5(sb.toString());
        boolean flag = this.sign.equalsIgnoreCase(sign);
        if(!flag){
            Log.i("sign fail! orderID ="+orderID +",sign = "+sign +", this.sign = "+this.sign +",str = "+sb.toString());
        }
        return flag;
    }
    private void renderState(int state, String msg) {
        JSONObject json = new JSONObject();
        json.put("state", state);
        json.put("data", msg);
        super.renderJson(json.toString());
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }


    public String getChannelOrderID() {
        return channelOrderID;
    }

    public void setChannelOrderID(String channelOrderID) {
        this.channelOrderID = channelOrderID;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
