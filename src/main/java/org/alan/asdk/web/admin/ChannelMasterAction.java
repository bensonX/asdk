package org.alan.asdk.web.admin;

import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UChannelMaster;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.UChannelMasterManager;
import org.alan.asdk.service.admin.UAdminManager;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 渠道商管理
 * Created by ant on 2015/8/22.
 */
@Controller
@Scope("prototype")
@Namespace("/admin/channelMaster")
public class ChannelMasterAction extends UActionSupport implements ModelDriven<UChannelMaster> {

    private int page;           //当前请求的页码
    private int rows;           //当前每页显示的行数

    private UChannelMaster master;

    private int currMasterID;


    @Autowired
    private UChannelMasterManager channelMasterManager;
    @Autowired
    private UAdminManager adminManager;

    @Action(value = "showChannelMasters",
            results = {@Result(name = "success", location = "/WEB-INF/admin/_channelMaster.jsp")})
    public String showChannelMasters() {
        return "success";
    }

    @Action("getAllMastersSimple")
    public void getAllChannelMastersSimple() {
        try {

            List<UChannelMaster> masters = this.channelMasterManager.queryAll();

            JSONArray masterArray = new JSONArray();
            for (UChannelMaster m : masters) {
                JSONObject item = new JSONObject();
                item.put("masterID", m.getMasterID());
                item.put("masterName", m.getMasterName());
                masterArray.add(item);
            }

            renderJson(masterArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("getAllChannelMasters")
    public void getAllChannelMasters() {

        try {
            Object o = this.session.get("adminName");
            if (o==null){
                renderText("<!DOCTYPE html>");
                return;
            }
            TAdmin tAdmin = adminManager.getAdminByUsername(o.toString());
            if (tAdmin.getPermission()!=0){
                renderState(false,"权限不足");
                return;
            }
            int count = channelMasterManager.getMasterCount();
            if(page==0){
                page = 1;
                rows = 100000;
            }
            List<UChannelMaster> masters = channelMasterManager.queryPage(page, rows);


            JSONObject json = new JSONObject();
            json.put("total", count);
            JSONArray masterArray = new JSONArray();
            for (UChannelMaster m : masters) {
                masterArray.add(m.toJSON());
            }
            json.put("rows", masterArray);

            renderJson(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //添加或者编辑
    @Action("saveMaster")
    public void saveChannelMasters() {

        try {
            TAdmin tAdmin = (TAdmin) this.session.get("admin");
            if (tAdmin ==null || tAdmin.getPermission()!=0){
                renderState(false,"权限不足");
                return;
            }
            channelMasterManager.saveChannelMaster(this.master);

            renderState(true);
            Log.i("#渠道商管理(操作员:"+ tAdmin.getFullName()+")#保存渠道商:"+this.master.toJSON().toString());
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }

        renderState(false);
    }

    @Action("removeMaster")
    public void removeChannelMaster() {
        try {

            TAdmin tAdmin = adminManager.getAdminByUsername(this.session.get("adminName").toString());
            if (tAdmin.getPermission()!=0){
                renderState(false,"权限不足");
                return;
            }
            Log.d("Curr masterID is " + this.currMasterID);
            UChannelMaster m = this.channelMasterManager.queryChannelMaster(this.currMasterID);
            if (m == null) {
                renderState(false);
                return;
            }

            List<UChannel> lst = this.channelMasterManager.queryChannels(this.currMasterID);
            if (lst.size() > 0) {
                renderState(false, "请先删除该渠道商下面的所有渠道数据");
                return;
            }
            Log.i("#渠道商管理(操作员:"+ tAdmin.getFullName()+")#删除渠道商:"+m.toJSON().toString());
            this.channelMasterManager.deleteChannelMaster(m);

            renderState(true);
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }

        renderState(false);
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

    public UChannelMaster getMaster() {
        return master;
    }

    public void setMaster(UChannelMaster master) {
        this.master = master;
    }

    @Override
    public UChannelMaster getModel() {

        if (this.master == null) {
            this.master = new UChannelMaster();
        }
        return this.master;
    }

    public int getCurrMasterID() {
        return currMasterID;
    }

    public void setCurrMasterID(int currMasterID) {
        this.currMasterID = currMasterID;
    }
}
