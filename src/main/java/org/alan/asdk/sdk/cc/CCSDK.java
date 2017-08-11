package org.alan.asdk.sdk.cc;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 虫虫
 *
 * @author Lance Chow
 * @create 2016-04-25 11:51
 */
public class CCSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {
        JSONObject js = JSONObject.fromObject(extension);
        final String userID = js.getString("userid");
        String token = js.getString("token");
        Map<String,String> params =  new HashMap<>();
        params.put("token",token);
        UHttpAgent.getInstance().post(channel.getMaster().getAuthUrl(), params, new UHttpCallback() {
            @Override
            public void completed(String content) {
                try {
                    if (content.equals("success")){
                        SDKVerifyResult result = new SDKVerifyResult(true,userID,null,null);
                        callback.onSuccess(result);
                    }
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
