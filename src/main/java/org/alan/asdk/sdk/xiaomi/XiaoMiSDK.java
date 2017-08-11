package org.alan.asdk.sdk.xiaomi;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.*;
import org.alan.asdk.utils.HmacSHA1Encryption;
import org.alan.asdk.utils.JsonUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ant on 2015/4/21.
 */
public class XiaoMiSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {

            JSONObject json = JSONObject.fromObject(extension);

            final String sid = json.getString("uid");
            final String session = json.getString("session");

            Map<String, String> params = new HashMap<>();
            params.put("appId", channel.getCpAppID());
            params.put("session", session);
            params.put("uid", sid);

            String sb = "appId=" + channel.getCpAppID() + "&" +
                    "session=" + session + "&" +
                    "uid=" + sid;

            String signature = HmacSHA1Encryption.HmacSHA1Encrypt(sb, channel.getCpAppSecret());


            params.put("signature", signature);
            UHttpAgent.getInstance().get(channel.getMaster().getAuthUrl(), params, new UHttpCallback() {
                @Override
                public void completed(String content) {

                    Log.e("<小米>auth result is " + content);
                    try {

                        AuthInfo info = (AuthInfo) JsonUtils.decodeJson(content, AuthInfo.class);
                        if (info != null && info.getErrcode() == 200) {
                            SDKVerifyResult vResult = new SDKVerifyResult(true, sid + "", "", "");
                            callback.onSuccess(vResult);
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the auth result is " + content);
                }

                @Override
                public void failed(String e) {
                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. " + e);
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailed(channel.getMaster().getSdkName() + " verify execute failed. the exception is " + e.getMessage());
        }

    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback != null) {
            callback.onSuccess("");
        }
    }
}
