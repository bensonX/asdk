package org.alan.asdk.sdk.zhifubao;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.ISDKOrderListener;
import org.alan.asdk.sdk.ISDKScript;
import org.alan.asdk.sdk.ISDKVerifyListener;
import org.alan.asdk.sdk.SDKVerifyResult;
import org.alan.asdk.utils.DateHelper;
import net.sf.json.JSONObject;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 支付宝登录认证 Created by ant on 2015/8/10.
 */
public class ZFBSDK implements ISDKScript {

    private static Map<String, AlipayClient> clientMap = new HashMap<>();


    public static AlipayClient getClient(Params params) {
        AlipayClient client = clientMap.get(params.appId);
        if (client != null) {
            Log.i("<支付宝> 获取client 成功 , 使用已保存的client , " + params);
            return client;
        }
        Log.i("<支付宝> 无法获取已有的client , 开始初始化... , " + params);
        client = new DefaultAlipayClient(params.url, params.appId, params.privateKey, "json", "UTF-8", params.publicKey, "RSA2");
        clientMap.put(params.appId, client);
        Log.i("<支付宝> client , 初始化完成 , " + params);
        return client;
    }

    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {
        try {
            Log.i("<支付宝>验签开始: extension = " + extension);

            JSONObject jsonObject = JSONObject.fromObject(extension);
            String authorizationCode = jsonObject.getString("auth_code");
            AlipayClient client = getClient(new Params(channel));
            AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
            request.setGrantType("authorization_code");
            request.setCode(authorizationCode);
            AlipaySystemOauthTokenResponse response = client.execute(request);
            if (response.isSuccess()) {
                String userId = response.getUserId();
                String accessToken = response.getAccessToken();
                Log.i("<支付宝>获取用户信息成功 , extension = " + extension + " , userId = " + userId + "accessToken = " + accessToken);
                callback.onSuccess(new SDKVerifyResult(true, userId, accessToken, null));
            } else {
                callback.onFailed("<支付宝>验签失败: 返回结果 = " + response.getBody() + ", extension = " + extension);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback != null) {
            //签名
            try {
                UChannel channel = order.getChannel();
                Map<String, String> params = new HashMap<>();
                params.put("app_id", channel.getCpAppID());
                params.put("method", "alipay.trade.app.pay");
                params.put("charset", "utf-8");
                params.put("sign_type", "RSA2");
                params.put("timestamp", new DateHelper().format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                params.put("version", "1.0");
                params.put("format", "json");
                params.put("notify_url", channel.getMaster().getPayCallbackUrl());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("subject", "百战斗斗堂 "+(order.getDiamond() == 0 ?"月卡":order.getDiamond()+"钻石"));
                jsonObject.put("out_trade_no", order.getOrderID());
                jsonObject.put("total_amount", order.getMoney());
//                jsonObject.put("total_amount", 0.01);//FIXME: 此处不能存在于正式环境中
                jsonObject.put("product_code", "QUICK_MSECURITY_PAY");
                jsonObject.put("partner",channel.getCpPayID());
                jsonObject.put("seller_id",channel.getCpPayID());
                jsonObject.put("timeout_express", "30m");
                jsonObject.put("extra_common_param", channel.getAppID() + "|" + channel.getCpPayID());
                params.put("biz_content", jsonObject.toString());
                String signString = AlipaySignature.getSignCheckContentV2(params);
                String sign = AlipaySignature.rsa256Sign(signString, order.getChannel().getCpPayPriKey(), "utf8");
                params.put("sign", sign);
                Log.i("<支付宝>获取订单签名 =" + signString + "&sign=" + sign);
                callback.onSuccess(urlEncode(params,sign));
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("<支付宝>获取订单签名失败! orderId =" + order.getOrderID());
            }
        }
    }

    private String urlEncode(Map<String, String> params , String sign) {
        try{
            Set<String> keys = params.keySet();
            for (String key : keys) {
                String value = URLEncoder.encode(params.get(key),"utf-8");
                params.put(key,value);
            }
            return AlipaySignature.getSignCheckContentV2(params)+"&sign="+URLEncoder.encode(sign,"utf-8");
        }catch (Exception e){
             e.printStackTrace();
        }
        return null;
    }

    public static class Params {

        String appId;

        String privateKey;

        String publicKey;

        String url;

        public Params(UChannel channel) {
            this.appId = channel.getCpAppID();
            this.privateKey = channel.getCpPayPriKey();
            this.publicKey = channel.getCpPayKey();
            this.url = channel.getMaster().getAuthUrl();
        }

        public Params(String appId , String privateKey ,String publicKey){
            this.appId = appId;
            this.privateKey=privateKey;
            this.publicKey=publicKey;
            this.url = "https://openapi.alipay.com/gateway.do";
        }

        @Override
        public String toString() {
            return "Params{" +
                    "appId='" + appId + '\'' +
                    ", privateKey='" + privateKey + '\'' +
                    ", publicKey='" + publicKey + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

}
