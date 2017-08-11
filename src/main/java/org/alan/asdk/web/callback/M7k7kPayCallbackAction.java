package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
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
 * 7k支付回调
 *
 * @author Lance Chow
 * @create 2016-04-25 11:30
 */
@Controller
@Scope("prototype")
@Namespace("/pay/7k7k")
public class M7k7kPayCallbackAction extends UActionSupport {

    private String uid;
    private String cporder;
    private double money;
    private String order;
    private String cpappid;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {

        //获取订单号
        try {
            Log.i("<7k7k>充值回调开始： uid=" +uid+ ", cporder=" +cporder+ ", money=" +money+ ", order=" +order+ ", cpappid=" +cpappid+ ", sign=" +sign);

            long orderID = Long.parseLong(cporder);
            UOrder order = orderManager.getOrder(orderID);
            if (order==null || order.getChannel() == null){
                Log.i("<7k7k>["+orderID+"]无法找到渠道或者订单对象");
                renderText("fail");
                return;
            }
            if (order.getState() == PayState.STATE_SUC) {
                Log.i("<7k7k> 订单[" + orderID+"] 重复");
                renderText("fail");
                return;
            }
            if (!order.getChannel().getCpAppID().equals(cpappid)){
                Log.i("<7k7k>["+orderID+"]CpAppID["+order.getChannel().getCpAppID()+"]与提供的cpappid["+cpappid+"]不一致");
                renderText("fail");
                return;
            }
            //验证签名
            if (!isValid(order)){
                renderText("fail");
                return;
            }
            //验证金额
//            int d = (int)(money*100);
//            if(order.getMoney()!=d || d ==0){
//                Log.i("<7k7k>["+orderID+"]订单金额["+order.getMoney()+"]与提供的金额["+money+"]不一致");
//                renderText("success");
//                return;
//            }
            //通知游戏服务器
            order.setChannelOrderID(this.order);
            SendAgent.sendCallbackToServer(orderManager,order);
            Log.i("<7k7k>["+orderID+"]CpAppID["+order.getChannel().getCpAppID()+"]充值成功");

        } catch (Exception e) {
            e.printStackTrace();
        }

        renderText("success");
    }

    private boolean isValid(UOrder order){
        int _money = new Double(money).intValue();
        String e = uid+cporder+_money+this.order+cpappid+order.getChannel().getCpAppSecret();

       String md5 = EncryptUtils.md5(e);
        if (!md5.equals(sign)){
            Log.i("<7k7k>["+order.getOrderID()+"]签名["+md5+"](原文:"+e+")与提供的签名["+sign+"]不一致");
            return false;
        }
        return true;
    }


    /**
     * geter seter
     **/

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCporder() {
        return cporder;
    }

    public void setCporder(String cporder) {
        this.cporder = cporder;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCpappid() {
        return cpappid;
    }

    public void setCpappid(String cpappid) {
        this.cpappid = cpappid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
