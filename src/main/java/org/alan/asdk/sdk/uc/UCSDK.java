package org.alan.asdk.sdk.uc;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.*;
import org.alan.asdk.utils.EncryptUtils;
import org.alan.asdk.utils.JsonUtils;
import org.apache.http.entity.ByteArrayEntity;

import java.nio.charset.Charset;
import java.util.*;

/**
 * UC渠道
 */
public class UCSDK implements ISDKScript {

    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        UHttpAgent httpClient = UHttpAgent.getInstance();


        String url = channel.getMaster().getAuthUrl();
        Map<String, Object> proData = assemblyParameters(channel, extension);
        String jsonData = JsonUtils.encodeJson(proData);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        httpClient.post(url, headers, new ByteArrayEntity(jsonData.getBytes(Charset.forName("UTF-8"))), new UHttpCallback() {
            @Override
            public void completed(String result) {

                VerifySessionResponse rsp = (VerifySessionResponse) JsonUtils.decodeJson(result, VerifySessionResponse.class);

                if (rsp != null) {
                    int code = rsp.getState().getCode();
                    if (code == 1) {
                        SDKVerifyResult verifyResult = new SDKVerifyResult(true, rsp.getData().getAccountId(), rsp.getData().getAccountId(), rsp.getData().getNickName());
                        callback.onSuccess(verifyResult);
                        return;
                    }
                }

                callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the post result is " + result);

            }

            @Override
            public void failed(String e) {
                callback.onFailed(channel.getMaster().getSdkName() + " verify failed. " + e);
            }
        });
    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback != null) {
            callback.onSuccess("");
        }
    }

    private Map<String, Object> assemblyParameters(UChannel channel, String extension) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", System.currentTimeMillis()/1000);// 当前系统时间

        Map<String, Object> game = new HashMap<>();
        game.put("gameId", channel.getCpAppID());

        params.put("game", game);

        Map<String, Object> data = new HashMap<>();
        data.put("sid", extension);

        params.put("data", data);
        /*
		 * 签名规则=签名内容+apiKey 假定apiKey=202cb962234w4ers2aaa,sid=abcdefg123456 那么签名原文sid=abcdefg123456202cb962234w4ers2aaa
		 * 签名结果6e9c3c1e7d99293dfc0c81442f9a9984
		 */
        String signSource = getSignData(data) + channel.getCpAppKey();
        // String signSource = "sid=70b37f00-5f59-4819-99ad-8bde027ade00144882"+apiKey;
        String sign = getMD5Str(signSource);// MD5加密签名
        params.put("sign", sign);
//        String body = JsonUtils.encodeJson(params);// 把参数序列化成一个json字符串
//        Log.i("[请求参数]" + body);
        return params;
    }

    private static String getSignData(Map params) {
        StringBuilder content = new StringBuilder();

        // 按照key做排序
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            String value = params.get(key) == null ? "" : params.get(key).toString();
            if (value != null) {
                content.append(key).append("=").append(value);
            } else {
                content.append(key).append("=");
            }
        }

        return content.toString();
    }

    private static String getMD5Str(String str) {

        return EncryptUtils.md5(str);
    }

    /**
     * 验证支付
     *
     * @param channel
     * @param rsp
     * @return
     */
    public boolean verifyPay(UChannel channel, PayCallbackResponse rsp) {

        String signSource = "accountId=" + rsp.getData().getAccountId() + "amount=" + rsp.getData().getAmount() + "callbackInfo=" + rsp.getData().getCallbackInfo();
        if(rsp.getData().getCpOrderId()!=null){
            signSource += "cpOrderId=" + rsp.getData().getCpOrderId();
        }
        signSource = signSource  + "creator=" + rsp.getData().getCreator() + "failedDesc=" + rsp.getData().getFailedDesc() + "gameId=" + rsp.getData().getGameId()
                + "orderId=" + rsp.getData().getOrderId() + "orderStatus=" + rsp.getData().getOrderStatus()
                + "payWay=" + rsp.getData().getPayWay()
                + channel.getCpAppKey();

        String sign = getMD5Str(signSource);

        return sign.equals(rsp.getSign());

    }

    /**
     * 验证支付
     *
     * @param channel
     * @param rsp
     * @return
     */
    public String generatePaySign(UChannel channel, PayCallbackResponse rsp) {

        String signSource = "accountId=" + rsp.getData().getAccountId()
                + "amount=" + rsp.getData().getAmount()
                + "callbackInfo=" + rsp.getData().getCallbackInfo()
                + "cpOrderId=" + rsp.getData().getCpOrderId() + "creator=" + rsp.getData().getCreator() + "failedDesc=" + rsp.getData().getFailedDesc() + "gameId=" + rsp.getData().getGameId()
                + "orderId=" + rsp.getData().getOrderId() + "orderStatus=" + rsp.getData().getOrderStatus()
                + "payWay=" + rsp.getData().getPayWay() + "serverId=" + rsp.getData().getServerId()
                + channel.getCpAppKey();

        return getMD5Str(signSource);

    }
}
