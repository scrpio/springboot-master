package com.springboot.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WeatherUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherUtil.class);
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:63.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";
    /**
     * Mob全国天气预报接口
     */
    private final static String WEATHER_IP = "http://apicloud.mob.com/v1/weather/ip?key=&ip=";
    private final static String CITYS = "http://apicloud.mob.com/v1/weather/citys?key=";
    private final static String WEATHER_CITY = "http://apicloud.mob.com/v1/weather/query?key=";

    /**
     * 发送HttpGet请求
     *
     * @param url 请求地址
     * @return 返回字符串
     */
    public static String sendGet(String url) {
        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", userAgent);
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * 根据IP查询天气
     *
     * @param ip ip地址
     * @return
     */
    public static String getWeatherInfo(String ip) {
        if (null != ip) {
            String url = WEATHER_IP + ip;
            return sendGet(url);
        }
        return null;
    }

    /**
     * 获取城市列表
     *
     * @return
     */
    public static String getCitys() {
        return sendGet(CITYS);
    }

    /**
     * 根据城市名查询天气
     *
     * @param city
     * @param province
     * @return
     */
    public static String getWeatherByCity(String city, String province) {
        String str = "&city=" + city + "&province=" + province;
        return sendGet(WEATHER_CITY + str);
    }
}
