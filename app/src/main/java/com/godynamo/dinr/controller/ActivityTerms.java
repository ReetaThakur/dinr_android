package com.godynamo.dinr.controller;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.tools.Utils;

/**
 * Created by dankovassev on 2015-02-05.
 */
public class ActivityTerms extends BaseDinrActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_servicy);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar_back_button);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        TextView textView = (TextView)actionBar.getCustomView().findViewById(R.id.custom_action_bar_title);
        textView.setText(this.getString(R.string.terms_of_service));

        ImageView backButton = (ImageView)actionBar.getCustomView().findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebView webview = (WebView) findViewById(R.id.term_webview);
        webview.setBackgroundColor(getResources().getColor(R.color.black));
        webview.loadUrl(Utils.returnLocalizedUrl(EndPointUrl.DINR_TERMS_URL));

    }

}
