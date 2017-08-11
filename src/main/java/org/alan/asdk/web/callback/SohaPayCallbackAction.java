package org.alan.asdk.web.callback;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.entity.*;
import org.alan.asdk.sdk.UHttpAgent;
import org.alan.asdk.sdk.soha.CommonDAO;
import org.alan.asdk.service.UChannelManager;
import org.alan.asdk.service.UGoodsManager;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.service.UUserManager;
import org.alan.asdk.utils.EncryptUtils;
import org.alan.asdk.web.SendAgent;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 越南iga充值回调
 *
 * @author Lance Chow
 *         2016-09-27 10:32
 */
@Controller
@Scope("prototype")
@Namespace("/pay/soha")
public class SohaPayCallbackAction extends UActionSupport {
    private String signed_request;

    private String user_id;

    private String checkdata;

    @Value("${config.vn.soha.channel.id}")
    private int vnChannelID;

    @Autowired
    private UOrderManager orderManager;

    @Autowired
    private UChannelManager channelManager;

    @Autowired
    private UUserManager userManager;

    @Autowired
    private UGoodsManager goodsManager;
    @Action("payCallback")
    public void payCallback() {
        //通过app_id 找到channel
        try {
            CommonDAO dao = new CommonDAO();
            UChannel channel = channelManager.queryChannel(vnChannelID);
            if (channel == null){
                Log.i("<SohaGame> callback error , channel is null!!!! , vnChannelId = " + vnChannelID);
                renderJson("failed","");
                return;
            }
            String [] secrets = channel.getCpAppSecret().split(",");
            String res = null;
            for (String secret : secrets) {
                if (!"fail".equals(dao.parse_signed_request(signed_request,secret))){
                    res = dao.parse_signed_request(signed_request,secret);
                    break;
                }
            }
            if (res == null){
                Log.i("<SohaGame> callback error , cannot format the str ="+signed_request);
                return;
            }
            JSONObject object = JSONObject.fromObject(res);
//            String appId = object.getString("app_id");
            String orderId = object.getString("order_id");
            String userId = object.getString("user_id");
            String orderInfo = object.getString("order_info");
            String roleId = object.getString("role_id");
            String areaId = object.getString("area_id");

            //判断是否重复
            UOrder order = orderManager.getOrderByChannelOrderID(orderId);
            if (order != null) {
                Log.i("<SohaGame> callback error , order is exist , params = " + res);
                renderJson("failed"," order is exist ");
                return;
            }
            //查找用户
            UUser user = userManager.getUserByCpID(channel.getAppID(), channel.getChannelID(), userId);
            if (user == null) { //如果为就返回错误
                Log.i("<SohaGame> callback error , userid is not exist , params = " + res);
                renderJson("failed","");
            }
            //查找商品
            UGoods goods = goodsManager.getGoodsBySdkGoodsID(channel.getGoodsConfigID(), orderInfo);
            if (goods == null) {
                Log.i("<SohaGame> callback error , goods isn't exist , params = " + res);
                renderJson("failed","");
                return;
            }

            order = orderManager.generateOrder(user, goods.getMoney(), roleId, "#unknow#", areaId, "#unkonw#", goods.getProductID(), goods.getDiamond());
            order.setChannelOrderID(orderId);
            SendAgent.sendCallbackToServer(this.orderManager, order);
            Log.i("<SohaGame> callback success , params = " + res);
            renderJson("settled","success");
        }catch (Exception e){
            Log.i("<SohaGame> callback error , params = " + signed_request);
            e.printStackTrace();
        }

    }

    @Action("getRoleList")
    public void getRoleList(){
        UChannel channel = channelManager.queryChannel(vnChannelID);
        if (channel == null){
            Log.i("<SohaGame> getRoleList error , channel is null");
            renderJson("1","channel is null");
            return;
        }
        //验证签名
        if (!isValid(channel.getCpAppSecret())){
            renderJson("1","md5 is err");
            return;
        }
        //获取用户
        UUser user = userManager.getUserByCpID(channel.getAppID(),channel.getChannelID(),this.user_id);
        if (user == null){
            Log.i("<SohaGame> getRoleList error , user is null , user_id = "+this.user_id);
            renderJson("1","user is null");
            return;
        }

        //询问游戏服务器
        UGame game = channel.getGame();
        String url = game.getPayCallbackDebug();
        if (url == null){
            Log.i("<SohaGame> getRoleList error , url is null , user_id = "+this.user_id);
            renderJson("1","url is null");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("openID",user.getId()+"");
        String result = UHttpAgent.getInstance().post(url,params);
        renderText(result);
        Log.i("<SohaGame> getRoleList success , user_id = "+this.user_id+" openID = "+user.getId()+",result=" +result);
    }

    public boolean isValid(String key){
        StringBuilder sb = new StringBuilder();
        sb.append(this.user_id).append(key);
        String md5 = EncryptUtils.md5(sb.toString());
        boolean valid = md5.equalsIgnoreCase(this.checkdata);
        if (!valid){
            Log.i("<SohaGame> getRoleList error , md5 is err , str = "+sb.toString() +", md5 = "+md5 +", checkdata="+this.checkdata);
        }
        return valid;
    }

    private void renderJson(String status,String msg){
        JSONObject js = new JSONObject();
        js.put("status",status);
        js.put("message",msg);
        this.renderText(js.toString());
    }

    public String getSigned_request() {
        return signed_request;
    }

    public void setSigned_request(String signed_request) {
        this.signed_request = signed_request;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCheckdata() {
        return checkdata;
    }

    public void setCheckdata(String checkdata) {
        this.checkdata = checkdata;
    }
}
