package com.godynamo.dinr.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;

/**
 * Created by dankovassev on 14-11-27.
 */
public class DinrClient {


    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        client.post(null, url, null, params, "application/json", responseHandler);
    }


    public static void put(String request, StringEntity entity,
                           JsonHttpResponseHandler responseHandler) {
        client.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        client.put(null,request,entity, "application/json",responseHandler);
    }

    public static void post(String request, StringEntity entity, JsonHttpResponseHandler responseHandler) {
        client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        client.post(null,request, entity, "application/json",responseHandler);

    }

    public static void delete(String request, JsonHttpResponseHandler responseHandler) {
        client.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        client.removeHeader("X-HTTP-Method-Override");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");
        client.delete(request, responseHandler);
    }

    public static void patch(String request, StringEntity entity,
                             JsonHttpResponseHandler responseHandler) {
        client.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        client.addHeader("X-HTTP-Method-Override", "PATCH");
        client.setUserAgent("android-async-http/1.4.4 (http://loopj.com/android-async-http)");

        client.post(null, request, null, entity, "application/json",
                responseHandler);
    }

}
