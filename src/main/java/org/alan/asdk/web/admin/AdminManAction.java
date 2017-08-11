package org.alan.asdk.web.admin;

import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.admin.UAdminManager;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 管理账号管理
 *
 * @author Lance Chow
 * @create 2016-02-23 10:05
 */
@Controller
@Scope("prototype")
@Namespace("/admin/manage")
public class AdminManAction extends UActionSupport implements ModelDriven<TAdmin> {

    private TAdmin admin;

    @Autowired
    private UAdminManager adminManager;

    @Action("getAdmins")
    public void getAdminUsers() {
        TAdmin admin = (TAdmin) session.get("admin");
        if (admin == null){
            return;
        }
        List<TAdmin> admins = adminManager.getChildern(admin.getId());
        JSONArray array = new JSONArray();
        for (TAdmin tAdmin : admins) {
            JSONObject json = tAdmin.toJSON();
            json.put("password","******");
            array.add(json);
        }
        renderJson(array.toString());
    }
    @Action("saveAdmin")
    public void saveAdmin(){
        TAdmin creater = (TAdmin) session.get("admin");
        if (creater == null){
            return;
        }
        if (admin.getpAdminID()==null){
            admin.setpAdminID(creater.getId());
        }
        //判断保存用户是否大于现在用户的权限
        if (creater.getPermission() > admin.getPermission()){
            renderState(false,"你无法为该用户授权这个权限!");
            return;
        }
        String msg = adminManager.saveAdmain(admin);
        renderState(msg==null , msg==null ? "操作成功" : msg);
    }

    @Action("removeAdmin")
    public void removeAdmin(){
        adminManager.removeAdmin(admin.getId());
        renderState(true);
    }

    @Action(value = "showAdmins", results = {@Result(name = "success", location = "/WEB-INF/admin/_admin.jsp")})
    public String showModule() {
        return "success";
    }


    @Override
    public TAdmin getModel() {
        if (this.admin == null) {
            this.admin = new TAdmin();
        }

        return this.admin;
    }

    private void renderState(boolean suc) {
        renderState(suc,suc ? "操作成功" : "操作失败(用户名重复)");
    }
    private void renderState(boolean suc , String msg) {
        JSONObject json = new JSONObject();
        json.put("state", suc ? 1 : 0);
        json.put("msg", msg);
        renderText(json.toString());
    }

}
