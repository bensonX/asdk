package org.alan.asdk.web.callback;

import com.lenovo.pay.sign.CpTransSyncSignValid;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
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
 * 联想支付回调
 * Created by ant on 2015/4/27.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/lenovo")
public class LenovoPayCallbackAction extends UActionSupport {


    private String transdata;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback() {
        try {
            Log.i("<联想>充值回调开始： transdata=" +transdata+ ", sign=" +sign);

            LenovoPayContent data = (LenovoPayContent) JsonUtils.decodeJson(transdata, LenovoPayContent.class);

            if (data == null) {
                Log.e("<联想>内容解析错误...");
                return;
            }

            long localOrderID = Long.parseLong(data.getExorderno());

            UOrder order = orderManager.getOrder(localOrderID);

            if (order == null || order.getChannel() == null) {
                Log.d("<联想>订单或渠道不存在.");
                this.renderState(false, "notifyId 错误");
                return;
            }

            if (order.getState() == PayState.STATE_COMPLETE) {
                Log.d("<联想>订单完成状态：" + order.getState());
                this.renderState(true, "订单已经被处理,或者CP订单号重复");
                return;
            }

            int orderMoney = Integer.valueOf(data.getMoney());      //转换为分


            if (order.getMoney() != orderMoney) {
                Log.e("<联想>订单金额不一致! local orderID:" + localOrderID + "; money returned:" + data.getMoney() + "; order money:" + order.getMoney());
                this.renderState(false, "订单金额不一致");
                return;
            }

            if (!"0".equals(data.getResult())) {
                Log.e("<联想>平台支付失败 local orderID:" + localOrderID + ";lenovo order id:" + data.getTransid());
                this.renderState(false, "支付中心返回的结果是支付失败");
                return;
            }

            if (isValid(order.getChannel())) {
                order.setChannelOrderID(data.getTransid());
                order.setState(PayState.STATE_SUC);
                orderManager.saveOrder(order);
                SendAgent.sendCallbackToServer(this.orderManager, order);
                this.renderState(true, "");
            } else {
                order.setChannelOrderID(data.getTransid());
                order.setState(PayState.STATE_FAILED);
                orderManager.saveOrder(order);
                this.renderState(false, "Sign错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.renderState(false, "未知错误");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean isValid(UChannel channel) {

        return CpTransSyncSignValid.validSign(this.transdata, this.sign, channel.getCpPayKey());
        //测试时使用 return RSAUtils.verify(this.transdata, this.sign, channel.getCpPayKey(), "UTF-8");

    }

    private void renderState(boolean suc, String msg) throws IOException {

        if (suc) {
            this.response.getWriter().write("SUCCESS");
        } else {
            this.response.getWriter().write("FAILURE");
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

    public static class LenovoPayContent {

        private String exorderno;           //游戏订单号
        private String transid;           //联想支付平台的订单号
        private String appid;            //游戏 id  String  Max(20)    Open AppID
        private String waresid;            //商品编码  integer  Max(8)    商品编号
        private String feetype;            //计费方式  integer  Max(3)    计费方式：0、按次；1、开放价格；
        private String money;            //交易金额  integer  Max(10)    本次交易的金额，单位：分
        private String count;            //购买数量  integer  Max(10)    本次购买的商品数量
        private String result;            //交易结果  integer  Max(2)    交易结果：0– 交易成功；1–交易失败
        private String transtype;            //交易类型  integer  Max(2)    交易类型： 0 –交易； 1 –冲正
        private String transtime;            //交易时间  String  Max(20)    交易时间格式：yyyy-mm-dd hh24:mi:ss
        private String cpprivate;            //商户私有信息  String  Max(128)  商户私有信息
        private String paytype;            //支付方式  Integer  Max(2)  支付方式（该字段值后

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

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }
    }
}
