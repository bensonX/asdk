package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.mz.Base64;
import org.alan.asdk.sdk.mz.Rsa;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.web.SendAgent;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 拇指游戏 摩奇卡卡充值回调
 * @author 周麟东 on 17:18
 */
@Controller
@Scope("prototype")
@Namespace("/pay/mz")
public class MZPayCallbackAction extends UActionSupport {


    private String content;
    private String sign;

    @Autowired
    private UOrderManager orderManager;

//    public static void main(String [] scripts) {
//        String str = "eyJwYXlfbm8iOiI2MTEyMjE2MDEwMzEiLCJ1c2VybmFtZSI6Im0xODg2MjMwMTE4IiwidXNlcl9pZCI6MjY4NzM5NiwiZGV2aWNlX2lkIjoiMTEyMjE1MTU1NTY1MzE1Iiwic2VydmVyX2lkIjoiMSIsInBhY2tldF9pZCI6IjEwMDQ2NTAwMSIsImdhbWVfaWQiOiI0NjUiLCJjcF9vcmRlcl9pZCI6IjEwNzU1MTYxNjUxODQyOTA4MTciLCJwYXlfdHlwZSI6Imlvc3BheSIsImFtb3VudCI6NjAwLCJwYXlTdGF0dXMiOjB9";
//                String key ="&key=zty465";
//        String a =new String(com.u8.com.u8.server.sdk.mz.Base64.decode(str));
//        System.out.println(Rsa.getMD5(a+key));
//    }

    @Action("payCallback")
    public void payCallback() {
        Log.i("<摩奇卡卡>充值回调开始: content = "+this.content +" , sign = " +this.sign);
        try {
            //解析内容
            String content = new String(Base64.decode(this.content));
            if (StringUtils.isEmpty(content)){
                Log.i("<摩奇卡卡>充值回调错误 , content为空");
                return;
            }
            Log.i("<摩奇卡卡>解析结果:"+content);
            JSONObject json = JSONObject.fromObject(content);
            //获取订单号
            long orderID = Long.parseLong(json.getString("cp_order_id"));
            UOrder order = orderManager.getOrder(orderID);
            if (order == null || order.getState() == PayState.STATE_SUC){
                Log.i("<摩奇卡卡>充值回调错误 , orderID = "+orderID +", order对象为空,或者状态为已完成");
                return;
            }
            UChannel channel = order.getChannel();
            String str = content+"&key="+channel.getCpAppKey();
            String md5 = Rsa.getMD5(str);
            if (!md5.equalsIgnoreCase(sign)){
                str = content+"&key="+channel.getCpAppSecret();
                md5 = Rsa.getMD5(str);
                if (!md5.equalsIgnoreCase(sign)){
                    Log.i("<摩奇卡卡>订单["+orderID+"]签名出错 , str = " +str +" , md5 = "+md5 +", sign = "+this.sign);
                    return;
                }
            }
            //判断是否为重复订单
            if (json.getInt("payStatus") != 0){
                Log.i("<摩奇卡卡>订单["+orderID+"]错误,payStatus = "+ json.getInt("payStatus"));
                return;
            }
            //判断金额
            if (json.getInt("amount")!=(order.getMoney()*100)){
                Log.i("<摩奇卡卡>订单["+orderID+"] 订单会被修改 订单金额不等于实际金额 amount = "+json.getInt("amount")+" , order.getMoney = "+ order.getMoney());
                order.setProductName("com.muzhiyouwan.bzddt_any");
                order.setMoney(json.getInt("amount"));
            }
            //发放奖励
            order.setChannelOrderID(json.getString("pay_no"));
            SendAgent.sendCallbackToServer(this.orderManager, order);
            //充值正确
            Log.i("<摩奇卡卡>订单["+orderID+"]充值成功!");
        }catch (Exception e){
            Log.i("<摩奇卡卡>充值回调程序异常!",e);
            e.printStackTrace();
        }finally {
            renderText("success");
        }

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
