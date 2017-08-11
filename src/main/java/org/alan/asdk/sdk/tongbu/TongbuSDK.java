package org.alan.asdk.sdk.tongbu;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ant on 2015/8/10.
 */
public class TongbuSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {

            JSONObject json = JSONObject.fromObject(extension);
            String sessionid = json.getString("token");

            Map<String, String> params = new HashMap<String, String>();
            params.put("appid", channel.getCpAppID());
            params.put("session", sessionid);

            String url = channel.getMaster().getAuthUrl();

            THttpAgent.getInstance().get(url, params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {

                        int returnCode = Integer.parseInt(result);

                        if (returnCode > 0) {
                            SDKVerifyResult vResult = new SDKVerifyResult(true, result, result, "");

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
