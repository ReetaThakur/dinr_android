package com.godynamo.dinr.controller;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.tools.Utils;

public class ActivityTerms extends BaseDinrActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_servicy);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar_back_button);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        TextView textView = actionBar.getCustomView().findViewById(R.id.custom_action_bar_title);
        textView.setText(this.getString(R.string.terms_of_service));

        ImageView backButton = actionBar.getCustomView().findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        WebView webview = findViewById(R.id.term_webview);
        webview.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        webview.loadUrl(Utils.returnLocalizedUrl(EndPointUrl.DINR_TERMS_URL));

    }

}
