package com.godynamo.dinr.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.params.ClientPNames;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by dankovassev on 14-11-27.
 */
public class DinrClient {
    private static String TAG = "DinrClient";
    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //  client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        Log.e(TAG + " Get ", "url " + url + " params " + params + " client ");
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        // client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        Log.e(TAG + " Post 1 ", "url " + url + " params " + params + " client " );
        client.post(null, url, null, params, "application/json", responseHandler);
    }


    public static void put(String request, HttpEntity entity,
                           JsonHttpResponseHandler responseHandler) {
        // client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        Log.e(TAG + " Put ", "url " + request + " params " + entity + " client ");
        client.put(null, request, entity, "application/json", responseHandler);
    }

    public static void post(String request, HttpEntity entity, JsonHttpResponseHandler responseHandler) {
        //  client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        Log.e(TAG + " Post 2 ", "url " + request + " params " + entity + " client ");
        client.post(null, request, entity, "application/json", responseHandler);
    }

    public static void delete(String request, JsonHttpResponseHandler responseHandler) {
        // client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        Log.e(TAG + " Delete ", "url " + request + " client ");
        client.delete(request, responseHandler);
    }

    public static void patch(String request, HttpEntity entity,
                             JsonHttpResponseHandler responseHandler) {
        //  client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        client.addHeader("X-HTTP-Method-Override", "PATCH");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        Log.e(TAG + " Patch ", "url " + request + " client ");
        client.post(null, request, null, entity, "application/json", responseHandler);
    }

}
