package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.coolpad.CoolPadPayResult;
import org.alan.asdk.sdk.coolpad.SignHelper;
import org.alan.asdk.service.UChannelManager;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.JsonUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * 酷派支付回调
 * Created by ant on 2015/9/15.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/coolpad")
public class CoolPadPayCallbackAction extends UActionSupport {


    private String transdata;
    private String sign;
    private String signtype;

    private int u8ChannelID;

    @Autowired
    private UOrderManager orderManager;

    @Autowired
    private UChannelManager channelManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<酷派>充值回调开始： transdata=" +transdata+ ", sign=" +sign+ ", signtype=" +signtype+ ", u8ChannelID=" +u8ChannelID);

            UChannel channel = channelManager.queryChannel(this.u8ChannelID);
            if (channel == null) {
                Log.e("<酷派>渠道不存在.channelID:" + this.u8ChannelID + ";data:" + this.transdata);
                return;
            }

            if (!isSignOK(channel)) {
                Log.d("<酷派>签名验证失败.sign:%s;appKey:%s;transdata:%s", sign, channel.getCpPayKey(), transdata);
                this.renderState(false);
                return;
            }

            CoolPadPayResult result = (CoolPadPayResult) JsonUtils.decodeJson(this.transdata, CoolPadPayResult.class);

            if (result == null) {
                Log.e("<酷派>数据解析错误...");
                this.renderState(false);
                return;
            }


            long orderID = Long.parseLong(result.getCporderid());

            UOrder order = orderManager.getOrder(orderID);

            if (order == null || order.getChannel() == null) {
                Log.d("<酷派>订单或渠道不存在.");
                this.renderState(false);
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.d("<酷派>订单完成的状态：" + order.getState());
                this.renderState(true);
                return;
            }

            if ("0".equals(result.getResult())) {

                float money = Float.parseFloat(result.getMoney());
                int moneyInt = (int) (money * 100);  //以分为单位

                order.setMoney(moneyInt);
                order.setChannelOrderID(result.getTransid());
                order.setState(PayState.STATE_SUC);

                orderManager.saveOrder(order);

                SendAgent.sendCallbackToServer(this.orderManager, order);

            } else {
                order.setChannelOrderID(result.getTransid());
                order.setState(PayState.STATE_FAILED);
                orderManager.saveOrder(order);
            }


        } catch (Exception e) {
            try {
                renderState(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private boolean isSignOK(UChannel channel) {

        return SignHelper.verify(transdata, this.sign, channel.getCpPayKey());
    }

    private void renderState(boolean suc) throws IOException {

        String r = "SUCCESS";
        if (!suc) {
            r = "FAILURE";
        }

        this.response.getWriter().write(r);
    }

    public String getTransdata() {
        return transdata;
    }

    public void setTransdata(String transdata) {
        this.transdata = transdata;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSigntype() {
        return signtype;
    }

    public void setSigntype(String signtype) {
        this.signtype = signtype;
    }

    public int getU8ChannelID() {
        return u8ChannelID;
    }

    public void setU8ChannelID(int u8ChannelID) {
        this.u8ChannelID = u8ChannelID;
    }
}
