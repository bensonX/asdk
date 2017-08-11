package org.alan.asdk.sdk.soha;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 越南
 *
 * @author Lance Chow
 *         2016-09-26 14:34
 */
public class SohaGame  implements ISDKScript {
    @Override
    public void verify(UChannel channel, String extension, ISDKVerifyListener callback) {
        try {
            JSONObject object = JSONObject.fromObject(extension);
            String token = object.getString("token");
            String verifyUrl = channel.getMaster().getAuthUrl();
            Map<String,String> params = new HashMap<>();
            params.put("access_token",token);
            String resultStr = UHttpAgent.getInstance().get(verifyUrl,params);
            if (StringUtils.isEmpty(resultStr)){
                Log.i("SohaGame verify error , result is null , extension = "+extension);
            }
            JSONObject result = JSONObject.fromObject(resultStr);
            if (!"success".equals(result.getString("status"))){
                Log.i("SohaGame verify error , result is null , result = "+resultStr);
                return;
            }

            JSONObject userInfo = result.getJSONObject("user_info");

            SDKVerifyResult vResult = new SDKVerifyResult(true, userInfo.getString("user_id"), userInfo.getString("username"),userInfo.getString("username"));

            callback.onSuccess(vResult);

        }catch (Exception e){
            e.printStackTrace();
            Log.i("SohaGame verify error , Exception = " + e.toString());
        }
    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {

    }
}
