package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.pp.RSAEncrypt;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import sun.misc.BASE64Decoder;

/**
 * PP充值回调
 * @author Lance Chow
 * @create 2016-01-06 16:19
 */
@Controller
@Scope("prototype")
@Namespace("/pay/pp")
public class PPPayCallbackAction extends UActionSupport {
    /**
     * pp助手参数
     */
    private String order_id;
    private String billno;
    private String account;
    private String amount;
    private int status;
    private String app_id;
    private String roleid;
    private int zone;
    private String sign;
    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback(){
        try {
            Log.i("<PP>充值回调开始： order_id=" +order_id+ ", billno=" +billno+ ", account=" +account+ ", amount=" +amount+ ", status=" +status+ ", app_id=" +app_id+
            ", roleid=" +roleid+ ", zone=" +zone+ ", sign=" +sign);

            Log.i("<PP>订单["+billno+"] 回调成功!");
            long orderID = Long.parseLong(billno);
            UOrder order = orderManager.getOrder(orderID);
            if (order==null){
                Log.i("<PP>订单["+billno+"]不存在");
                renderState(true);
                return;
            }

            if (order.getState() == PayState.STATE_SUC) {
                Log.i("<PP> 订单[" + billno+"] 重复");
                this.renderState(true);
                return;
            }
            UChannel channel = order.getChannel();
            //验证签名
            if (!isValid(channel)){
                renderState(true);
                return;
            }
            //验证appID
            if(!channel.getCpAppID().equals(app_id)){
                Log.i("<PP>订单["+billno+"]app_id["+app_id+"] 不一致,cpAppID="+channel.getCpAppID());
                renderState(true);
                return;
            }
            //验证角色ID
            if (!roleid.equals(order.getRoleID())){
                Log.i("<PP>订单["+billno+"]角色ID["+roleid+"]不一致,roleID="+order.getRoleID());
                renderState(true);
                return;
            }

            int d = (int)(Double.parseDouble(amount)*100);
            //验证金额
            if (d != order.getMoney() || d == 0){
                Log.i("<PP>订单["+billno+"]金额["+amount+"]不一致,money="+ order.getMoney());
                renderState(true);
                return;
            }
            //通知游戏服务器
            order.setChannelOrderID(order_id);
            SendAgent.sendCallbackToServer(this.orderManager, order);
            //充值正确
            Log.i("<PP>订单["+billno+"]充值成功!");
            renderState(true);
        }catch (Exception e){
            Log.e("error:",e);
            renderState(false);
        }
    }

    private boolean isValid(UChannel channel){
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            RSAEncrypt rsaEncrypt= new RSAEncrypt();
            byte[] dcDataStr = base64Decoder.decodeBuffer(sign);
            rsaEncrypt.loadPublicKey(channel.getCpPayKey());
            byte[] plainData = rsaEncrypt.decrypt(rsaEncrypt.getPublicKey(), dcDataStr);
            RSAEncrypt.byteArrayToString(plainData);
            String s = new String(plainData);
            JSONObject js = JSONObject.fromObject(s);
            long billno = Long.parseLong(js.getString("billno"));
            boolean flag = Long.parseLong(this.billno) == billno;
            if (!flag){
                Log.i("<PP>订单["+this.billno+"] 解密 orderID["+billno+"] 不匹配:"+s);
            }
            return flag;
        } catch (Exception e) {
            Log.i("<PP>订单["+billno+"] 解密异常["+sign+"]");
            e.printStackTrace();
        }
        return false;
    }

    private void renderState(boolean suc) {
        try {
            if (suc) {
                this.response.getWriter().write("success");
            } else {
                this.response.getWriter().write("fail");
            }
        }catch (Exception e){
            Log.e("错误!",e);
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
}
