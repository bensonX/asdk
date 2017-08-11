package org.alan.asdk.sdk.haima;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 海马
 * Created by Administrator on 2015-12-08.
 */
public class HaiMaSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {
        JSONObject js = JSONObject.fromObject(extension);
        String token = js.getString("token");
        final String uid = js.getString("accountID");

        Map<String,String> params = new HashMap<String, String>();
        params.put("appid", channel.getCpAppID());
        params.put("t", token);
        params.put("uid", uid);

        String url = channel.getMaster().getAuthUrl();

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String content = UHttpAgent.getInstance().post(url,headers,params,"UTF-8");
        try {
            if (content.indexOf("success") >= 0) {
                String accountID = content.split("&")[1];
                SDKVerifyResult result = new SDKVerifyResult(true, accountID, null, null);
                callback.onSuccess(result);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the result is" + content);
    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback != null) {
            callback.onSuccess("");
        }
    }
}
