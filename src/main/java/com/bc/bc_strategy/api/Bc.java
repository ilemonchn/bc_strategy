package com.bc.bc_strategy.api;

import com.bc.bc_strategy.data.Datas;
import com.bc.bc_strategy.indicator.Boll;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class Bc {

    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setProxy(new HttpHost("127.0.0.1", 4780)).build())
            .build();

    public static void main(String[] args) throws Exception {
        LocalDateTime dateTime = LocalDateTime.of(2022, 9, 28, 17, 0);
        long sTime = dateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long eTime = dateTime.plusHours(3).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();

        String url = "https://api.binance.com";

        Map<String, Object> map = new HashMap<>();
        map.put("symbol", "BTCUSDT");
        map.put("interval", "1h");
//        map.put("startTime", sTime);
//        map.put("endTime", eTime);
        map.put("limit", 1000);
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append("/api/v3/klines?");
        map.forEach((k,v) -> {
            sb.append(String.format("%s=%s", k, v));
            sb.append("&");
        });
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());

        HttpGet get = new HttpGet(sb.toString());
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-Type", "application/json");
        get.setHeader("client_SDK_Version", "binance_futures-1.0.1-java");
        get.setHeader("Connection", "close");

        try (CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(get)) {
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                System.out.println("err" + httpResponse.getStatusLine());
            } else {
                String resp = EntityUtils.toString(httpResponse.getEntity());
                Datas.writeFile(resp);
                List<Klines> klines = Klines.parseData(resp);
                System.out.println("------");
                System.out.println(klines.size());
            }
        } finally {
            get.reset();
        }

    }





}
