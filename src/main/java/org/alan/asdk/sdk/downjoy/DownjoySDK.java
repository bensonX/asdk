package org.alan.asdk.sdk.downjoy;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.utils.EncryptUtils;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 当乐SDK
 * Created by ant on 2015/2/9.
 */
public class DownjoySDK implements ISDKScript {


    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {

            JSONObject json = JSONObject.fromObject(extension);
            final String mid = json.getString("accountID");
            final String token = json.getString("token");
            final String account = json.getString("account");

            Map<String, String> data = assemblyParameters(channel, mid, token);

            THttpAgent.getInstance().get(channel.getMaster().getAuthUrl(), data, new UHttpCallback() {
                @Override
                public void completed(String result) {
                    try {
                        Log.i(result);
                        JSONObject res = JSONObject.fromObject(result);

                        if (res != null) {
                            int code = res.getInt("valid");
                            if (code == 1) {
                                SDKVerifyResult vResult = new SDKVerifyResult(true,mid,account,null);
                                callback.onSuccess(vResult);
                                return;
                            }
                        }
                        callback.onFailed(channel.getMaster().getSdkName() + " 验证失败. 返回消息是 " + result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    callback.onFailed(channel.getMaster().getSdkName() + " 验证失败. 返回消息是 " + result);
                }

                @Override
                public void failed(String e) {

                    callback.onFailed(channel.getMaster().getSdkName() + " 验证失败. 返回消息是 " + e);
                }

            });


        } catch (Exception e) {
            callback.onFailed(channel.getMaster().getSdkName() + " 验证失败 系统错误: " + e.getMessage());
            Log.e(e.getMessage());
        }

    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {


        if (callback != null) {
            callback.onSuccess("");
        }
    }

    private Map<String, String> assemblyParameters(UChannel channel, String sid, String token) {

        Map<String, String> data = new HashMap<String, String>();
        data.put("appid", channel.getCpAppID());
        data.put("umid", sid);
        data.put("token", token);
        data.put("sig", getSig(channel, token, sid));

        return data;
    }

    private String getSig(UChannel channel, String token, String sid) {

        return EncryptUtils.md5(channel.getCpAppID() + "|" + channel.getCpAppKey() + "|" + token + "|" + sid);
    }


}
