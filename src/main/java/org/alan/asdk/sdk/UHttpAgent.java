package org.alan.asdk.sdk;

import org.alan.asdk.common.Log;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by ant on 2015/10/12.
 */
public class UHttpAgent {

    private int connectTimeout = 30000;  //30s
    private int socketTimeout = 30000;   //5s

    private final int defaultMaxConnPerHost  = 200;
    private final int defaultMaxTotalConn    = 1000;//最大总连接数

    public static void main(String[] args) {
        Map<String,String> params = new HashMap<>();
        params.put("userName","user27");
        params.put("pwd","j4VDZy1ZkxSqjjrmZAQmkQ==");
        System.out.println(UHttpAgent.getInstance().post("http://192.168.2.21:8080/user/account/certify",params));

    }

    private PoolingHttpClientConnectionManager connectionManager;

    private UHttpAgent() {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(defaultMaxConnPerHost);//连接池最大并发连接数
        connectionManager.setDefaultMaxPerRoute(defaultMaxTotalConn);//单路由最大并发数
    }

    public static UHttpAgent getInstance() {
        return new UHttpAgent();
    }

    public static UHttpAgent newInstance() {
        return new UHttpAgent();
    }

    public void get(String url, Map<String, String> params, UHttpCallback callback) {
        get(url, null, params, "UTF-8", callback);
    }

    public void post(String url, Map<String, String> params, UHttpCallback callback) {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (params != null) {

            for (String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, params.get(key)));
            }
        }

        post(url, null, new UrlEncodedFormEntity(pairs, Charset.forName("UTF-8")), callback);
    }

    /**
     * http post 请求
     *
     * @param url      请求地址
     * @param headers  请求头
     * @param params   参数
     * @param encoding 编码 UTF-8等
     * @return
     */

    public String post(String url, Map<String, String> headers, Map<String, String> params, String encoding) {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (params != null) {

            for (String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, params.get(key)));
            }
        }

        return post(url, headers, new UrlEncodedFormEntity(pairs, Charset.forName(encoding)));
    }

    /**
     * http get 请求 默认请求头 参数编码UTF-8
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public String get(String url, Map<String, String> params) {

        return get(url, null, params, "UTF-8");
    }

    /**
     * http post 请求 默认请求头 参数编码UTF-8
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public String post(String url, Map<String, String> params) {

        return post(url, null, params, "UTF-8");
    }

    /**
     * http get 异步请求方式 TODO: 目前实现还是同步，异步待实现
     *
     * @param url
     * @param headers
     * @param params
     * @param encoding
     * @param callback
     */
    public String get(String url, Map<String, String> headers, Map<String, String> params, String encoding, UHttpCallback callback) {

        String result = get(url, headers, params, encoding);

        if (result != null) {
            callback.completed(result);
        } else {
            callback.failed(url + " failed");
        }
        return result;
    }

    /**
     * http post 异步请求方式 TODO: 目前实现还是同步，异步待实现
     *
     */
    public void post(String url, Map<String, String> headers, HttpEntity entity, UHttpCallback callback) {
        String result = post(url, headers, entity);
        if (result != null) {
            callback.completed(result);
        } else {
            callback.failed(url + "failed");
        }
    }


    /**
     * http get 请求
     *
     * @param url      请求地址
     * @param headers  请求头
     * @param params   参数
     * @param encoding 编码 UTF-8等
     * @return
     */
    public String get(String url, Map<String, String> headers, Map<String, String> params, String encoding) {

        CloseableHttpClient httpClient = createClient();

        String fullUrl = url;
        String urlParams = parseGetParams(params, encoding);

        if (urlParams != null) {
            if (url.contains("?")) {
                fullUrl = url + "&" + urlParams;
            } else {
                fullUrl = url + "?" + urlParams;
            }
        }
        Log.d("--UHttpAgent-- 发送请求到:"+fullUrl);
        HttpGet getReq = new HttpGet(fullUrl.trim());
        getReq.setHeaders(parseHeaders(headers));
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        getReq.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = httpClient.execute(getReq);
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity,"UTF-8");
            response.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String post(String url, Map<String, String> headers, HttpEntity entity) {

        try {
            Log.d("--UHttpAgent-- 发送请求到:"+url+",参数: "+EntityUtils.toString(entity,"UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        CloseableHttpResponse response = null;
        CloseableHttpClient client = createClient();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        HttpPost httpPost = new HttpPost(url);
        if (entity!=null){
            httpPost.setEntity(entity);
        }
        httpPost.setHeaders(parseHeaders(headers));
        httpPost.setConfig(requestConfig);
        try {
            response = client.execute(httpPost);
            entity = response.getEntity();
            return EntityUtils.toString(entity,"UTF-8");
        } catch (IOException e) {
            if (response!=null){
                try {
                    response.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            destroy(client);
//            e.printStackTrace();
        }finally {
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    //解析请求头
    private Header[] parseHeaders(Map<String, String> headers) {
        if (null == headers || headers.isEmpty()) {
            return getDefaultHeaders();
        }

        Header[] hs = new BasicHeader[headers.size()];
        int i = 0;
        for (String key : headers.keySet()) {
            hs[i] = new BasicHeader(key, headers.get(key));
            i++;
        }

        return hs;
    }

    //获取默认的请求头
    private Header[] getDefaultHeaders() {
        Header[] hs = new BasicHeader[2];
        hs[0] = new BasicHeader("Content-Type", "application/x-www-form-urlencoded");
        hs[1] = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        return hs;
    }

    //将参数解析成get请求的参数格式
    private String parseGetParams(Map<String, String> data, String encoding) {
        if (data == null || data.size() <= 0) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        Iterator<String> keyItor = data.keySet().iterator();
        while (keyItor.hasNext()) {
            String key = keyItor.next();
            String val = data.get(key);

            try {
                result.append(key).append("=").append(URLEncoder.encode(val, encoding).replace("+", "%2B")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result.deleteCharAt(result.length() - 1).toString();

    }

    //初始化， 创建httpclient实例
    private CloseableHttpClient createClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
        return httpClient;
    }



    //销毁
    public void destroy(CloseableHttpClient client) {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
