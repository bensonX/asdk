package org.alan.asdk.web.admin;

import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.cache.impl.logic.UChannelMasterCache;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UChannelMaster;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.UChannelManager;
import org.alan.asdk.service.UUserManager;
import org.alan.asdk.service.admin.UAdminManager;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 渠道管理
 * Created by ant on 2015/8/22.
 */
@Controller
@Scope("prototype")
@Namespace("/admin/channels")
public class ChannelAction extends UActionSupport implements ModelDriven<UChannel> {

    private int page;           //当前请求的页码
    private int rows;           //当前每页显示的行数

    private UChannel channel;

    private int currChannelID;

    @Autowired
    private UChannelManager channelManager;

    @Autowired
    private UUserManager userManager;

    @Autowired
    private UAdminManager adminManager;

    @Action(value = "channelManage", results = {@Result(name = "success", location = "/WEB-INF/admin/channels.jsp")})
    public String channelManage() {

        return "success";
    }

    @Action("getAllChannels")
    public void getAllChannels() {
        try {
            TAdmin tAdmin = (TAdmin) this.session.get("admin");
            if (tAdmin == null){
                return;
            }

            int count = this.channelManager.getChannelCount(tAdmin);

            List<UChannel> lst;

            if (rows == 0 && page == 0) {
                lst = this.channelManager.queryPage(1, 10000, tAdmin);
            } else {
                lst = this.channelManager.queryPage(page, rows, tAdmin);
            }

            if (lst == null) {
                return;
            }

            JSONObject json = new JSONObject();
            json.put("total", count);
            JSONArray masterArray = new JSONArray();
            for (UChannel m : lst) {
                masterArray.add(m.toJSON());
            }
            json.put("rows", masterArray);


            renderJson(json.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Action("getSimpleChannels")
    public void getSimpleChannels(){
        TAdmin tAdmin = (TAdmin) this.session.get("admin");
        List<UChannel> channels = this.channelManager.getByAdmin(tAdmin);
        JSONArray array = new JSONArray();
        for (UChannel channel : channels) {
            JSONObject o = new JSONObject();
            o.put("channelID",channel.getChannelID());
            o.put("channelName",channel.getChannelName());
            array.add(o);
        }

        renderJson(array.toString());


    }

    //添加或者编辑
    @Action("saveChannel")
    public void saveChannel() {

        try {
            TAdmin tAdmin = (TAdmin) this.session.get("admin");
            if (tAdmin ==null){
                renderState(false,"请重新登录!");
                return;
            }
            if (this.channel.getCreateAdminID() == 0){
                this.channel.setCreateAdminID(tAdmin.getId());
            }
            if (this.channel.getChannelID()!=null && this.channel.getChannelID() != 0){
                UChannel channel = channelManager.queryChannel(this.channel.getChannelID());
                if (this.channel.getCreateAdminID() != channel.getCreateAdminID() && !tAdmin.getId().equals(channel.getCreateAdminID()) && !adminManager.isPUser(tAdmin,channel.getCreateAdmin())){
                    renderState(false,"你不能更改这个渠道的所有者!");
                    return;
                }
            }
            channelManager.saveChannel(this.channel);
            renderState(true);
            Log.i("#渠道管理(操作员:"+ tAdmin.getFullName()+")#保存渠道:"+this.channel.toJSON().toString());
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }

        renderState(false);
    }

    @Action("removeChannel")
    public void removeChannel() {
        try {

            Log.d("Curr channelID is " + this.currChannelID);
            UChannel c = this.channelManager.queryChannel(this.currChannelID);
            if (c == null) {
                renderState(false);
                return;
            }
            TAdmin tAdmin = adminManager.getAdminByUsername(this.session.get("adminName").toString());
            if (tAdmin.getPermission() != 0 && !adminManager.isPUser(tAdmin,c.getCreateAdmin())) {
                renderState(false, "你不是该渠道的所有者,不能删除该渠道 , 请联系该渠道的所有者!");
                return;
            }
            List<UUser> lst = this.userManager.getUsersByChannel(this.currChannelID);
            if (lst.size() > 0) {
                renderState(false, "请先删除该渠道下面的所有用户数据");
                return;
            }
            Log.i("#渠道管理(操作员:"+ tAdmin.getFullName()+")#删除渠道:"+c.toJSON().toString());
            this.channelManager.deleteChannel(c);

            renderState(true);
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }

        renderState(false);
    }

    @Action("getMasterValidField")
    public void getMasterValidField() {
        UChannelMaster channelManager = UChannelMasterCache.getInstance().get(currChannelID);

        String validField = channelManager.getValidField();
        if (validField==null){
            return;
        }
        renderText(channelManager.getValidField());
    }

    private void renderState(boolean suc) {
        JSONObject json = new JSONObject();
        json.put("state", suc ? 1 : 0);
        json.put("msg", suc ? "操作成功" : "操作失败");
        renderText(json.toString());
    }

    private void renderState(boolean suc, String msg) {
        JSONObject json = new JSONObject();
        json.put("state", suc ? 1 : 0);
        json.put("msg", msg);
        renderText(json.toString());
    }


    @Override
    public UChannel getModel() {

        if (this.channel == null) {
            this.channel = new UChannel();
        }

        return this.channel;
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

    public UChannel getChannel() {
        return channel;
    }

    public void setChannel(UChannel channel) {
        this.channel = channel;
    }

    public int getCurrChannelID() {
        return currChannelID;
    }

    public void setCurrChannelID(int currChannelID) {
        this.currChannelID = currChannelID;
    }
}
