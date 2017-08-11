package org.alan.asdk.web.admin;

import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.SelfChannel;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.SelfChannelMananger;
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
@Namespace("/admin/self")
public class SelfChannelAction extends UActionSupport implements ModelDriven<SelfChannel> {

    private SelfChannel channel;

    private int currConfigID;


    @Autowired
    private SelfChannelMananger manager;

    @Action(value = "showChargeChannels",
            results = {@Result(name = "success", location = "/WEB-INF/admin/self_channel/_channelConfig.jsp")})
    public String showChannelMasters() {
        return "success";
    }

    @Action("getAllChannels")
    public void getAllChargeChannel() {
        TAdmin admin = (TAdmin) this.getSession().get("admin");
        List<SelfChannel> channels = manager.getByAdmin(admin);
        JSONArray array = new JSONArray();
        for (SelfChannel channel : channels) {
            array.add(channel.toJSON());
        }
        renderJson(array.toString());
    }

    //添加或者编辑
    @Action("saveChannel")
    public void saveChannel() {

        TAdmin tAdmin = (TAdmin) this.session.get("admin");
        manager.save(this.channel , tAdmin);
        renderState(true);
    }
    //删除
    @Action("removeChannel")
    public void removeChannel() {
        TAdmin tAdmin = (TAdmin) this.session.get("admin");
        manager.remove(this.currConfigID,tAdmin);
        renderState(true);
    }

    private void renderState(boolean suc) {
        renderState(suc,suc ? "操作成功" : "操作失败");
    }

    private void renderState(boolean suc, String msg) {
        JSONObject json = new JSONObject();
        json.put("state", suc ? 1 : 0);
        json.put("msg", msg);
        renderText(json.toString());
    }
    @Override
    public SelfChannel getModel() {

        if (this.channel == null) {
            this.channel = new SelfChannel();
        }
        return this.channel;
    }

    public int getCurrConfigID() {
        return currConfigID;
    }

    public void setCurrConfigID(int currConfigID) {
        this.currConfigID = currConfigID;
    }
}
