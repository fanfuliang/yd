package com.yudao.lianying.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.Map;

/**
 * @Description TODO
 * @Author gengweiqiang
 * @date 2021/12/13 11:52
 **/

public class HttpUtils {
    public static String getTenantAccessToken(String tokenUrl,String appId,String appSecret) {

        CloseableHttpResponse response = null;

        //创建httpclient对象
        CloseableHttpClient client = null;
        String respBody;
        try {
            client = HttpClients.custom()
                    .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
                            //忽略掉对服务器端证书的校验
                            .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                            .build(), NoopHostnameVerifier.INSTANCE))
                    .build();
            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(tokenUrl);
            // 请求头设置
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            // 情求体设置
            JSONObject pa = new JSONObject();
            pa.put("app_id", appId);
            pa.put("app_secret", appSecret);

            httpPost.setEntity(new StringEntity(pa.toJSONString(), "utf-8"));

            //执行请求操作，并拿到结果
            response = client.execute(httpPost);
            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                JSONObject res = JSONObject.parseObject(EntityUtils.toString(entity));
                return res.containsKey("tenant_access_token") ? res.getString("tenant_access_token") : "";
            }
        } catch (Exception e) {

        } finally {
            try {
                if (client != null) {
                    client.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {

            }
        }

        return null;
    }
}



