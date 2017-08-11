package org.alan.asdk.sdk.sogou;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.utils.EncryptUtils;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * sogouSDK登陆验证
 * Created by luo_s on 2016/6/29 0029.
 */
public class SogouSDK implements ISDKScript {
    @Override
    /**
     *
     */ public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {
            JSONObject json = JSONObject.fromObject(extension);
            final String gid = channel.getCpAppID();
            final String userId = json.getString("user_id");
            String sessionKey = json.getString("session_key");
            String app_secret = channel.getCpAppSecret();
            String auth = EncryptUtils.md5("gid=" + gid + "&session_key=" + sessionKey + "&user_id=" + userId + "&" + app_secret);
            String url = channel.getMaster().getAuthUrl();
            Map<String, String> map = new HashMap<>();
            map.put("gid",gid);
            map.put("session_key",sessionKey);
            map.put("user_id",userId);
            map.put("auth",auth);
            UHttpAgent.getInstance().post(url, map, new UHttpCallback() {
                @Override
                public void completed(String content) {
                        JSONObject json = JSONObject.fromObject(content);
                        if (json.has("error")) {
                            callback.onFailed(channel.getMaster().getSdkName() + " <搜狗>用户验证出错" + content);
                            return;
                        }
                        boolean i = json.getBoolean("result");
                        if (i) {
                            SDKVerifyResult vResult = new SDKVerifyResult(true, userId, userId, userId);

                            callback.onSuccess(vResult);

                            return;

                        }
                        callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the result is " + content);
                }

                @Override
                public void failed(String err) {

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
