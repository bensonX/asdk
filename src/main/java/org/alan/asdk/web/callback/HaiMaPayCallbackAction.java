package org.alan.asdk.web.callback;

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

import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * 海马充值回调
 * Created by Administrator on 2015-12-08.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/haima")
public class HaiMaPayCallbackAction extends UActionSupport {

    private String notify_time;
    private String appid;
    private String out_trade_no;
    private String total_fee;
    private String subject;
    private String body;
    private String trade_status;
    private String sign;
    private String user_param;

    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback(){
        try {
            Log.i("<海马>充值回调开始： notify_time=" +notify_time+ ", appid=" +appid+ ", out_trade_no=" +out_trade_no+ ", total_fee=" +total_fee+ ", subject=" +subject+ ", body=" +body+ ", trade_status=" +trade_status+ ", sign=" +sign+ ", user_param=" +user_param);

            Log.i("<海马>订单["+out_trade_no+"] 回调成功!");

            //验证是否成功
            int status = Integer.parseInt(trade_status);
            if (status!=1){
                Log.i("<海马>订单["+out_trade_no+"] 交易状态(trade_status)["+trade_status+"]不为1");
                renderState(true);
                return;
            }

            //获取Order
            long orderID = Long.parseLong(out_trade_no);
            UOrder order = orderManager.getOrder(orderID);

            if(order==null){
                Log.i("<海马>订单["+out_trade_no+"]不存在");
                renderState(true);
                return;
            }

            if (order.getState()!= PayState.STATE_PAYING){
                Log.i("<海马>订单["+out_trade_no+"]重复");
                renderState(true);
                return;
            }

            //验证签名
            if (!isVaild(order.getChannel())){
                renderState(false);
                return;
            }

            int d = (int) (Double.parseDouble(total_fee)*100);
            if (d==0 || d!=order.getMoney()){
                Log.i("<海马> 订单["+out_trade_no+"] amount["+d+"] 与金额["+order.getMoney()+"] 不一致 ");
                renderState(true);
                return;
            }
            //通知游戏服务器
            SendAgent.sendCallbackToServer(this.orderManager, order);
            Log.i("<海马> 订单["+out_trade_no+"] 充值成功!");
            renderState(true);
        }catch (Exception e){
            Log.i("<海马> 订单["+out_trade_no+"] 失败 , 逻辑错误:" +e.getMessage());
            e.printStackTrace();
            renderState(false);
        }
    }

    public boolean isVaild(UChannel channel){
        StringBuffer sb = new StringBuffer();

        try {
            sb.append("notify_time=").append(URLEncoder.encode(notify_time,"UTF-8"))
                    .append("&appid=").append(URLEncoder.encode(appid,"UTF-8"))
                    .append("&out_trade_no=").append(URLEncoder.encode(out_trade_no,"UTF-8"))
                    .append("&total_fee=").append(URLEncoder.encode(total_fee,"UTF-8"))
                    .append("&subject=").append(URLEncoder.encode(subject,"UTF-8"))
                    .append("&body=").append(URLEncoder.encode(body,"UTF-8"))
                    .append("&trade_status=").append(trade_status)
                    .append(channel.getCpAppKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String right = EncryptUtils.md5(sb.toString());
        if (right.equals(sign)){
            return true;
        }
        Log.i("sb="+sb.toString());
        Log.i("<海马>订单["+out_trade_no+"] 签名["+sign+"] 不匹配 "+right);
        return right.equals(sign);
    }

    public void renderState(boolean flag){
        try {
            PrintWriter writer = response.getWriter();
            if (flag){
                writer.write("success");
            }else {
                writer.write("fail");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUser_param() {
        return user_param;
    }

    public void setUser_param(String user_param) {
        this.user_param = user_param;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }
}
