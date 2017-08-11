package org.alan.asdk.sdk.muzhiwan;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.*;
import org.alan.asdk.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 拇指玩
 * Created by ant on 2015/4/25.
 */
public class MuZhiWanSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {

            final String token = extension;

            Map<String, String> params = new HashMap<String, String>();
            params.put("token", token);
            params.put("appkey", channel.getCpAppKey());

            String url = channel.getMaster().getAuthUrl();

            UHttpAgent.getInstance().get(url, params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {

                        MuZhiWanAuthInfo res = (MuZhiWanAuthInfo) JsonUtils.decodeJson(result, MuZhiWanAuthInfo.class);

                        if (res != null && res.getCode() == 1) {

                            SDKVerifyResult vResult = new SDKVerifyResult(true, res.getUser().getUid(), res.getUser().getUsername(), "");

                            callback.onSuccess(vResult);

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
