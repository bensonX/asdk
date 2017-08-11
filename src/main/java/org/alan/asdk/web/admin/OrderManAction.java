package org.alan.asdk.web.admin;

import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.Page;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.EncryptUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * 订单管理
 * Created by ant on 2015/8/29.
 */
@Controller
@Scope("prototype")
@Namespace("/admin/orders")
public class OrderManAction extends UActionSupport implements ModelDriven<UOrder> {

    private int page;           //当前请求的页码
    private int rows;           //当前每页显示的行数

    private UOrder order;

    private String currRoleID;
    private String currOrderID;
    private String currChannelOrderID;
    private String pwd;

    @Autowired
    private UOrderManager orderManager;

    @Action(value = "showOrders",
            results = {@Result(name = "success", location = "/WEB-INF/admin/_orders.jsp")})
    public String showOrders() {

        return "success";
    }

    @Action(value = "showOrderAnalytics",
            results = {@Result(name = "success", location = "/WEB-INF/admin/order_analytics.jsp")})
    public String showOrderAnalytics() {

        return "success";
    }


    @Action("getAllOrders")
    public void getAllOrders() {
        try {
            Page<UOrder> currPage = this.orderManager.queryPage(page, rows);

            JSONObject json = new JSONObject();
            json.put("total", currPage.getTotalCount());
            JSONArray orders = new JSONArray();
            for (UOrder m : currPage.getResultList()) {
                orders.add(m.toJSON());
            }
            json.put("state", 1);
            json.put("rows", orders);

            renderJson(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Action("getOrders")
    public void getOrders() {
        try {
            long orderID = StringUtils.isEmpty(this.currOrderID) ? 0 : Long.parseLong(currOrderID);
            Page<UOrder> currPage = this.orderManager.getOrdersByParams(currRoleID,orderID,currChannelOrderID);
            JSONArray orders = new JSONArray();
            for (UOrder m : currPage.getResultList()) {
                orders.add(m.toJSON());
            }
            JSONObject json = new JSONObject();
            json.put("state", 1);
            json.put("rows", orders);
            json.put("total", currPage.getTotalCount());
            renderJson(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 补单
     */
    @Action("budan")
    public void budan(){
        TAdmin admin = (TAdmin) this.session.get("admin");
        //验证密码
        if(admin==null){
            renderState(false,"用户信息丢失请重新登录");
            return;
        }
        if(!admin.getPassword().equals(EncryptUtils.md5(pwd))){
            renderState(false,"密码错误");
            return;
        }
        UOrder order =  orderManager.getOrder(Long.parseLong(currOrderID));
        if (order==null){
            renderState(false,"订单不存在");
            return;
        }
        //通知游戏服务器
        if(order.getState() == PayState.STATE_SUC || order.getState() == PayState.STATE_COMPLETE){
            renderState(false,"订单已完成不能补单");
            return;
        }
        order.setChannelOrderID("该订单由"+admin.getFullName()+",在 "+DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")+" 完成补单");
        SendAgent.sendCallbackToServer(this.orderManager, order);
        renderState(true,"补单成功!");
    }


    @Action("removeOrder")
    public void removeOrder() {
        try {
            Log.d("Curr orderID is " + this.currOrderID);

            UOrder order = orderManager.getOrder(Long.parseLong(this.currOrderID));

            if (order == null) {
                renderState(false);
                return;
            }

            orderManager.deleteOrder(order);

            renderState(true);

            return;

        } catch (Exception e) {
            e.printStackTrace();
        }

        renderState(false);
    }


    private void renderState(boolean suc) {
        renderState(suc,suc ? "操作成功" : "操作失败");
    }
    private void renderState(boolean suc ,String msg) {
        JSONObject json = new JSONObject();
        json.put("state", suc ? 1 : 0);
        json.put("msg", msg);
        renderText(json.toString());
    }


    @Override
    public UOrder getModel() {

        if (this.order == null) {
            this.order = new UOrder();
        }

        return this.order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public UOrder getOrder() {
        return order;
    }

    public void setOrder(UOrder order) {
        this.order = order;
    }

    public String getCurrChannelOrderID() {
        return currChannelOrderID;
    }

    public void setCurrChannelOrderID(String currChannelOrderID) {
        this.currChannelOrderID = currChannelOrderID;
    }

    public String getCurrRoleID() {
        return currRoleID;
    }

    public void setCurrRoleID(String currRoleID) {
        this.currRoleID = currRoleID;
    }

    public String getCurrOrderID() {
        return currOrderID;
    }

    public void setCurrOrderID(String currOrderID) {
        this.currOrderID = currOrderID;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
