package org.alan.asdk.sdk.meizu;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.*;
import org.alan.asdk.utils.EncryptUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ant on 2015/4/30.
 */
public class MeizuSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {
        try {

            JSONObject json = JSONObject.fromObject(extension);

            String app_id = channel.getCpAppID();

            final String uid = json.getString("uid");
            String session_id = json.getString("session");
            String ts = "" + System.currentTimeMillis();
            String sign_type = "md5";

            StringBuilder sb = new StringBuilder();
            sb.append("app_id=").append(app_id).append("&")
                    .append("session_id=").append(session_id).append("&")
                    .append("ts=").append(ts).append("&")
                    .append("uid=").append(uid).append(":").append(channel.getCpAppSecret());

            String sign = EncryptUtils.md5(sb.toString());

            Map<String, String> params = new HashMap<String, String>();
            params.put("app_id", app_id);
            params.put("session_id", session_id);
            params.put("uid", uid);
            params.put("ts", ts);
            params.put("sign_type", sign_type);
            params.put("sign", sign);

            String url = channel.getMaster().getAuthUrl();

            UHttpAgent.getInstance().post(url, params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {
                        Log.e("The auth result is " + result);

                        JSONObject json = JSONObject.fromObject(result);
                        int code = json.getInt("code");


                        if (code == 200) {

                            callback.onSuccess(new SDKVerifyResult(true, uid, "", ""));
                            return;

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the post result is " + result);
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
