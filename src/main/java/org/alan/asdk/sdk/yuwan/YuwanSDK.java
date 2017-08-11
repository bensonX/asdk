package org.alan.asdk.sdk.yuwan;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.ISDKOrderListener;
import org.alan.asdk.sdk.ISDKScript;
import org.alan.asdk.sdk.ISDKVerifyListener;
import org.alan.asdk.sdk.SDKVerifyResult;
import net.sf.json.JSONObject;

/**
 * 鱼丸SDK
 *
 * @author Lance Chow
 * @create 2016-07-06 9:27
 */
public class YuwanSDK implements ISDKScript {
    @Override
    public void verify(UChannel channel, String extension, ISDKVerifyListener callback) {
        try {
            JSONObject object = JSONObject.fromObject(extension);
            String username = object.getString("username");
            String userID = object.getString("userID");
            SDKVerifyResult vResult = new SDKVerifyResult(true, userID, username, username);
            callback.onSuccess(vResult);

        }catch (Exception e){
            e.printStackTrace();
            callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the result is " + extension);
        }


    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback!=null){
            callback.onSuccess("");
        }
    }
}
