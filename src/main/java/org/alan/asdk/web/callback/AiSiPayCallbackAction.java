package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.aisi.RSADecrypt;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 爱思充值回调
 *
 * @author Lance Chow
 * @create 2015-12-28 10:55
 */

@Controller
@Scope("prototype")
@Namespace("/pay/aisi")
public class AiSiPayCallbackAction extends UActionSupport {
    private String order_id;
    private String billno;
    private String account;
    private double amount;
    private int status;
    private String app_id;
    private String role;
    private int zone;
    private String sign;

    @Autowired
    private UOrderManager orderManager;


    @Action("payCallback")

    public void payCallback(){
        try {
            Log.i("<爱思>充值回调开始： order_id= " +order_id+ ", billno= " +billno+ ", account= " +account+ ", amount= " +amount+ ", status= " + status+ ", app_id= " +app_id+ ", role= " +role+ ", zone= " +zone+ ", sign= " +sign);
            long orderID = Long.parseLong(billno);
            UOrder order = orderManager.getOrder(orderID);
            if (order==null){
                Log.i("<爱思>订单["+billno+"]不存在");
                renderState(true);
                return;
            }
            //验证订单是否重复
            if (order.getState()!= PayState.STATE_PAYING){
                Log.i("<爱思>订单["+billno+"]重复");
                renderState(true);
                return;
            }
            UChannel channel = order.getChannel();
            //验证签名
            if (!isValid(channel)){
                Log.i("<爱思>订单["+billno+"]签名验证不成功!");
                renderState(true);
                return;
            }
            //验证APPOID
            if (!channel.getCpAppID().equals(app_id)){
                Log.i("<爱思>订单["+billno+"]appID["+app_id+"]不一致,chanelCpAppID="+channel.getCpAppID());
                renderState(true);
                return;
            }
            //验证角色ID
            if(!role.equals(order.getRoleID())){
                Log.i("<爱思>订单["+billno+"]角色ID["+role+"]不一致,roleID="+order.getRoleID());
                renderState(true);
                return;
            }
            //验证金额
            int d = (int)amount*100;
            if(d!=order.getMoney()){
                Log.i("<爱思>订单["+billno+"]爱思币["+amount+"]不一致,Money="+order.getMoney());
                renderState(true);
                return;
            }
            //充值正确
            SendAgent.sendCallbackToServer(orderManager,order);
            Log.i("<爱思>订单["+billno+"]充值完成");
            renderState(true);
        }catch (Exception e){
            Log.e("<爱思>订单["+billno+"]充值错误!",e);
            renderState(false);
        }
    }



    public boolean isValid(UChannel channel) {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            String publicKey = channel.getCpPayKey();
            sign = sign.replace("\\n","\n");
            byte[] dcDataStr = base64Decoder.decodeBuffer(sign);
            byte[] plainData = RSADecrypt.decryptByPublicKey(dcDataStr, publicKey);
            String [] data = new String(plainData).split("&");
            Map<String,String> params = new HashMap<String, String>();
            for (int i = 0; i <data.length; i++) {
                String [] s = data[i].split("=");
                params.put(s[0] , s[1]);
            }
            if (billno.equals(params.get("billno"))){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 响应SDK
     */
    public void renderState(boolean success) {
        try {
            if (success) {
                this.response.getWriter().write("success");
            } else {
                this.response.getWriter().write("success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
