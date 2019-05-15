package com.springboot.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class NoteUtil {
    private static final String ACCOUNTSID = "0b4f09f7af6044dca86a373525ecf21a";
    private static final String AUTHTOKEN = "eff49d5983c3e3c693d3dcb2ab86a878";
    private static final String RESTURL = "https://open.ucpaas.com/ol/sms";
    private static final String APPID = "1e6e07e92b984886b3c96ee63b4d0957";
    private static final String TEMPLETEID = "249500";

    public static HttpResponse post(CloseableHttpClient client, String url, String body)
            throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

        // 设置连接超时,设置读取超时
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();
        httpPost.setConfig(requestConfig);

        if (body != null && body.length() > 0) {
            // 设置参数
            StringEntity se = new StringEntity(body, "UTF-8");
            httpPost.setEntity(se);
        }
        return client.execute(httpPost);
    }

    public static boolean checkNum(String mobile, String param) {
        CloseableHttpClient httpClient = null;
        String url = getSmsRestUrl();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sid", ACCOUNTSID);
        jsonObject.put("token", AUTHTOKEN);
        jsonObject.put("appid", APPID);
        jsonObject.put("templateid", TEMPLETEID);
        jsonObject.put("param", param);
        jsonObject.put("mobile", mobile);

        String body = jsonObject.toJSONString();

        try {
            httpClient = HttpConnectionManager.getInstance().getHttpClient();
            HttpResponse response = post(httpClient, url, body);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static String getSmsRestUrl() {
        StringBuffer sb = new StringBuffer();
        return sb.append(RESTURL).append("/sendsms").toString();
    }
}
