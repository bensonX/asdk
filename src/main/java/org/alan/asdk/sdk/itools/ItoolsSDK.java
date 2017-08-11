package org.alan.asdk.sdk.itools;

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
 * Itools iOS 渠道
 * Created by ant on 2015/8/10.
 */
public class ItoolsSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {

            JSONObject json = JSONObject.fromObject(extension);
            final String uid = json.getString("accountID");
            String sessionid = json.getString("token");
            final String userName = json.getString("account");


            Map<String, String> params = new HashMap<String, String>();
            params.put("appid", channel.getCpAppID());
            params.put("sessionid", sessionid);

            StringBuilder sb = new StringBuilder();
            sb.append("appid=").append(channel.getCpAppID()).append("&sessionid=").append(sessionid);
            String sign = EncryptUtils.md5(sb.toString()).toLowerCase();

            params.put("sign", sign);

            String url = channel.getMaster().getAuthUrl();

            THttpAgent.getInstance().get(url, params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {

                        Log.e("The auth result is " + result);

                        if (result != null && result.contains("success")) {
                            SDKVerifyResult vResult = new SDKVerifyResult(true, uid, userName, "");

                            callback.onSuccess(vResult);

                            return;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the result is " + result);
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
