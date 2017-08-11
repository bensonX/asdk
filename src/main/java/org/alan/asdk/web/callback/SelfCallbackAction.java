package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.service.UUserManager;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * 自运营平台充值回调
 *
 * @author Lance Chow
 * @create 2015-12-14 18:00
 */
@Namespace("/pay/TSDK")
@Scope("prototype")
@Controller
public class SelfCallbackAction extends UActionSupport {
    private long orderID;
    @Autowired
    private UUserManager manager;

    @Autowired
    private UOrderManager orderManager;
//    @Action("payCallback")
    public void payCallback() {
        Log.i("<自运营平台>充值回调开始： orderID=" +orderID);

        UOrder order = orderManager.getOrder(orderID);
        if (order == null) {
            renderState(false, "订单不存在:"+orderID);
            return;
        }
        //通知游戏服务器
        Log.i("测试回调["+orderID+"]成功");
        SendAgent.sendCallbackToServer(this.orderManager, order);
    }

    private void renderState(boolean suc, String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", suc ? 1 : 0);
        jsonObject.put("des", msg);
        try {
            this.response.getWriter().write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public UUserManager getManager() {
        return manager;
    }

    public void setManager(UUserManager manager) {
        this.manager = manager;
    }
}
