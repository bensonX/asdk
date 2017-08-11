package org.alan.asdk.sdk.dkm;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2017/4/21.
 * 哆可梦登录SDK
 * @author Chow
 * @since 1.0
 */
public class DKMSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {
        JSONObject jsonObject = JSONObject.fromObject(extension);
        String token = jsonObject.getString("token");
        final String userid = jsonObject.getString("userid");
        final String account = jsonObject.getString("account");
        Map<String,String>  params = new HashMap<>();
        params.put("session",token);
        UHttpAgent.getInstance().post(channel.getMaster().getAuthUrl(), params, new UHttpCallback() {
            @Override
            public void completed(String content) {
                try{
                    JSONObject resault = JSONObject.fromObject(content);
                    if (resault.getInt("state")!=1){
                        Log.e("哆可梦角色验证失败 , 服务器返回 state == "+ resault.getInt("state"));
                        return;
                    }
                    JSONObject data = resault.getJSONObject("data");
                    String uid = data.getString("uid");
                    SDKVerifyResult result = new SDKVerifyResult(true,uid,account,userid);
                    callback.onSuccess(result);
                }catch (Exception e){
                     e.printStackTrace();
                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the result is " + content);
                }
            }

            @Override
            public void failed(String err) {
                callback.onFailed(channel.getMaster().getSdkName() + " verify failed. " + err);
            }
        });
    }
    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback!=null){
            callback.onSuccess("");
        }
    }
}
