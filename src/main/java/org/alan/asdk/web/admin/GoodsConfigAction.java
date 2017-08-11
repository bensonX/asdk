package org.alan.asdk.web.admin;

import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.UGoodsConfig;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.UChannelManager;
import org.alan.asdk.service.UGoodsConfigManager;
import org.alan.asdk.service.admin.UAdminManager;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 商品配置
 *
 * @author Lance Chow
 * @create 2015-12-28 11:26
 */
@Controller
@Scope("prototype")
@Namespace("/admin/goodsConfig")
public class GoodsConfigAction extends UActionSupport implements ModelDriven<UGoodsConfig> {


    private int page;           //当前请求的页码
    private int rows;           //当前每页显示的行数
    private int currConfigID;

    private UGoodsConfig config;

    @Autowired
    private UGoodsConfigManager manager;

    @Autowired
    private UChannelManager channelManager;

    @Autowired
    private UAdminManager adminManager;

    public GoodsConfigAction() {
    }

    public UGoodsConfig getModel() {
        if (this.config == null){
            this.config = new UGoodsConfig();
        }
        return this.config;
    }

    /** config 方法开始 **/
    @Action(value = "showGoods" , results = {@Result(name = "success" , location = "/WEB-INF/admin/_goods.jsp")})
    public String showGoods(){
        return "success";
    }

    @Action("getAllConfig")
    public void getAllConfig(){
        Object o = this.session.get("adminName");
        if (o==null){
            renderText("<!DOCTYPE html>");
            return;
        }
        TAdmin tAdmin = adminManager.getAdminByUsername(o.toString());
        List<UGoodsConfig> goodsConfigs = manager.queryConfigPage(page, rows, tAdmin);
        if (goodsConfigs==null){
            return;
        }
        int count = manager.getConfigCount(tAdmin);
        JSONObject json = new JSONObject();
        json.put("total", count);
        JSONArray array = new JSONArray();
        for (UGoodsConfig config : goodsConfigs){
            array.add(config.toJSON());
        }
        json.put("rows",array);
        renderJson(json.toString());
    }

    @Action("getAllConfigSimple")
    public void getAllConfigSimple(){
        try {
            List<UGoodsConfig> configs = manager.queryAllConfig();
            JSONArray array = new JSONArray();
            for (UGoodsConfig config : configs){
                JSONObject item = new JSONObject();
                item.put("goodsConfigID",config.getId());
                item.put("goodsConfigName",config.getName());
                array.add(item);
            }
            renderJson(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("saveConfig")
    public void saveGoodsConfig(){
        TAdmin tAdmin = adminManager.getAdminByUsername(this.session.get("adminName").toString());
        config.setCreateAdminID(tAdmin.getId());
        manager.saveConfig(config);
        Log.i("#商品配置管理(操作员:"+ tAdmin.getFullName()+")#保存商品配置:"+this.config.toJSON().toString());
        renderState(true);
    }

    @Action("removeConfig")
    public void removeConfig(){
        TAdmin tAdmin = adminManager.getAdminByUsername(this.session.get("adminName").toString());
        if (!channelManager.getChannelByGoodsConfigID(currConfigID).isEmpty()){
            renderState(false,"请先删除该商品的所对应的渠道");
            return;
        }
        manager.removeConfig(currConfigID);
        Log.i("#商品配置管理(操作员:"+ tAdmin.getFullName()+")#删除商品配置ID:"+currConfigID);
        renderState(true);
    }


    private void renderState(boolean suc) {
        renderState(suc,suc ? "操作成功" : "操作失败");
    }
    private void renderState(boolean suc , String msg) {
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

    public int getCurrConfigID() {
        return currConfigID;
    }

    public void setCurrConfigID(int currConfigID) {
        this.currConfigID = currConfigID;
    }

    public UGoodsConfig getConfig() {
        return config;
    }

    public void setConfig(UGoodsConfig config) {
        this.config = config;
    }
}
