package org.alan.asdk.sdk.txmsdk;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.ISDKOrderListener;
import org.alan.asdk.sdk.ISDKScript;
import org.alan.asdk.sdk.ISDKVerifyListener;
import org.alan.asdk.sdk.SDKVerifyResult;
import net.sf.json.JSONObject;

/**
 * 腾讯应用宝
 * Created by ant on 2015/10/14.
 */
public class TXMSDK implements ISDKScript {

    @Override
    public void verify(UChannel channel, String extension, ISDKVerifyListener callback) {

        try {

            JSONObject json = JSONObject.fromObject(extension);
            int type = json.getInt("accountType");
            String openId = json.getString("openId");

            String accountType = "qq";
            if (type == 1) {
                accountType = "wx";
            }

            callback.onSuccess(new SDKVerifyResult(true, openId, accountType + "-" + openId, ""));

        } catch (Exception e) {
            callback.onFailed(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback != null) {
            callback.onSuccess("");
        }
    }
}
