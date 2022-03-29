package com.godynamo.dinr.api;

import org.json.JSONObject;

/**
 * Created by dankovassev on 14-11-27.
 */
public interface APICaller {

    void onSuccess(JSONObject obj, String event);
    void onFailure(JSONObject errorResponse, String event);

}
