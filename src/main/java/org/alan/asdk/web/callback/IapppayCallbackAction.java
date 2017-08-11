package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.iapppay.sign.SignHelper;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 爱贝支付回调
 *
 * @author Lance Chow
 * @create 2016-05-13 10:01
 */
@Controller
@Scope("prototype")
@Namespace("/pay/iapppay")
public class IapppayCallbackAction extends UActionSupport {

    private String transdata;
    private String signtype;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback(){
        Log.i("<爱贝支付>回调开始: transdata=" +transdata+ ", signtype=" +signtype+ ", sign=" +sign);

        if (transdata==null){
            Log.i("<爱贝支付>回调内容为空 transdata=null");
        }
        try {
            JSONObject data = JSONObject.fromObject(transdata);
            long orderID = Long.parseLong(data.getString("cporderid"));
            UOrder order = orderManager.getOrder(orderID);
            if (order==null || order.getChannel() == null){
                Log.i("<爱贝支付>订单["+orderID+"] , 找不到订单或者渠道");
                renderMsg(false);
                return;
            }
            UChannel channel = order.getChannel();
            //检验签名
            if(!SignHelper.verify(transdata,sign,channel.getCpPayPriKey())){
                Log.i("<爱贝支付>订单["+orderID+"] , 签名验证不正确!");
                renderMsg(false);
                return;
            }
            //验证交易结果
            if (data.getInt("result")!=0){
                Log.i("<爱贝支付>订单["+orderID+"] , 交易结果不正确 result = "+data.get("result"));
                renderMsg(false);
                return;
            }
            //验证金额
            double money = data.getDouble("money");
            int m = new Double(money*100).intValue();
            if (m!=order.getMoney()){
                Log.i("<爱贝支付>订单["+orderID+"] , 金额["+money+"],与订单金额["+order.getMoney()+"]不一致!");
                renderMsg(false);
                return;
            }
            //通知游戏服务器
            SendAgent.sendCallbackToServer(orderManager,order);
            renderMsg(true);
        }catch (Exception e){
            Log.e("<爱贝支付>订单出错!",e);
            renderMsg(false);
        }
    }

    private void renderMsg(boolean flag){
        renderText(flag?"SUCCESS":"FAIL");
    }

    public String getTransdata() {
        return transdata;
    }

    public void setTransdata(String transdata) {
        this.transdata = transdata;
    }

    public String getSigntype() {
        return signtype;
    }

    public void setSigntype(String signtype) {
        this.signtype = signtype;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
