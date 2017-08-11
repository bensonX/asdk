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

/**
 * 说玩充值回调
 * Created by luo_s on 2016/6/28 0028.
 */
@Controller
@Scope("prototype")
@Namespace("/pay/shuowan")
public class ShuoWanCallbackAction extends UActionSupport {
    /**
     * 说玩参数
     */
    private String orderid;
    private String username;
    private String gameid;
    private String roleid;
    private String serverid;
    private String paytype;
    private String amount;
    private long paytime;
    private String attach;
    private String sign;
    @Autowired
    private UOrderManager orderManager;

    @Action("payCallback")
    public void payCallback(){
        try{
            Log.i("<说玩>充值回调开始： orderid=" +orderid+ ", username=" +username+ ", gameid=" +gameid+ ", roleid=" +roleid+ ", serverid=" +serverid+ ", paytype=" +paytype+ ", amount=" +amount+ ", paytime=" +paytime+ ", attach=" +attach+ ", sign=" +sign);
            //Log.i("<说玩>订单["+attach+"] 回调开始!");
            long orderID = Long.parseLong(attach);
            UOrder order = orderManager.getOrder(orderID);
            //验证订单是否存在
            if (order==null) {
                Log.i("<说玩>订单["+attach+"] 不存在!");
                renderState(false);
                return;
            }
            //验证订单是否重复
            if (order.getState() == PayState.STATE_SUC) {
                Log.i("<说玩> 订单["+attach+"重复!");
                this.renderState(false);
                return;
            }

            UChannel channel = order.getChannel();
            //验证签名
            if (!isValid(channel)) {
                renderState(true);
                return;
            }
            //验证游戏ID
            if (!channel.getCpID().equals(gameid)) {
                Log.i("<说玩>订单["+attach+"]gameid["+gameid+"] 不一致,cpAppID="+channel.getCpAppID());
                renderState(true);
                return;
            }
            //验证角色ID
            if (!roleid.equals(order.getRoleID())) {
                Log.i("<说玩>订单["+attach+"]角色ID["+roleid+"] 不一致，roleID="+order.getRoleID());
                renderState(true);
                return;
            }

            int d = (int)(Double.parseDouble(amount)*100);
            //验证金额
            if (d != order.getMoney() || d == 0) {
                Log.i("<说玩>订单["+attach+"]金额["+amount+"]不一致,money="+order.getMoney());
                renderState(true);
                return;
            }

            //通知游戏服务器
            order.setChannelOrderID(orderid);
            SendAgent.sendCallbackToServer(this.orderManager, order);
            //充值正确
            Log.i("<说玩>订单["+attach+"]充值成功!");
            renderState(true);

        }catch (Exception e) {
            Log.e("error", e);
            renderState(false);
        }
    }

    //验证MD5
    private boolean isValid(UChannel channel) {
        try {
            String appkey = channel.getCpAppKey();
            String str = "orderid="+orderid+"&username="+username+"&gameid="+gameid+"&roleid="+roleid+"&serverid="+serverid+"&paytype="+paytype+"&amount="+amount+"&paytime="+paytime+"&attach="+attach+"&appkey="+appkey;
            String md5 = EncryptUtils.md5(str);
            if (!md5.equals(sign)) {
                Log.i("<说玩>订单["+attach+"]签名["+sign+"]与MD5["+md5+"]不一致，str="+str);
            }
            return md5.equals(sign);
        } catch (Exception e) {
            Log.i("<说玩>订单["+attach+"] sign异常["+sign+"]");
            e.printStackTrace();
        }
        return false;
    }

    private void renderState(boolean suc) {
        try {
            if (suc) {
                this.response.getWriter().write("success");
            } else {
                this.response.getWriter().write("error");
            }
        }catch (Exception e) {
            Log.e("错误!", e);
        }
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getPaytime() {
        return paytime;
    }

    public void setPaytime(long paytime) {
        this.paytime = paytime;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
