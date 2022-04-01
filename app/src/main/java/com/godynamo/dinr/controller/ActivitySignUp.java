package com.godynamo.dinr.controller;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.godynamo.dinr.R;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.ui.ErrorDialog;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.json.JSONObject;


/**
 * Created by dankovassev on 15-01-31.
 */
public class ActivitySignUp extends BaseDinrActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private EditText password;
    private EditText passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar_back_button);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.custom_action_bar_title);
        textView.setText(this.getString(R.string.sign_up));

        ImageView backButton = (ImageView) actionBar.getCustomView().findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/MarkSmallCaps-Regular.otf");



        firstName = (EditText) findViewById(R.id.first_name);
        firstName.setTypeface(myTypeface);

        lastName = (EditText) findViewById(R.id.last_name);
        lastName.setTypeface(myTypeface);

        email = (EditText) findViewById(R.id.email);
        email.setTypeface(myTypeface);

        phone = (EditText) findViewById(R.id.phone_number);
        phone.setTypeface(myTypeface);

        password = (EditText) findViewById(R.id.password);
        password.setTypeface(myTypeface);

        passwordConfirm = (EditText) findViewById(R.id.password_confirmation);
        passwordConfirm.setTypeface(myTypeface);

    }

    //On Click Function declared in the XML resource
    public void singnUpOnClick(View view){

        try {

            if(firstName.getText().toString().equalsIgnoreCase("")) {
                firstName.forceLayout();
                YoYo.with(Techniques.Shake).playOn(firstName);
                return;
            }

            if(lastName.getText().toString().equalsIgnoreCase("")) {
                YoYo.with(Techniques.Shake).playOn(lastName);
                return;
            }

            if(email.getText().toString().equalsIgnoreCase("")) {
                YoYo.with(Techniques.Shake).playOn(email);
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                YoYo.with(Techniques.Shake).playOn(email);
                email.setError(getString(R.string.invalid_email));
                return;
            }

            if(phone.getText().toString().equalsIgnoreCase("")) {
                YoYo.with(Techniques.Shake).playOn(phone);
                return;
            }

            if(password.getText().toString().equalsIgnoreCase("")) {
                YoYo.with(Techniques.Shake).playOn(password);
                return;
            }

            if(passwordConfirm.getText().toString().equalsIgnoreCase("")) {
                YoYo.with(Techniques.Shake).playOn(passwordConfirm);
                return;
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("first_name", firstName.getText().toString());
            jsonObject.put("last_name", lastName.getText().toString());
            jsonObject.put("email", email.getText().toString());
            jsonObject.put("phone", phone.getText().toString());
            jsonObject.put("password", password.getText().toString());
            jsonObject.put("password_confirmation", passwordConfirm.getText().toString());

            JSONObject user = new JSONObject();
            user.put("user", jsonObject);

            StringEntity entity = new StringEntity(user.toString(), HTTP.UTF_8);
            wrapper.signUp(entity);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onSuccess(JSONObject obj, String event) {
        super.onSuccess(obj, event);

        if(EndPointUrl.DINR_EVENT_CREATE_USER.equalsIgnoreCase(event)) {

            getSharedPreferences(DinrSession.PREFS_NAME, 0).edit().putInt(DinrSession.USER_ID_PREF_NAME, DinrSession.getInstance().getUser(this).getId()).commit();

            finish();

        }

    }

    @Override
    public void onFailure(JSONObject errorResponse, String event) {

        try {
            new ErrorDialog(this, "Error", errorResponse.getString("error"));
        }catch (Exception e){
            new ErrorDialog(this, "Error", "Something went wrong, unable to create user. ");
            e.printStackTrace();
        }

    }


}



