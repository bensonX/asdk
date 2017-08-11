package org.alan.asdk.web;

import org.alan.asdk.common.Log;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.dto.StateCode;
import org.alan.asdk.entity.TFailOrderInfo;
import org.alan.asdk.entity.UGame;
import org.alan.asdk.entity.UMsdkOrder;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.sdk.UHttpAgent;
import org.alan.asdk.service.UOrderManager;
import org.alan.asdk.utils.UGenerator;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * U8Server向游戏服发送回调通知
 * Created by ant on 2015/2/9.
 */
@Component
public class SendAgent {

    /**
     * 通知游戏服务器回调
     *
     * @param orderManager
     * @param order
     * @return
     */
    public static boolean sendCallbackToServer(UOrderManager orderManager, UOrder order) {
        int state = sendCallback(order);
        if (state == StateCode.CODE_AUTH_SUCCESS) {
            order.setState(PayState.STATE_SUC);
        } else if (state == StateCode.CODE_AUTH_FAILED) {
            order.setState(PayState.STATE_FAILED);
        }
        orderManager.saveOrder(order);
        return state == StateCode.CODE_AUTH_SUCCESS;
    }

    /**
     * 通知服务器回调线程操作
     */
    public static boolean sendCallbackToServer(UOrderManager orderManager, TFailOrderInfo orderInfo) {
        int state = sendCallback(orderInfo.getOrder());
        if (state == StateCode.CODE_AUTH_SUCCESS) {
            UOrder order = orderInfo.getOrder();
            order.setState(PayState.STATE_COMPLETE);
            orderManager.saveOrder(order);
        }
        return state == StateCode.CODE_AUTH_SUCCESS;
    }

    private static int sendCallback(UOrder order) {
        long useTime = System.currentTimeMillis();
        UGame game = order.getGame();
        if (game == null) {
            return StateCode.CODE_GAME_NONE;
        }
        if (StringUtils.isEmpty(game.getPayCallback())) {
            Log.i("["+order.getOrderID()+"]GameServer callback url is null , callback field");
            return StateCode.CODE_GAME_NONE;
        }
        JSONObject response = new JSONObject();
        JSONObject js = order.toJSON();
        response.put("state", StateCode.CODE_AUTH_SUCCESS);
        response.put("data", js);
        response.put("sign", UGenerator.generateSign(StateCode.CODE_AUTH_SUCCESS, order, game.getAppkey()));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        Log.i("Call the GameServer!  : "+game.getPayCallback() +" , params = "+ response.toString());
        String serverRes = UHttpAgent.getInstance().post(game.getPayCallback(), headers, new ByteArrayEntity(response.toString().getBytes(Charset.forName("UTF-8"))));
        useTime = System.currentTimeMillis() - useTime;
        if (!StringUtils.isEmpty(serverRes)) {
            Log.i("["+order.getOrderID()+"]GameServer respones: "+serverRes +" , use " + useTime + "MS");
            return StateCode.CODE_AUTH_SUCCESS;
        } else {
            Log.i("["+order.getOrderID()+"]GameServer respones is null , callback field , use " + useTime + "MS");
            return StateCode.CODE_AUTH_FAILED;
        }
    }


    /**
     * 应用宝需要游戏服务器做特殊处理，这里让游戏服务器提供一个独立的回调通知地址,来处理应用宝等场景
     *
     * @param orderManager o
     * @param order o
     * @return 是否成功
     */
    public static boolean sendMSDKCallbackToServer(UOrderManager orderManager, UMsdkOrder order) {

        UGame game = order.getGame();
        if (game == null) {

            Log.e("send msdk order to game com.u8.server failed. game is null. orderID:%s " + order.getId());
            return false;
        }

        if (StringUtils.isEmpty(game.getMsdkPayCallback())) {
            Log.e("send msdk order to game com.u8.server failed. msdk pay callback url is not configed.");
            return false;
        }

        JSONObject data = new JSONObject();
        data.put("userID", order.getUserID());
        data.put("gameID", order.getAppID());
        data.put("channelID", order.getChannelID());
        data.put("coinNum", order.getCoinNum());
        data.put("firstPay", order.getFirstPay());
        data.put("allMoney", order.getAllMoney());

        JSONObject response = new JSONObject();
        response.put("state", StateCode.CODE_AUTH_SUCCESS);
        response.put("data", data);
        response.put("sign", UGenerator.generateMsdkSign(order));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");

        String serverRes = UHttpAgent.getInstance().post(game.getMsdkPayCallback(), headers, new ByteArrayEntity(response.toString().getBytes(Charset.forName("UTF-8"))));

        if (serverRes.contains("success")) {
            order.setState(PayState.STATE_COMPLETE);
            orderManager.saveUMsdkOrder(order);
            return true;
        }

        return false;
    }

}
