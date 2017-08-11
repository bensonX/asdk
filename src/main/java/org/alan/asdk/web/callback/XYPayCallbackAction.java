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
import org.springframework.util.StringUtils;

import java.io.PrintWriter;

/**
 * Created by Administrator on 2015-12-07.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/xy")
public class XYPayCallbackAction extends UActionSupport {

    private String orderid;
    private String uid;
    private String serverid;
    private String amount;
    private String extra;
    private String ts;
    private String sign;
    private String sig;
    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback(){
        try {
            Log.i("<XY>充值回调开始： orderid=" +orderid+ ", uid=" +uid+ ", serverid=" +serverid+ ", amount=" +amount+ ", extra=" +extra+ ", ts=" +ts+ ", sign=" +sign+ ", sig=" +sig);

            Log.i("<XY>订单["+extra+"] 回调成功!");
            long orderID = Long.parseLong(extra);
            UOrder order = orderManager.getOrder(orderID);
            if (order==null){
                Log.i("<XY> 订单["+extra+"]不存在");
                renderState(true);
                return;
            }
            if (order.getState() == PayState.STATE_SUC) {
                Log.i("<XY> 订单[" + extra+"] 重复");
                this.renderState(true);
                return;
            }
            UChannel channel = order.getChannel();
            if (!isValid(channel)){
                renderState(true);
                return;
            }
            double d  = (int) (Double.parseDouble(amount)*100);
            if (d==0 || d!=order.getMoney()){
                Log.i("<XY> 订单["+extra+"] amount["+d+"] 与金额["+order.getMoney()+"]不一致 ");
                renderState(true);
                return;
            }

            renderState(true);
            order.setChannelOrderID(orderid);
            SendAgent.sendCallbackToServer(this.orderManager, order);
            Log.i("<XY> 订单["+extra+"] 充值成功!");

        }catch (Exception e){
            Log.i("<XY> 订单["+extra+"] 失败 , 逻辑错误:" +e.getMessage());
        }

    }

    private boolean isValid(UChannel channel){
        String appKey = channel.getCpAppKey();
        String payKey = channel.getCpPayKey();
        //验证APPKEY是否正确
        StringBuilder sb = new StringBuilder();
        sb.append(appKey).append("amount=")
                .append(amount).append("&extra=")
                .append(extra).append("&orderid=")
                .append(orderid).append("&serverid=")
                .append(serverid).append("&ts=")
                .append(ts).append("&uid=")
                .append(uid);
        Log.i(sb.toString());
        String rightAppKey = EncryptUtils.md5(sb.toString());
        if (!rightAppKey.equals(sign)){
            Log.i("<XY> 订单["+extra+"] 签名(sign)["+sign+"] 不一致:"+rightAppKey);
            return false;
        }
        if (StringUtils.isEmpty(payKey)){
            sb = new StringBuilder();
            sb.append(payKey).append("amount=")
                    .append(amount).append("&extra=")
                    .append(extra).append("&orderid=")
                    .append(orderid).append("&serverid=")
                    .append(serverid).append("&ts=")
                    .append(ts).append("&uid=")
                    .append(uid);
            rightAppKey = EncryptUtils.md5(sb.toString());
            if (!rightAppKey.equals(sig)){
                Log.i("<XY> 订单["+extra+"] 签名(sig)["+sig+"] 不一致:"+rightAppKey);
                return false;
            }
        }
        return true;
    }
    private void renderState(boolean suc) {
        try {
            PrintWriter writer = response.getWriter();
            if (suc) {
                writer.write("success");
            } else {
                writer.write("fail");
            }
        }catch (Exception e){
            Log.e("错误!",e);
        }
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }


    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public UOrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(UOrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
