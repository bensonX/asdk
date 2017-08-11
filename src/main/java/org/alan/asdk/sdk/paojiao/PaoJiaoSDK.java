package org.alan.asdk.sdk.paojiao;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.*;
import org.alan.asdk.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 泡椒网
 * Created by ant on 2015/4/30.
 */
public class PaoJiaoSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {
        try {

            final String token = extension;

            Map<String, String> params = new HashMap<String, String>();
            params.put("appId", channel.getCpAppID());
            params.put("token", token);

            String url = channel.getMaster().getAuthUrl();

            UHttpAgent.getInstance().get(url, params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {
                        Log.e("The auth result is " + result);

                        PaoJiaoAuthInfo res = (PaoJiaoAuthInfo) JsonUtils.decodeJson(result, PaoJiaoAuthInfo.class);

                        if (res != null && res.getData() != null && res.getCode() == 1) {

                            callback.onSuccess(new SDKVerifyResult(true, res.getData().getUid(), res.getData().getUserName(), res.getData().getNiceName()));
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
