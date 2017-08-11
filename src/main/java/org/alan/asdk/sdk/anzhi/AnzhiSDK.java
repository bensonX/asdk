package org.alan.asdk.sdk.anzhi;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.utils.Base64;
import org.alan.asdk.utils.TimeFormater;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 安智
 * Created by ant on 2015/4/29.
 */
public class AnzhiSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, final String extension, final ISDKVerifyListener callback) {

        try {
            Log.i("<安智>登录验证开始:extension = "+extension);
            JSONObject json = JSONObject.fromObject(extension);
            final String uid = json.getString("uid");
            final String sid = json.getString("sid");

            Map<String, String> params = new HashMap<>();
            params.put("time", TimeFormater.format_yyyyMMddHHmmssSSS(new Date()));
            params.put("appkey", channel.getCpAppKey());
            params.put("sid", sid);

            String sign = Base64.encode(channel.getCpAppKey() + sid + channel.getCpAppSecret(), "UTF-8");

            params.put("sign", sign);

            String url = channel.getMaster().getAuthUrl();

            UHttpAgent.getInstance().post(url, params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {

                        JSONObject json = JSONObject.fromObject(result);
                        String sc = json.getString("sc");
                        if ("1".equals(sc)) {

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
