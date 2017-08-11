package org.alan.asdk.sdk.mumayi;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 木蚂蚁
 * Created by ant on 2015/4/30.
 */
public class MuMaYiSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {


        try {

            JSONObject json = JSONObject.fromObject(extension);
            final String sid = json.getString("uid");
            final String username = json.getString("username");
            final String token = json.getString("token");

            Map<String, String> params = new HashMap<String, String>();
            params.put("uid", sid);
            params.put("token", token);

            String url = channel.getMaster().getAuthUrl();

            UHttpAgent.getInstance().post(url, params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {
                        if ("success".equals(result)) {

                            callback.onSuccess(new SDKVerifyResult(true, sid, username, ""));
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

    public static boolean verify(String sign, String appKey, String orderId) throws UnsupportedEncodingException {
        if (sign.length() < 14) {
            return false;
        }
        String verityStr = sign.substring(0, 8);
        sign = sign.substring(8);
        String temp = MD5Util.toMD5(sign);
        if (!verityStr.equals(temp.substring(0, 8))) {
            return false;
        }
        String keyB = sign.substring(0, 6);

        String randKey = keyB + appKey;

        randKey = MD5Util.toMD5(randKey);

        byte[] signB = Base64.decodeFast(sign.substring(6));
        int signLength = signB.length;
        String verfic = "";
        for (int i = 0; i < signLength; i++) {
            char b = (char) (signB[i] ^ randKey.getBytes()[i % 32]);
            verfic += String.valueOf(b);
        }
        return verfic.equals(orderId);
    }
}
