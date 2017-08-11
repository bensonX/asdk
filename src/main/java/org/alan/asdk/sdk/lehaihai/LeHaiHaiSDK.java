package org.alan.asdk.sdk.lehaihai;

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
 * 乐嗨嗨SDK
 * Created by ant on 2015/8/17.
 */
public class LeHaiHaiSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {


            JSONObject json = JSONObject.fromObject(extension);

            final String uid = json.getString("uid");
            final String username = json.getString("username");
            final String token = json.getString("token");

            UHttpAgent httpClient = UHttpAgent.getInstance();

            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("token", token);
            params.put("pid", channel.getCpAppID());

            StringBuilder sb = new StringBuilder();
            sb.append("pid=").append(channel.getCpAppID()).append("&token=").append(token)
                    .append("&username=").append(username).append(channel.getCpAppSecret());
            String sign = EncryptUtils.md5(sb.toString());
            params.put("sign", sign);

            httpClient.post(channel.getMaster().getAuthUrl(), params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    Log.d("The auth result is " + result);

                    JSONObject json = JSONObject.fromObject(result);
                    if (json.containsKey("state") && json.getInt("state") == 1) {

                        callback.onSuccess(new SDKVerifyResult(true, uid, username, ""));
                        return;
                    }

                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the post result is " + result);

                }

                @Override
                public void failed(String e) {
                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. " + e);
                }

            });


        } catch (Exception e) {
            callback.onFailed(channel.getMaster().getSdkName() + " verify execute failed. the exception is " + e.getMessage());
            Log.e(e.getMessage());
        }

    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback != null) {
            callback.onSuccess("");
        }
    }
}
