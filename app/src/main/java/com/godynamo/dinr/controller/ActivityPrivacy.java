package com.godynamo.dinr.controller;

import android.app.ActionBar;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.tools.Utils;

public class ActivityPrivacy extends BaseDinrActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar_back_button);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        TextView textView = actionBar.getCustomView().findViewById(R.id.custom_action_bar_title);
        textView.setText(this.getString(R.string.privacy_policy));

        ImageView backButton = actionBar.getCustomView().findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        WebView webview = findViewById(R.id.privacy_webview);
        webview.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        webview.loadUrl(Utils.returnLocalizedUrl(EndPointUrl.DINR_PRIVACY_URL));

    }


}
