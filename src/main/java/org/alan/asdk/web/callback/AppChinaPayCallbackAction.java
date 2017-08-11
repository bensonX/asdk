package org.alan.asdk.web.callback;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.appchina.CpTransSyncSignValid;
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
 * 应用汇支付回调通知接口
 * Created by ant on 2015/4/28.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/appchina")
public class AppChinaPayCallbackAction extends UActionSupport {


    private String transdata;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<应用汇>充值回调开始： transdata= " +transdata+ ", sign= " +sign);

            AppChinaPayContent data = (AppChinaPayContent) JsonUtils.decodeJson(transdata, AppChinaPayContent.class);

            if (data == null) {
                Log.e("<应用汇>内容解析错误...");
                return;
            }

            long localOrderID = Long.parseLong(data.getExorderno());

            UOrder order = orderManager.getOrder(localOrderID);

            if (order == null || order.getChannel() == null) {
                Log.d("<应用汇>订单或渠道不存在");
                this.renderState(false);
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.d("<应用汇>订单完成的状态：" + order.getState());
                this.renderState(true);
                return;
            }

            if (!"0".equals(data.getResult())) {
                Log.e("<应用汇>平台支付失败 local orderID:" + localOrderID + "; order id:" + data.getTransid());
                this.renderState(false);
                return;
            }

            if (isValid(order.getChannel())) {
                order.setChannelOrderID(data.getTransid());
                order.setState(PayState.STATE_SUC);
                orderManager.saveOrder(order);
                SendAgent.sendCallbackToServer(this.orderManager, order);
                this.renderState(true);
            } else {
                order.setChannelOrderID(data.getTransid());
                order.setState(PayState.STATE_FAILED);
                orderManager.saveOrder(order);
                this.renderState(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.renderState(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean isValid(UChannel channel) {

        return CpTransSyncSignValid.validSign(this.transdata, this.sign, channel.getCpPayKey());
        //测试时使用 return RSAUtils.verify(this.transdata, this.sign, channel.getCpPayKey(), "UTF-8");

    }

    private void renderState(boolean suc) throws IOException {

        if (suc) {
            this.response.getWriter().write("SUCCESS");
        } else {
            this.response.getWriter().write("FAILURE");
        }

    }

    public static class AppChinaPayContent {
        private String exorderno;
        private String transid;
        private String appid;
        private String waresid;
        private String feetype;
        private String money;
        private String count;
        private String result;
        private String transtype;
        private String transtime;
        private String cpprivate;

        public String getExorderno() {
            return exorderno;
        }

        public void setExorderno(String exorderno) {
            this.exorderno = exorderno;
        }

        public String getTransid() {
            return transid;
        }

        public void setTransid(String transid) {
            this.transid = transid;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getWaresid() {
            return waresid;
        }

        public void setWaresid(String waresid) {
            this.waresid = waresid;
        }

        public String getFeetype() {
            return feetype;
        }

        public void setFeetype(String feetype) {
            this.feetype = feetype;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getTranstype() {
            return transtype;
        }

        public void setTranstype(String transtype) {
            this.transtype = transtype;
        }

        public String getTranstime() {
            return transtime;
        }

        public void setTranstime(String transtime) {
            this.transtime = transtime;
        }

        public String getCpprivate() {
            return cpprivate;
        }

        public void setCpprivate(String cpprivate) {
            this.cpprivate = cpprivate;
        }
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
}
