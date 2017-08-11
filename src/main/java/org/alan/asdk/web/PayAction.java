package org.alan.asdk.web;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.StateCode;
import org.alan.asdk.entity.*;
import org.alan.asdk.sdk.ISDKOrderListener;
import org.alan.asdk.sdk.ISDKScript;
import org.alan.asdk.sdk.SDKVerifyResult;
import org.alan.asdk.service.UChannelManager;
import org.alan.asdk.service.UGoodsManager;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.service.UUserManager;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 请求获取订单号
 */
@Controller
@Scope("prototype")
@Namespace("/pay")
public class PayAction extends UActionSupport {
    /**
     * 玩家在TSDK Server登录的token
     */
    private String token;

    /**
     * 渠道ID
     */
    private int channelID;
    private String productID;  //当前商品ID
    private String roleID;      //玩家在游戏服中的角色ID
    private String roleName;    //玩家在游戏服中的角色名称
    private String serverID;    //玩家所在的服务器ID
    private String serverName;  //玩家所在的服务器名称
    private String userID;

    @Autowired
    private UUserManager userManager;

    @Autowired
    private UOrderManager orderManager;

    @Autowired
    private UGoodsManager goodsManager;

    @Autowired
    private UChannelManager channelManager;

    /**
     * 获取订单
     * 使用参数
     * token
     * productID
     */
    @Action("getOrderID")
    public void getOrderID() {
        //Log.i("getOrder begin : token=" + token + ", channelID=" + channelID +", productID=" + productID +", roleID=" + roleID + ", roleName=" + roleName +", serverID=" + serverID + ", serverName=" + serverName + ", userID=" + userID);
        try {

            UUser user = userManager.getUUserByToken(token);

            if (user == null) {
                if ("getFacebookOrderID".equals(token)){
                    getFacebookOrder();
                    return;
                }
                renderState(StateCode.CODE_USER_NONE, null);
                Log.i("<订单>拉取订单失败 , token:"+token+",不能找到玩家");
                return;
            }
            UGame game = user.getGame();
            if (game==null){
                renderState(StateCode.CODE_GAME_NONE, null);
                return;
            }
//            String rightToken = UGenerator.generateToken(user,game.getAppkey(),null);
//            if(!rightToken.equals(token)){
//                renderState(StateCode.CODE_USER_NONE, null);
//                Log.i("<订单>拉取订单失败 ,token:"+token+",验证失败,token不匹配 rightToken = "+rightToken);
//                return;
//            }

            /**
             * 通过productID获取UGoods对象
             */
            final UChannel channel = user.getChannel();
            if (channel==null){
                renderState(StateCode.CODE_CHANNEL_NONE, null);
                Log.i("<订单>拉取订单失败 ,token:"+token+", 找不到channel");
                return;
            }
            final UGoods goods = goodsManager.getGoodsByProductID(channel.getGoodsConfigID(), productID);

            if(goods == null){
                Log.i("<订单>拉取订单失败 ,token:"+token+", 找不到goods , productID = "+productID);
                renderState(StateCode.GOODS_ID_NONE, null);
                return;
            }
            int money = goods.getMoney();
            UOrder order = orderManager.generateOrder(user, money, roleID, "#unknownName#", serverID, serverName, productID ,goods.getDiamond());

            if (order != null) {
                final String scriptName = user.getChannel().getMaster().getVerifyClass();
                if (!StringUtils.isEmpty(scriptName)) {
                    ISDKScript script = (ISDKScript) Class.forName(scriptName).newInstance();

                    final UOrder finalOrder = order;
                    script.onGetOrderID(user, order, new ISDKOrderListener() {
                        @Override
                        public void onSuccess(String jsonStr) {
                            JSONObject data = new JSONObject();
                            data.put("orderID", finalOrder.getOrderID()+"");
                            data.put("extension", jsonStr);
                            renderState(StateCode.CODE_AUTH_SUCCESS, data);
                            Log.i("Role ["+roleID+"] get order ID ["+ finalOrder.getOrderID()+"]success");
                        }

                        @Override
                        public void onFailed(String err) {

                            JSONObject data = new JSONObject();
                            data.put("orderID", finalOrder.getOrderID()+"");
                            renderState(StateCode.CODE_AUTH_SUCCESS, data);
                            Log.i("get order ID["+ finalOrder.getOrderID()+"] fail , err is [" + err+"]");
                        }
                    });
                }

            }


        } catch (Exception e) {
            renderState(StateCode.CODE_ORDER_ERROR, null);
            Log.e(e.getMessage(), e);
        }
    }

    public void getFacebookOrder(){
        try {
            UChannel channel = channelManager.queryChannel(channelID);
            if (channel == null) {
                renderState(StateCode.CODE_CHANNEL_NONE, null);
                return;
            }
            UUser user = userManager.getUserByCpID(channel.getAppID(), channel.getChannelID(), userID);
            if (user == null) {//如果没有则创建一个新账号
                SDKVerifyResult result = new SDKVerifyResult();
                result.setUserID(userID);
                result.setUserName(userID);
                result.setNickName(userID);
                user = userManager.generateUser(channel, result);
            }
            final UGoods goods = goodsManager.getGoodsByProductID(channel.getGoodsConfigID(), productID);
            int money = goods.getMoney();
            UOrder order = orderManager.generateOrder(user, money, roleID, "#unknownName#", serverID, serverName, productID, goods.getDiamond());
            if (order != null) {
                final String scriptName = user.getChannel().getMaster().getVerifyClass();
                if (!StringUtils.isEmpty(scriptName)) {
                    ISDKScript script = (ISDKScript) Class.forName(scriptName).newInstance();
                    final UOrder finalOrder = order;
                    script.onGetOrderID(user, order, new ISDKOrderListener() {
                        @Override
                        public void onSuccess(String jsonStr) {
                            JSONObject data = new JSONObject();
                            data.put("orderID", finalOrder.getOrderID() + "");
                            data.put("extension", jsonStr);
                            Log.i("Role [" + roleID + "] get order ID [" + finalOrder.getOrderID() + "]success");
                            renderState(StateCode.CODE_AUTH_SUCCESS, data);
                        }

                        @Override
                        public void onFailed(String err) {

                            JSONObject data = new JSONObject();
                            data.put("orderID", finalOrder.getOrderID() + "");
                            Log.i("get order ID[" + finalOrder.getOrderID() + "] fail , err is [" + err + "]");
                            renderState(StateCode.CODE_AUTH_SUCCESS, data);
                        }
                    });
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(e.getMessage(),e);
        }
    }

    private void renderState(int state, JSONObject data) {
        JSONObject json = new JSONObject();
        json.put("state", state);
        json.put("data", data);

        super.renderJson(json.toString());
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }


    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
