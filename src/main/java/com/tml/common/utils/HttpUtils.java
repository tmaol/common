package com.tml.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.tml.common.entities.ResultCode;
import com.tml.common.exceptions.ForViewException;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet; 
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMethod;

public class HttpUtils {

    public static JSONObject send(String url, Map<String, Object> params, Map<String, String> headers,
            RequestMethod method) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpUriRequest sendReq = null;;
        switch (method) {
            case GET: 
                sendReq = sendGet(url, params,headers);
                break;
            case POST:
                sendReq = sendPost(url, params,headers);
                break;
            case PUT:
                sendReq = sendPut(url, params,headers);
                break;
            default:
                break;
        }
        CloseableHttpResponse execute = client.execute(sendReq);
        String s = EntityUtils.toString(execute.getEntity());
        return JSONObject.parseObject(s);
    }


    private static HttpUriRequest sendPost(String url, Map<String, Object> params, Map<String, String> headers)
            throws IOException {
        HttpPost post = new HttpPost(url);
        Object remove = params.remove("paramsType");
        Integer paramsType = Integer.valueOf(remove.toString());
        HttpEntity urlEncodedFormEntity = setParams(params, paramsType);
        if (headers != null) {
            setHeaders(post, headers);
        } else {
            post.setHeader(HTTP.CONTENT_TYPE, "application/json");
        }
        post.setEntity(urlEncodedFormEntity);
        return post;
    }

    private static void setHeaders(HttpRequestBase req, Map<String, String> headers) {
        headers.forEach((key, value) -> {
            req.setHeader(new BasicHeader(key, value));
        });
    }

    private static HttpEntity setUrlFormParams(Map<String, Object> params) throws IOException {
        List<NameValuePair> param = new ArrayList<>();
        params.forEach((key, value) -> {
            param.add(new BasicNameValuePair(key.toString(), value.toString()));
        });
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param);
        return urlEncodedFormEntity;
    }

    /**
     * type 0： UrlEncodedFormEntity 1：StringEntity 2:InputStreamEntity
     */
    private static HttpEntity setParams(Map<String, Object> params, int type) throws IOException {
        switch (type) {
            case 0:
                return setUrlFormParams(params);
            case 1:
                return setJsonParams(params);
            case 2:
                return setInputStreamParams(params);
            default:
                return null;
        }
    }

    private static HttpEntity setInputStreamParams(Map<String, Object> params) {
        int size = params.size();
        if (size != 1) {
            throw ForViewException.getInstance(ResultCode.PARAM_EOOR);
        }
        List<InputStreamEntity> entity = new ArrayList<InputStreamEntity>();
        params.forEach((key, value) -> {
            if (value instanceof InputStream) {
                InputStreamEntity inputStreamEntity = new InputStreamEntity((InputStream) value);
                entity.add(inputStreamEntity);
            }
        });
        return entity.get(0);
    }

    private static HttpEntity setJsonParams(Map<String, Object> params) throws IOException {
        StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(params), "utf-8");
        stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        return stringEntity;
    }

    

    private static HttpUriRequest sendPut(String url, Map<String, Object> params, Map<String, String> headers)
            throws IOException {
        HttpPut put= new HttpPut(url);
        Object remove = params.remove("paramsType");
        Integer paramsType = Integer.valueOf(remove.toString());
        HttpEntity setParams = setParams(params, paramsType);
        if(headers!=null) {
            setHeaders(put, headers);
        }else {
            put.setHeader(HTTP.CONTENT_TYPE, "application/json");
        }
        put.setEntity(setParams);
        return put;
    }

    private static HttpUriRequest sendGet(String url, Map<String, Object> params,
            Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();
        params.remove("paramsType");
        params.forEach((k, v) -> {
            sb.append("&" + k + "=" + v);
        });
        url = url + sb.toString().replaceFirst("&", "?");
        HttpGet get = new HttpGet(url);
        if(headers!=null) {
            setHeaders(get, headers);
        }else {
            get.setHeader(HTTP.CONTENT_TYPE, "application/json");
        }
        return get;
    }
}
