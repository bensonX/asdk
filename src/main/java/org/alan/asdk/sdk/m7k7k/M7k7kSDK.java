package org.alan.asdk.sdk.m7k7k;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.*;
import org.alan.asdk.utils.EncryptUtils;
import net.sf.json.JSONObject;

/**
 * 7k7k安卓SDK
 *
 * @author Lance Chow
 * @create 2016-04-25 9:49
 */
public class M7k7kSDK implements ISDKScript {

    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {
        JSONObject js = JSONObject.fromObject(extension);
        String url = channel.getMaster().getAuthUrl();
        String vkey = js.getString("sid");
        String appid = channel.getCpAppID();
        final String uid = js.getString("userid");
        String sign = EncryptUtils.md5(uid+vkey+appid+channel.getCpAppSecret());
        url +="/uid/"+uid+"/vkey/"+vkey+"/appid/"+appid+"/sign/"+sign;
        UHttpAgent.getInstance().get(url, null, new UHttpCallback() {
            @Override
            public void completed(String content) {
                try {
                    JSONObject js = JSONObject.fromObject(content);
                    if(js.getInt("status")==0){
                        SDKVerifyResult result = new SDKVerifyResult(true,uid,uid,null);
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
        if (callback != null) {
            callback.onSuccess("");
        }
    }
}
