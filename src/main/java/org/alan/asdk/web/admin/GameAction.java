package org.alan.asdk.web.admin;

import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UGame;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.UGameManager;
import org.alan.asdk.service.admin.UAdminManager;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 游戏管理
 * Created by ant on 2015/8/26.
 */

@Controller
@Scope("prototype")
@Namespace("/admin/games")
public class GameAction extends UActionSupport implements ModelDriven<UGame> {


    private int page;           //当前请求的页码
    private int rows;           //当前每页显示的行数

    private UGame game;

    private int currAppID;

    @Autowired
    private UGameManager gameManager;
    @Autowired
    private UAdminManager adminManager;

    @Override
    public UGame getModel() {

        if (this.game == null) {
            this.game = new UGame();
        }

        return this.game;
    }


    @Action(value = "showGames", results = {@Result(name = "success", location = "/WEB-INF/admin/_games.jsp")})
    public String channelManage() {

        return "success";
    }

    @Action("getAllGames")
    public void getAllGames() {
        try {

            Object o = this.session.get("adminName");
            if (o==null){
                renderText("<!DOCTYPE html>");
                return;
            }

            TAdmin tAdmin = adminManager.getAdminByUsername(o.toString());

            int count = this.gameManager.getGameCount(tAdmin);
            List<UGame> lst = this.gameManager.queryPage(page, rows, tAdmin);
            if (lst==null){
                return;
            }
            JSONObject json = new JSONObject();
            json.put("total", count);
            JSONArray masterArray = new JSONArray();
            for (UGame m : lst) {
                masterArray.add(m.toJSON());
            }
            json.put("rows", masterArray);

            renderJson(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action("getAllGamesSimple")
    public void getAllGamesSimple() {
        try {

            List<UGame> games = this.gameManager.queryAllGames();

            JSONArray gameArray = new JSONArray();
            for (UGame game : games) {
                JSONObject item = new JSONObject();
                item.put("appID", game.getAppID());
                item.put("name", game.getName());
                gameArray.add(item);
            }

            renderJson(gameArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加或者编辑
    @Action("saveGame")
    public void saveChannelMasters() {

        try {

            TAdmin tAdmin = adminManager.getAdminByUsername(this.session.get("adminName").toString());
            this.game.setCreateAdminID(tAdmin.getId());
            if (this.game.getAppID() != null && this.game.getAppID() > 0) {
                UGame m = gameManager.queryGame(this.game.getAppID());
                if (m != null) {
                    m.setName(this.game.getName());
                    m.setPayCallback(this.game.getPayCallback());
                    m.setPayCallbackDebug(this.game.getPayCallbackDebug());
                    gameManager.saveGame(m);
                    renderState(true);
                    return;
                }
            }

            this.gameManager.generateGame(this.game.getName(), this.game.getPayCallback(), this.game.getPayCallbackDebug(), tAdmin.getId());
            Log.i("#游戏管理(操作员:"+ tAdmin.getFullName()+")#保存游戏:"+this.game.toJSON().toString());
            renderState(true);
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }

        renderState(false);
    }

    @Action("removeGame")
    public void removeGame() {
        try {

            TAdmin tAdmin = adminManager.getAdminByUsername(this.session.get("adminName").toString());
            if (tAdmin.getPermission()!=0){
                renderState(false,"权限不足");
                return;
            }
            UGame m = this.gameManager.queryGame(this.currAppID);
            if (m == null) {
                renderState(false);
                return;
            }

            List<UChannel> lst = this.gameManager.queryChannels(this.currAppID);
            if (lst.size() > 0) {
                renderState(false, "请先删除该游戏下面的所有渠道数据");
                return;
            }

            this.gameManager.deleteGame(m);
            Log.i("#游戏管理(操作员:"+ tAdmin.getFullName()+")#删除游戏:"+this.game.toJSON().toString());
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

    public UGame getGame() {
        return game;
    }

    public void setGame(UGame game) {
        this.game = game;
    }

    public int getCurrAppID() {
        return currAppID;
    }

    public void setCurrAppID(int currAppID) {
        this.currAppID = currAppID;
    }
}
