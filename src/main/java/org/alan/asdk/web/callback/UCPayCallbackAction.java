package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.uc.PayCallbackResponse;
import org.alan.asdk.sdk.uc.UCSDK;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.JsonUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;


/**
 * UC渠道的支付回调请求处理
 */
@Controller
@Scope("prototype")
@Namespace("/pay/uc")
public class UCPayCallbackAction extends UActionSupport {

    @Autowired
    private UOrderManager orderManager;

    private int u8ChannelID;

    @Action("payCallback")
    public void payCallback() {

        try {
            BufferedReader br = this.request.getReader();
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }

            Log.i("<UC> 回调开始:" + sb.toString());

            PayCallbackResponse rsp = (PayCallbackResponse) JsonUtils.decodeJson(sb.toString(), PayCallbackResponse.class);

            if (rsp == null) {
                this.renderState(false);
                return;
            }

            long orderID = Long.parseLong(rsp.getData().getCallbackInfo());
            UOrder order = orderManager.getOrder(orderID);

            if (order == null || order.getChannel() == null) {
                Log.i("<UC>["+orderID+"]无法获取channel或order");
                this.renderState(false);
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.i("<UC>["+orderID+"]订单已经完成 state="+order.getState());
                this.renderState(true);
                return;
            }


            UCSDK sdk = new UCSDK();

            if (!sdk.verifyPay(order.getChannel(), rsp)) {
                Log.i("<UC>["+orderID+"]订单已经完成 state="+order.getState());
                this.renderState(true);
                return;
            }

            if ("S".equals(rsp.getData().getOrderStatus())) {
                float money = Float.parseFloat(rsp.getData().getAmount());
                int moneyInt = (int) (money * 100);  //以分为单位
                if(moneyInt!=order.getMoney()){
                    renderState(true);
                    Log.i("<UC>["+orderID+"]金额不一致 moneyInt =" +moneyInt +",order.getMoney = "+order.getMoney());
                    return;
                }
                order.setChannelOrderID(rsp.getData().getOrderId());
                order.setState(PayState.STATE_SUC);

                orderManager.saveOrder(order);

                SendAgent.sendCallbackToServer(this.orderManager, order);


            } else {
                order.setChannelOrderID(rsp.getData().getOrderId());
                order.setState(PayState.STATE_FAILED);
                orderManager.saveOrder(order);
            }


            renderState(true);

        } catch (Exception e) {
            try {
                this.renderState(false);
            } catch (Exception e2) {
                Log.e(e2.getMessage());
            }

            Log.e(e.getMessage());

        }

    }

    private void renderState(boolean suc) throws IOException {

        String res = "SUCCESS";
        if (!suc) {
            res = "FAILURE";
        }

        this.response.getWriter().write(res);
    }

    public int getU8ChannelID() {
        return u8ChannelID;
    }

    public void setU8ChannelID(int u8ChannelID) {
        this.u8ChannelID = u8ChannelID;
    }
}


