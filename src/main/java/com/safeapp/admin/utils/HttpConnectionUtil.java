package com.safeapp.admin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.bind.annotation.RequestMethod;

import net.minidev.json.JSONObject;

public class HttpConnectionUtil {

    private static String sendHttp(Map<String, String> headerMap, String targetUrl, Map<String, Object> bodyMap,
        RequestMethod methodType) {
        URLConnection urlConnection;
        StringBuilder stb = new StringBuilder();
        boolean isSSL = false;
        try {
            URL url = new URL(targetUrl);
            if ("https".contains(targetUrl))
                isSSL = true;

            urlConnection = (isSSL ? (HttpsURLConnection)url.openConnection()
                : (HttpURLConnection)url.openConnection());

            for (String key : headerMap.keySet())
                urlConnection.setRequestProperty(key, headerMap.get(key));

            OutputStream outputStream;
            //            if(RequestMethod.POST.equals(methodType))
            {
                if (isSSL)
                    ((HttpsURLConnection)urlConnection).setRequestMethod(methodType.name());
                else
                    ((HttpURLConnection)urlConnection).setRequestMethod(methodType.name());
                outputStream = (isSSL ? (HttpsURLConnection)urlConnection : (HttpURLConnection)urlConnection)
                    .getOutputStream();
                outputStream.write(JSONObject.toJSONString(bodyMap).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
            }
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null)
                stb.append(line);

            reader.close();
            if (isSSL)
                ((HttpsURLConnection)urlConnection).disconnect();
            else
                ((HttpURLConnection)urlConnection).disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stb.toString();
    }

    public static Map<String, Object> sendHttp(String url, RequestMethod method, Map<String, String> headerMap,
        Map<String, Object> bodyMap) {
        return new GsonJsonParser().parseMap(sendHttp(headerMap, url, bodyMap, method));
    }

    public static Map<String, Object> sendPostByForm(String url, Map<String, String> headerMap,
        Map<String, Object> bodyMap) {

        headerMap.put("Accept-Charset", "UTF-8");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");

        return new GsonJsonParser().parseMap(sendHttp(headerMap, url, bodyMap, RequestMethod.POST));
    }
}
