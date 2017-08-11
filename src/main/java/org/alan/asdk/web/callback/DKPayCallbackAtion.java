package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
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

import java.util.Objects;

/**
 * Created on 2017/4/21.
 *
 * @author Chow
 * @since 1.0
 */
@Controller
@Scope("prototype")
@Namespace("/pay/dkm")
public class DKPayCallbackAtion  extends UActionSupport {

    private String order_id;
    private String out_transaction_id;
    private String pay_type;
    private String uid;
    private String product_id;
    private String money;
    private String role_id;
    private String server_id;
    private String partner_id;
    private String ext;
    private String time;
    private String sign;


    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try{
            Log.i("哆可梦充值开始:参数: "+"order_id="+order_id+"&uid=" + uid +"&product_id="+ product_id+"&money="
                    + money+"&role_id=" + role_id+"&server_id=" + server_id+"&partner_id=" + partner_id+
                    "&ext=" + ext + "&time="+time+"&sign="+sign);
            long orderId = Integer.parseInt(ext);
            UOrder order = orderManager.getOrder(orderId);
            if (order == null){
                Log.i("<哆可梦>充值失败 , order不存在 , orderId = "+orderId);
                renderState(true,"orderId不存在");
                return;
            }
            if (order.getState() == PayState.STATE_SUC){
                Log.i("<哆可梦>充值回调错误 , orderID = "+orderId +", order状态为已完成");
                renderState(true,"order状态为已完成");
                return;
            }
            //验证签名
            String src = order_id + uid + product_id + money + role_id + server_id + partner_id + ext + time + order.getChannel().getCpAppSecret();
            String c = EncryptUtils.md5(src);
            if (!Objects.equals(sign, c)){
                Log.i("<哆可梦>充值回调错误 , orderID = "+orderId +", 签名错误 sign = "+sign+", c = "+c+" , src = "+src);
                renderState(true,"签名错误");
                return;
            }
            //验证金额
            int d = (int)(Double.parseDouble(money)*100);
            if (order.getMoney()!=d){
                Log.i("<哆可梦>充值回调警告 , orderID = "+orderId +", 充值金额("+d+")!=订单金额("+order.getMoney()+")");
                renderState(true,"充值金额错误");
                order.setProductName("com.muzhiyouwan.bzddt_any");
                order.setMoney(d);
            }
            //验证角色ID
            if(!Objects.equals(order.getRoleID(),role_id)){
                Log.i("<哆可梦>充值回调错误 , orderID = "+orderId +", 角色ID不一致 当前角色ID("+role_id+")!=订单角色("+order.getRoleID()+")");
                return;
            }
            //发奖
            order.setChannelOrderID(order_id);
            order.setExtension(out_transaction_id);
            SendAgent.sendCallbackToServer(this.orderManager, order);
            renderState(true,"充值成功");
            Log.i("<哆可梦>充值回调成功 , orderID = "+orderId);
        }catch (Exception e){
             e.printStackTrace();
        }

    }

    private void renderState(boolean suc , String msg) {
        try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ret",suc?1:0);
                jsonObject.put("msg",msg);
                this.response.getWriter().write(jsonObject.toString());
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

    public String getOut_transaction_id() {
        return out_transaction_id;
    }

    public void setOut_transaction_id(String out_transaction_id) {
        this.out_transaction_id = out_transaction_id;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
