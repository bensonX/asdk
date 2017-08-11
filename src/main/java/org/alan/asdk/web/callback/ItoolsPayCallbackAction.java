package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.itools.RSASignature;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * ITools支付回调
 * Created by ant on 2015/8/10.
 */

@Controller
@Scope("prototype")
@Namespace("/pay/itools")
public class ItoolsPayCallbackAction extends UActionSupport {

    private String notify_data;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<iTools>充值回调开始： notify_data=" +notify_data+ ", sign=" +sign);

            String r = RSASignature.decrypt(this.notify_data);
            JSONObject json = JSONObject.fromObject(r);
            String order_id_com = json.getString("order_id_com");
            String amount = json.getString("amount");
            String order_id = json.getString("order_id");
            String result = json.getString("result");

            Log.i("<iTools> 订单["+order_id_com+"] 回调成功");
            if (!RSASignature.verify(r, sign)){
                Log.i("<iTools> 订单["+order_id_com+"] 效验失败");
                renderState(true);
                return;
            }
            if (!result.equals("success")){
                Log.i("<iTools> 订单["+order_id_com+"] 状态错误:"+result);
                this.renderState(true);
                return;
            }

            UOrder order = orderManager.getOrder(Long.parseLong(order_id_com));

            if (order==null){
                Log.i("<iTools> 订单["+order_id_com+"] 订单不存在");
                renderState(true);
                return;
            }

            if (order.getState()!= PayState.STATE_PAYING){
                Log.i("<iTools>订单["+order_id_com+"]重复");
                renderState(true);
                return;
            }

            int d = (int) (Double.parseDouble(amount)*100);

            if (d == 0 || d != order.getMoney()){
                Log.i("<iTools> 订单["+order_id_com+"] 金额["+d+"] 不一致 money="+order.getMoney());
                renderState(true);
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.d("<iTools> 订单["+order_id_com+"]重复!");
                this.renderState(true);
                return;
            }

            order.setChannelOrderID(order_id);
            order.setState(PayState.STATE_SUC);
            orderManager.saveOrder(order);
            Log.d("<iTools> 订单["+order_id_com+"] 充值成功!");
            this.renderState(true);
            SendAgent.sendCallbackToServer(this.orderManager, order);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                Log.d("<iTools> 订单  逻辑错误 :" +sign);
                this.renderState(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void renderState(boolean suc) throws IOException {

        if (suc) {
            this.response.getWriter().write("success");
        } else {
            this.response.getWriter().write("fail");
        }

    }

    public String getNotify_data() {
        return notify_data;
    }

    public void setNotify_data(String notify_data) {
        this.notify_data = notify_data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
