package com.yahelei.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpsClientUtil {
        public static String doPost(String url,String body,Map<String,String> headers){
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new sslzlr();
            httpPost = new HttpPost(url);

            StringEntity entity = new StringEntity(body);
            if(headers!=null&&headers.size()>0) {
                for (String x : headers.keySet()) {
                    httpPost.setHeader(x, headers.get(x));
                }
            }
//            entity.setContentType("application/json;charset=utf-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity);
                }
                Header[] x = response.getAllHeaders();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    public static String sendhttps(String url, String paramerString, Map headerMap, String method) {
        String paramerString2="";
        try {
            paramerString2=new String(paramerString.getBytes("UTF-8"),"ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        switch (method.toUpperCase()) {
            case "POST":
                return doPost(url, paramerString2, headerMap);
            case "GET":
                return doGet(url+"?"+paramerString2, headerMap);
            case "PUT":
                return doPut(url, headerMap,paramerString2);
            case "DELETE":
                return doDelete(url, headerMap,paramerString2);
            default:
                return null;
        }
    }
    public static String doDelete(String url, Map<String,String> headers,String body) {
        sslzlr httpClient = null;
        try {
            httpClient = new sslzlr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpDelete.setConfig(requestConfig);
        httpDelete.setHeader("DataEncoding", "UTF-8");
        if(headers!=null&&headers.size()>0) {
            for (String x : headers.keySet()) {
                httpDelete.setHeader(x, headers.get(x));
            }
        }
        CloseableHttpResponse httpResponse = null;
        try {
            StringEntity entity2 = new StringEntity(body);
            httpDelete.setEntity(entity2);
            httpResponse = httpClient.execute(httpDelete);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static String doPut(String url, Map<String,String> headers, String jsonStr) {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = new sslzlr();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpPut.setConfig(requestConfig);
        httpPut.setHeader("DataEncoding", "UTF-8");
        if(headers!=null&&headers.size()>0) {
            for (String x : headers.keySet()) {
                httpPut.setHeader(x, headers.get(x));
            }
        }
        CloseableHttpResponse httpResponse = null;
        try {
            httpPut.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPut);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static String doGet(String url,Map<String, String> headers){
        String response = null;
        try{
            DefaultHttpClient client = new sslzlr();

            URIBuilder builder = new URIBuilder(url);
            HttpGet request = new HttpGet(builder.build());
            if(headers!=null&&headers.size()>0) {
                for (String x : headers.keySet()) {
                    request.setHeader(x, headers.get(x));
                }
            }
//            request.setHeader("ContentTye","application/json");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(6000)
                    .setConnectTimeout(6000)
                    .setConnectionRequestTimeout(6000).build();
            request.setConfig(requestConfig);


            HttpResponse res = client.execute(request);
            HttpEntity entity = res.getEntity();
            response = EntityUtils.toString(res.getEntity());// 返回json格式：
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}