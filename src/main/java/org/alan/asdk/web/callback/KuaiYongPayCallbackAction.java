package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.kuaiyong.kuaiyong.Base64;
import org.alan.asdk.sdk.kuaiyong.kuaiyong.RSAEncrypt;
import org.alan.asdk.sdk.kuaiyong.kuaiyong.RSASignature;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.HttpUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Map;

/**
 * 快用支付回调
 * Created by ant on 2015/9/22.
 */

@Controller
@Scope("prototype")
@Namespace("/pay/kuaiyong")

public class KuaiYongPayCallbackAction extends UActionSupport {

    private String notify_data;
    private String orderid;
    private String dealseq;
    private String uid;
    private String subject;
    private String v;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<快用>充值回调开始： notify_data=" +notify_data+ ", orderid=" +orderid+ ", dealseq=" +dealseq+ ", uid=" +uid+ ", subject=" +subject+ ", v=" +v+ "sign=" +sign);

            long localOrderID = Long.parseLong(dealseq);

            UOrder order = orderManager.getOrder(localOrderID);

            if (order == null || order.getChannel() == null) {
                Log.i("<快用> 订单[" + dealseq+"]不存在");
                this.renderState(true);
                return;
            }

            if (order.getState() == PayState.STATE_SUC) {
                Log.i("<快用> 订单[" + dealseq+"] 重复");
                this.renderState(true);
                return;
            }

            if (!isValid(order.getChannel())) {
                Log.i("<快用> 订单[" + dealseq+"] 签名失败");
                this.renderState(true);
                return;
            }

            RSAEncrypt encrypt = new RSAEncrypt();
            encrypt.loadPublicKey(order.getChannel().getCpPayKey());
            byte[] dcDataStr = Base64.decode(notify_data);
            byte[] plainData = encrypt.decrypt(encrypt.getPublicKey(), dcDataStr);
            //获取到加密通告信息
            String notifyDataStr = new String(plainData, "UTF-8");

            Map<String, String> notifyData = HttpUtils.parseUrlParams(notifyDataStr);

            String feeStr = notifyData.containsKey("fee") ? notifyData.get("fee") : null;
            String payresultStr = notifyData.containsKey("payresult") ? notifyData.get("payresult") : null;

            int fee = (int) (Float.valueOf(feeStr) * 100);
            int payresult = Integer.valueOf(payresultStr);


            if (order.getMoney() != fee) {
                Log.i("<快用> 订单[" + dealseq+"] 金额["+fee+"] 不一致:"+ order.getMoney());
                this.renderState(true);
                return;
            }

            if (payresult != 0) {
                Log.i("<快用> 订单[" + dealseq+"] 支付失败 :"+ payresult);
                return;
            }

            order.setChannelOrderID(orderid);
            SendAgent.sendCallbackToServer(this.orderManager, order);
            this.renderState(true);
            Log.i("<快用> 订单[" + dealseq+"] 充值成功! :"+ payresult);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.renderState(true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean isValid(UChannel channel) {

        StringBuilder sb = new StringBuilder();
        sb.append("dealseq=").append(dealseq).append("&")
                .append("notify_data=").append(notify_data).append("&")
                .append("orderid=").append(orderid).append("&")
                .append("subject=").append(subject).append("&")
                .append("uid=").append(uid).append("&")
                .append("v=").append(v);

        return RSASignature.doCheck(sb.toString(), sign, channel.getCpPayKey(),"utf-8");
    }

    private void renderState(boolean suc) throws IOException {

        if (suc) {
            this.response.getWriter().write("success");
        } else {
            this.response.getWriter().write("failed");
        }

    }

    public String getNotify_data() {
        return notify_data;
    }

    public void setNotify_data(String notify_data) {
        this.notify_data = notify_data;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getDealseq() {
        return dealseq;
    }

    public void setDealseq(String dealseq) {
        this.dealseq = dealseq;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
