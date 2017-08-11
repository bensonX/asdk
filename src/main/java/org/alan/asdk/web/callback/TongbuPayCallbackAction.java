package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.EncryptUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * 同步推充值回调
 *
 * @author Lance Chow
 * @create 2015-12-10 15:10
 */
@Controller
@Scope("prototype")
@Namespace("/pay/tongbu")
public class TongbuPayCallbackAction extends UActionSupport {

    private String source;
    private String trade_no;
    private int amount;
    private String partner;
    private String paydes;
    private String debug;
    private String tborder;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback(){
        Log.i("<同步推>充值回调开始： source=" +source+ ", trade_no=" +trade_no+ ", amount=" +amount+ ", partner=" +partner+ ", paydes=" +paydes+ ", debug=" +debug+ ", tborder=" +tborder+ ", sign=" +sign);

        Log.i("<同步推>订单["+trade_no+"] 回调完成!");
        UOrder order = orderManager.getOrder(Long.parseLong(trade_no));
        if (order==null){
            renderState();
            Log.i("<同步推>订单["+trade_no+"] 订单不存在!");
            return;
        }
        UChannel channel = order.getChannel();
        //验证签名
        if (!signValid(channel)){
            renderState();
            return;
        }
        //验证订单是否重复
        if (order.getState()!= PayState.STATE_PAYING){
            Log.i("<同步推>订单["+trade_no+"]重复");
            renderState();
            return;
        }
        //验证金额
        if (order.getMoney()!=amount){
            renderState();
            Log.i("<同步推>订单["+trade_no+"]金额["+amount+"]不吻合,订单金额["+order.getMoney()+"]");
            return;
        }
        if(!order.getChannel().getCpAppID().equals(partner)){
            renderState();
            Log.i("<同步推>订单["+trade_no+"]CP AppID["+partner+"]不吻合"+order.getChannel().getCpAppID());
            return;
        }
        Log.i("<同步推>订单["+trade_no+"] 充值成功!");
        //通知游戏服务器
        order.setChannelOrderID(tborder);
        SendAgent.sendCallbackToServer(this.orderManager, order);
        renderState();
    }

    /**
     * 响应SDK
     */
    public void renderState(){
        JSONObject js = new JSONObject();
        js.put("status","success");
        try {
            response.getWriter().write(js.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证签名是否正确
     * @param channel
     * @return
     */
    public boolean signValid(UChannel channel){
        StringBuffer buffer = new StringBuffer();
        buffer.append("source=").append(source).
                append("&trade_no=").append(trade_no).
                append("&amount=").append(amount).
                append("&partner=").append(partner).
                append("&paydes=").append(paydes).
                append("&debug=").append(debug).
                append("&tborder=").append(tborder)
                .append("&key=").append(channel.getCpAppKey());
        boolean isTrue = EncryptUtils.md5(buffer.toString()).equals(sign);
            if(!isTrue){
                Log.i("签名["+sign+"]验证不正确,正确的是:["+ EncryptUtils.md5(buffer.toString())+"]");
            }
        return isTrue;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getPaydes() {
        return paydes;
    }

    public void setPaydes(String paydes) {
        this.paydes = paydes;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getTborder() {
        return tborder;
    }

    public void setTborder(String tborder) {
        this.tborder = tborder;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
