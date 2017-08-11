package org.alan.asdk.sdk.xy;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import net.sf.json.JSONObject;
import org.alan.asdk.sdk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * XY渠道SDK
 * @author Lance Chow
 * @create 2016-01-05 15:15
 */
public class XYSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {
            JSONObject jsonObject = JSONObject.fromObject(extension);
            final String uid = jsonObject.getString("accountID");
            String token = jsonObject.getString("token");

            Map<String , String > params = new HashMap<String, String>();
            params.put("uid",uid);
            params.put("appid",channel.getCpAppID());
            params.put("token",token);

            String url = channel.getMaster().getAuthUrl();

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            String content = UHttpAgent.getInstance().post(url,headers,params,"UTF-8");
            JSONObject js = JSONObject.fromObject(content);
            if (js.getInt("ret")==0){
                SDKVerifyResult result = new SDKVerifyResult(true, uid, null, null);
                Log.i("XY登录成功!uid="+uid);
                callback.onSuccess(result);
            }else {
                Log.i("XY登录失败!uid="+uid+";返回:"+content);
            }
        }catch (Exception e){
            e.printStackTrace();
            callback.onFailed(channel.getMaster().getSdkName() + " 验证失败. 程序逻辑错误!" + e.getMessage());
        }

    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback!=null){
            callback.onSuccess("");
        }
    }
}
