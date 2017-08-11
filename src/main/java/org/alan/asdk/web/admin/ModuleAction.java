package org.alan.asdk.web.admin;

import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.entity.admin.TModule;
import org.alan.asdk.service.admin.UModuleManager;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * 模块控制器
 *
 * @author Lance Chow
 * @create 2016-01-14 15:17
 */
@Controller
@Scope("prototype")
@Namespace("/admin/module")
public class ModuleAction extends UActionSupport implements ModelDriven<TModule> {

    private TModule module;
    @Autowired
    private UModuleManager manager;

    private int currModuleID;


    @Override
    public TModule getModel() {
        if (this.module == null) {
            this.module = new TModule();
        }
        return this.module;
    }

    @Action(value = "showModule", results = {@Result(name = "success", location = "/WEB-INF/admin/_module.jsp")})
    public String showModule() {
        return "success";
    }


    @Action("getModulesTreeType")
    public void getModulesTreeType() {
        TAdmin admin = (TAdmin) session.get("admin");
        //判断权限
        if (!isAdmin()) {
            renderState(false);
            return;
        }
        List<TModule> modules = manager.getModuleByPermission(admin.getPermission());
        List<Map<String, Object>> list = manager.moduleToTree(modules, -1);
        JSONArray array = JSONArray.fromObject(list);
        renderText(array.toString());
    }

    //获取简单的父级菜单
    @Action("getSimplePModules")
    public void getSimplePModules(){
        List<TModule> modules = manager.getPModules();
        JSONArray array = new JSONArray();
        for (TModule tModule : modules) {
            array.add(tModule.toJSON());
        }
        renderText(array.toString());
    }

    @Action("getAllModules")
    public void getAllModules() {
        TAdmin admin = (TAdmin) session.get("admin");
        //判断权限
        if (!isAdmin()) {
            renderState(false);
            return;
        }
        List<TModule> modules = manager.getModuleByPermission(admin.getPermission());
        JSONArray array = new JSONArray();
        for (TModule m : modules) {
            array.add(m.toJSON());
        }
        renderText(array.toString());
    }

    @Action("saveModule")
    public void saveModule() {
        Log.i("保存模块:" + module.toJSON().toString());
        manager.saveModule(module);
        renderState(true);
    }
    @Action("getModuleByID")
    public void getModuleByID(){
        renderText(manager.getModule(currModuleID).toJSON().toString());
    }

    @Action("removeModule")
    public void removeModule() {
        Integer id = module.getId();
        Log.i("删除模块:" + id);
        manager.removeModuleByID(id);
        renderState(true);
    }

    private boolean isAdmin() {
        TAdmin admin = (TAdmin) session.get("admin");
        //判断权限
        return admin.getPermission() <= TAdmin.MANAGER;
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

    public TModule getModule() {
        return module;
    }

    public void setModule(TModule module) {
        this.module = module;
    }

    public int getCurrModuleID() {
        return currModuleID;
    }

    public void setCurrModuleID(int currModuleID) {
        this.currModuleID = currModuleID;
    }
}
