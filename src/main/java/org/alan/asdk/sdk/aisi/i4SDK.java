package org.alan.asdk.sdk.aisi;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 爱思SDK
 *
 * @author Lance Chow
 * @create 2015-12-28 15:47
 */
public class i4SDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {
        try {
            final JSONObject js = JSONObject.fromObject(extension);
            String token = js.getString("token");

            Map<String,String> params = new HashMap<String, String>();
            params.put("token",token);

            UHttpAgent.getInstance().get(channel.getMaster().getAuthUrl(), params, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {
                        JSONObject json = JSONObject.fromObject(result);
                        int state = json.getInt("status");
                        if (state == 0) {
                            String username = json.getString("username");
                            String userID = json.getString("userid");
                            SDKVerifyResult vResult = new SDKVerifyResult(true, userID, username,null);
                            callback.onSuccess(vResult);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the result is " + result);
                }

                @Override
                public void failed(String e) {
                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. " + e);
                }

            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback!=null){
            callback.onSuccess("");
        }
    }
}
