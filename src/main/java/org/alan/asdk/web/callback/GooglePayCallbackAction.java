package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UGoods;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.google.RSAUtils;
import org.alan.asdk.service.UGoodsManager;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 谷歌充值回调地址
 *
 * @author Lance Chow
 * @create 2016-06-03 11:08
 */
@Controller
@Scope("prototype")
@Namespace("/pay/google")
public class GooglePayCallbackAction extends UActionSupport {

    private String orderID;
    private String data;
    private String dataSignature;


    @Autowired
    private UGoodsManager goodsManager;
    @Autowired
    private UOrderManager orderManager;

    @Action("verify")
    public void payCallback() {
        Log.i("<google>Verify the start:orderID = "+orderID+" , data = " +data +"\r\n , dataSignature = " +dataSignature);
        if (orderID==null){
            renderState(false, "orderID is invalid");
            return;
        }
        long orderID = Long.parseLong(this.orderID);
        UOrder order = orderManager.getOrder(orderID);
        if (order==null){
            Log.i("<Google> order[" + orderID + "] , orderID isEmpty.");
            renderState(false, "orderID is invalid");
            return;
        }

        if (order.getState() == PayState.STATE_COMPLETE) {
            Log.i("<Google> order[" + orderID + "] order is old"); //充值已经完成 , 订单重复
            renderState(false, "order is invaild.");
            return;
        }
        UChannel channel = order.getChannel();

        if (channel==null){
            Log.i("<Google> order[" + orderID + "] Channel is error");
            renderState(false, "channel is invaild.");
            return;
        }

        if(!isValid(channel)){
            Log.i("<Google> order[" + orderID + "] pay success , Order data cannot be verified"); //充值已经完成 , 订单数据不能通过验证
            renderState(false, "order is invaild.");
            return;
        }
        JSONObject jo = JSONObject.fromObject(data);
        String productID = jo.getString("productId");
        //验证金额
        UGoods goods = goodsManager.getGoodsBySdkGoodsID(channel.getGoodsConfigID(),productID);
        if (goods == null){
            Log.i("<Google> order[" + orderID + "] goods error , productID = " +productID);
            renderState(false, "money is invaild.");
            return;
        }

        if (goods.getMoney()!= order.getMoney()){
            Log.i("<Google> order[" + orderID + "] money is inValid goods.getMoney = " + goods.getMoney() +" , order.getMoney =" + order.getMoney()); //金额不一致
            renderState(false, "money is invaild.");
            return;
        }

        order.setChannelOrderID(jo.getString("orderId"));
        SendAgent.sendCallbackToServer(orderManager,order);
        Log.i("<Google> order[" + orderID + "] pay success!"); //充值成功!
        renderState(true,"success");

    }

    private boolean isValid(UChannel channel) {
        return RSAUtils.verifyPurchase(channel.getCpPayKey(),data,dataSignature);
    }
    public void renderState(boolean success, String resultMsg) {
        try {
            JSONObject json = new JSONObject();
            json.put("success", success);
            json.put("msg", resultMsg);
            renderJson(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage());
        }
    }
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataSignature() {
        return dataSignature;
    }

    public void setDataSignature(String dataSignature) {
        this.dataSignature = dataSignature;
    }
}
