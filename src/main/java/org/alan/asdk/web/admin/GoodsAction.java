package org.alan.asdk.web.admin;

import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.UGoods;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.UGoodsManager;
import org.alan.asdk.service.admin.UAdminManager;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * Created by Administrator on 2015-11-09.
 */
@Controller
@Scope("prototype")
@Namespace("/admin/goods")
public class GoodsAction extends UActionSupport implements ModelDriven<UGoods> {

    private int page;           //当前请求的页码
    private int rows;           //当前每页显示的行数

    private int currGoodsID;

    private UGoods goods;

    @Autowired
    private UGoodsManager uGoodsManager;
    @Autowired
    private UAdminManager adminManager;

    public GoodsAction() {
    }

    @Override
    public UGoods getModel() {
        if (this.goods == null) {
            this.goods = new UGoods();
        }
        return this.goods;
    }
    @Action("getAllGoods")
    public void getAllGoods(){
        try {
            if (goods.getGoodsConfigID()==0){
                return;
            }
            List<UGoods> currPage = this.uGoodsManager.queryGoodsPage(goods.getGoodsConfigID());
            JSONObject json = new JSONObject();
            json.put("total", currPage.size());
            JSONArray orders = new JSONArray();
            for (UGoods m : currPage) {
                orders.add(m.toJSON());
            }
            json.put("rows", orders);
            renderJson(json.toString());
        } catch (Exception e) {
            renderState(false,e.toString());
            e.printStackTrace();
        }
    }
    //添加或者编辑
    @Action("saveGoods")
    public void saveGoodsMasters() {
        TAdmin tAdmin = adminManager.getAdminByUsername(this.session.get("adminName").toString());
        uGoodsManager.saveGoods(this.goods);
        Log.i("#商品管理(操作员:"+ tAdmin.getFullName()+")#保存商品:"+this.goods.toJSON().toString());
        renderState(true);
    }

    @Action("removeGoods")
    public void removeGoods(){
        TAdmin tAdmin = adminManager.getAdminByUsername(this.session.get("adminName").toString());

        uGoodsManager.removeGoods(currGoodsID);
        Log.i("#商品管理(操作员:"+ tAdmin.getFullName()+")#删除商品ID:"+currGoodsID);
        renderState(true);
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
    /**getter and setter**/
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

    public UGoods getGoods() {
        return goods;
    }

    public void setGoods(UGoods goods) {
        this.goods = goods;
    }

    public UGoodsManager getuGoodsManager() {
        return uGoodsManager;
    }

    public void setuGoodsManager(UGoodsManager uGoodsManager) {
        this.uGoodsManager = uGoodsManager;
    }

    public int getCurrGoodsID() {
        return currGoodsID;
    }

    public void setCurrGoodsID(int currGoodsID) {
        this.currGoodsID = currGoodsID;
    }
}
