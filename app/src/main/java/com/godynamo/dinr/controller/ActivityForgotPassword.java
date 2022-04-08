package com.godynamo.dinr.controller;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.godynamo.dinr.R;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.ui.ErrorDialog;
import com.godynamo.dinr.ui.SuccessDialog;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.json.JSONObject;

public class ActivityForgotPassword extends BaseDinrActivity {

    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_resset);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar_back_button);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.custom_action_bar_title);
        textView.setText(this.getString(R.string.forgot_password));

        ImageView backButton = (ImageView) actionBar.getCustomView().findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/MarkSmallCaps-Regular.otf");


        email = (EditText) findViewById(R.id.email);
        email.setTypeface(myTypeface);

    }

    //On Click Function declared in the XML resource
    public void resetPassword(View view){

        System.out.println("vvv RESET click!");

        try {

            if(email.getText().toString().equalsIgnoreCase("")) {
                YoYo.with(Techniques.Shake).playOn(email);
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                YoYo.with(Techniques.Shake).playOn(email);
                email.setError(getString(R.string.invalid_email));
                return;
            }

            JSONObject emailJson = new JSONObject();

            emailJson.put("email", email.getText().toString());

//            JSONObject user = new JSONObject();
//            user.put("user", jsonObject);

            StringEntity entity = new StringEntity(emailJson.toString(), HTTP.UTF_8);
            RequestParams params = new RequestParams();
            params.put("email", email.getText().toString());

            wrapper.resetPassword(entity);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject obj, String event) {
        super.onSuccess(obj, event);

        if(EndPointUrl.DINR_EVENT_PASSWORD_RESET.equalsIgnoreCase(event)) {
            new SuccessDialog(this, getString(R.string.forgot_password), getString(R.string.password_reset_success));
        }
    }

    @Override
    public void onFailure(JSONObject errorResponse, String event) {

        try {
            new ErrorDialog(this, "Error", errorResponse.getString("error"));
        }catch (Exception e){
            new ErrorDialog(this, "Error", "Something went wrong, unable to send request! ");
            e.printStackTrace();
        }

    }


}



