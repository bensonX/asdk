package org.alan.asdk.sdk.m4399;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.*;
import org.alan.asdk.utils.JsonUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class M4399SDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {

            JSONObject json = JSONObject.fromObject(extension);
            final String uid = json.getString("uid");
            String state = json.getString("token");
            final String nickName = json.getString("nickname");
            final String userName = json.getString("username");


            Map<String, String> params = new HashMap<>();
            params.put("uid", uid);
            params.put("state", state);

            String url = channel.getMaster().getAuthUrl();

            UHttpAgent.getInstance().get(url, params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {

                        Log.i("The auth result is " + result);

                        AuthInfo res = (AuthInfo) JsonUtils.decodeJson(result, AuthInfo.class);


                        if (res != null && res.getCode() == 100) {

                            SDKVerifyResult vResult = new SDKVerifyResult(true, uid, userName, nickName);

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
