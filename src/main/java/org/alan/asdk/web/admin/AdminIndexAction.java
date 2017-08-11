package org.alan.asdk.web.admin;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.StateCode;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.entity.admin.TModule;
import org.alan.asdk.service.admin.UAdminManager;
import org.alan.asdk.service.admin.UModuleManager;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 后台管理主页
 * Created by ant on 2015/8/22.
 */
@Controller
@Scope("prototype")
@Namespace("/admin")
public class AdminIndexAction extends UActionSupport {

    private String username;
    private String password;

    @Autowired
    private UAdminManager adminManager;

    @Autowired
    private UModuleManager moduleManager;

    @Action(value = "login", results = {@Result(name = "success", location = "/WEB-INF/admin/_login.jsp")})
    public String showLogin() {

        return "success";
    }

    @Action("doLogin")
    public void login() {
        try {

            int loginTime = 0;
            Object o =  this.getSession().get("loginTime");
            if (o!=null){
                loginTime = (Integer)o;
            }
            //错误次数太多就拒绝登录
            if (loginTime>20){
                renderState(StateCode.CODE_LOGIN_ERROR,"拒绝登录!");
                return;
            }
            loginTime++;
            this.getSession().put("loginTime",loginTime);

            Log.d("The username is " + username + ";password:" + password);

            TAdmin admin = adminManager.getAdminByUsername(this.username);

            if (admin == null) {
                renderState(StateCode.CODE_AUTH_FAILED, "用户名错误");
                return;
            }

            if (!admin.getPassword().equals(this.password)) {
                renderState(StateCode.CODE_AUTH_FAILED, "用户密码错误");
                return;
            }

            this.session.put("adminName", admin.getUsername());
            this.session.put("admin",admin);
            //获取模块权限
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
            this.session.put("modules",getModulesHtml(moduleManager.getModuleByPermission(admin.getPermission()),-1,basePath));
            renderState(StateCode.CODE_AUTH_SUCCESS, "登录成功");
            this.getSession().put("loginTime",0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getModulesHtml(List<TModule> modules , int pid , String basePath){
        String html = "";
        for (TModule module : modules) {
            if (module.getPid() == pid){
                if (module.isLeaf()){
                    html+="<li><a href='"+basePath+module.getUrl()+"'><i class='fa "+module.getIconCls()+"'></i> "+module.getName()+"</a></li>";
                }else {
                    html+="<li>" +
                            "<a href='#'>" +
                                "<i class='fa "+module.getIconCls()+"'></i> "+module.getName()+"" +
                                "<span class='fa arrow'></span>" +
                            "</a>" +
                            "<ul class='nav nav-second-level'>" +
                            getModulesHtml(modules,module.getId(),basePath)+
                            "</ul>"+
                        "<li>";
                }
            }
        }

        return html;
    }

    @Action(value = "index", results = {@Result(name = "success", location = "/WEB-INF/admin/_index.jsp")})
    public String index() {
        return "success";
    }
    @Action(value = "logout", results = {@Result(name = "success", location = "/WEB-INF/admin/_login.jsp")})
    public String logout() {
        this.session.clear();
        this.getSession().clear();
        return "success";
    }

    private void renderState(int suc, String msg) {
        JSONObject json = new JSONObject();
        json.put("state", suc);
        json.put("msg", msg);
        renderText(json.toString());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
