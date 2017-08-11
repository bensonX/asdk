package org.alan.asdk.sdk.ouwan;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.ISDKOrderListener;
import org.alan.asdk.sdk.ISDKScript;
import org.alan.asdk.sdk.ISDKVerifyListener;
import org.alan.asdk.sdk.SDKVerifyResult;
import org.alan.asdk.utils.EncryptUtils;
import net.sf.json.JSONObject;

/**
 * 偶玩
 * Created by ant on 2015/5/4.
 */
public class OuWanSDK implements ISDKScript {
    @Override
    public void verify(UChannel channel, String extension, ISDKVerifyListener callback) {

        try {

            JSONObject json = JSONObject.fromObject(extension);
            final String uid = json.getString("uid");
            String sign = json.getString("sign");
            String st = json.getString("st");

            if (Math.abs(System.currentTimeMillis() / 1000 - Integer.valueOf(st)) > 600) {
                callback.onFailed("The token is out of date");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(uid).append("&").append(st).append("&").append(channel.getCpAppSecret());
            String vCode = EncryptUtils.md5(sb.toString()).toLowerCase();

            if (vCode.equals(sign)) {
                callback.onSuccess(new SDKVerifyResult(true, uid, "", ""));
            } else {
                callback.onFailed(channel.getMaster().getSdkName() + " verify execute failed.uid" + uid + ";ext:" + extension);
            }


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
