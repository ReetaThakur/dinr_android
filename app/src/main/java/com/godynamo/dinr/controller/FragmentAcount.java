package com.godynamo.dinr.controller;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
/*import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;*/
import com.godynamo.dinr.R;
import com.godynamo.dinr.api.APICaller;
import com.godynamo.dinr.api.APIWrapper;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.ui.ErrorDialog;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by dankovassev on 15-01-26.
 */
public class FragmentAcount extends Fragment implements APICaller {

    public Button signUpButton;
    public Button logInFaceBook;
    public Button logInEmail;
    public LinearLayout loginChooserContainer;
    public LinearLayout emailSignInContainer;
    public EditText emailEditText;
    public EditText passwordField;
    public Button singInEmailButton;
    public TextView forgotPassword;

    public Button previousReservationButton;
    public Button paymentButton;
    public Button termOrServiceButton;
    public Button privacyPoliceButton;
    public Button aboutFaqButton;
    public Button rateAppButton;

    public TextView nameGreetingTextView;
    public ImageView userPicture;

    public ScrollView loggedProfileContainer;

    private APIWrapper wrapper;

   // private UiLifecycleHelper uiHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        wrapper = new APIWrapper(this, getActivity(), getActivity());

        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MarkBook.otf");

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == signUpButton) {

                    Intent intent = new Intent(getActivity(), ActivitySignUp.class);
                    startActivity(intent);

                } else if (v == logInFaceBook) {


                } else if (v == logInEmail) {

                    loginChooserContainer.setVisibility(View.INVISIBLE);
                    emailSignInContainer.setVisibility(View.VISIBLE);

                } else if (v == singInEmailButton) {

                    try {

                        if (emailEditText.getText().toString().equalsIgnoreCase("")) {
                            YoYo.with(Techniques.Shake).playOn(emailEditText);
                            return;
                        }

                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                            YoYo.with(Techniques.Shake).playOn(emailEditText);
                            emailEditText.setError(getString(R.string.invalid_email));
                            return;
                        }

                        if (passwordField.getText().toString().equalsIgnoreCase("")) {
                            YoYo.with(Techniques.Shake).playOn(passwordField);
                            return;
                        }


                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("email", emailEditText.getText().toString());
                        jsonObject.put("password", passwordField.getText().toString());

                        StringEntity entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
                        wrapper.signInWithEmail(entity);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } else if (v == previousReservationButton) {

                    Intent intent = new Intent(getActivity(), ActivityPreviousReservation.class);
                    startActivity(intent);

                } else if (v == paymentButton) {

                    Intent intent = new Intent(getActivity(), ActivityPayment.class);
                    startActivity(intent);

                } else if (v == termOrServiceButton) {

                    Intent intent = new Intent(getActivity(), ActivityTerms.class);
                    startActivity(intent);

                } else if (v == privacyPoliceButton) {

                    Intent intent = new Intent(getActivity(), ActivityPrivacy.class);
                    startActivity(intent);

                } else if (v == aboutFaqButton) {

                    Intent intent = new Intent(getActivity(), ActivityFaq.class);
                    startActivity(intent);

                } else if (v == rateAppButton) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.godynamo.dinr")));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else if(v == forgotPassword){

                    Intent intent = new Intent(getActivity(), ActivityForgotPassword.class);
                    startActivity(intent);

                }
            }
        };

      //  uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
     //   uiHelper.onCreate(savedInstanceState);

        //Login Chooser
        signUpButton = (Button) rootView.findViewById(R.id.btn_sign_up_login);
        signUpButton.setOnClickListener(onClickListener);
        signUpButton.setTypeface(myTypeface);

        logInFaceBook = (Button) rootView.findViewById(R.id.btn_facebook);
        logInFaceBook.setTypeface(myTypeface);
        logInFaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityMainNew) getActivity()).LoginFacebook();
            }
        });
        // start Facebook Login


        logInEmail = (Button) rootView.findViewById(R.id.btn_sign_in_email);
        logInEmail.setOnClickListener(onClickListener);
        logInEmail.setTypeface(myTypeface);


        loginChooserContainer = (LinearLayout) rootView.findViewById(R.id.choose_facebook_email_container);

        //login Section
        emailSignInContainer = (LinearLayout) rootView.findViewById(R.id.email_login_container);

        emailEditText = (EditText) rootView.findViewById(R.id.email_edit_text);
        emailEditText.setTypeface(myTypeface);

        passwordField = (EditText) rootView.findViewById(R.id.password_edit_text);
        passwordField.setTypeface(myTypeface);


        singInEmailButton = (Button) rootView.findViewById(R.id.sign_in_button);
        singInEmailButton.setOnClickListener(onClickListener);
        singInEmailButton.setTypeface(myTypeface);

        forgotPassword = (TextView) rootView.findViewById(R.id.reset_password);
        forgotPassword.setOnClickListener(onClickListener);

        //Logged Section
        previousReservationButton = (Button) rootView.findViewById(R.id.tonight_reservation_button);
        previousReservationButton.setOnClickListener(onClickListener);
        previousReservationButton.setTypeface(myTypeface);


        paymentButton = (Button) rootView.findViewById(R.id.payment_button);
        paymentButton.setOnClickListener(onClickListener);
        paymentButton.setTypeface(myTypeface);


        termOrServiceButton = (Button) rootView.findViewById(R.id.term_of_service_button);
        termOrServiceButton.setOnClickListener(onClickListener);
        termOrServiceButton.setTypeface(myTypeface);


        privacyPoliceButton = (Button) rootView.findViewById(R.id.privacy_policy_button);
        privacyPoliceButton.setOnClickListener(onClickListener);
        privacyPoliceButton.setTypeface(myTypeface);


        aboutFaqButton = (Button) rootView.findViewById(R.id.about_faq_button);
        aboutFaqButton.setOnClickListener(onClickListener);
        aboutFaqButton.setTypeface(myTypeface);


        rateAppButton = (Button) rootView.findViewById(R.id.rate_app_button);
        rateAppButton.setOnClickListener(onClickListener);
        rateAppButton.setTypeface(myTypeface);


        nameGreetingTextView = (TextView) rootView.findViewById(R.id.name_greeting);
        nameGreetingTextView.setTypeface(myTypeface);

        userPicture = (ImageView) rootView.findViewById(R.id.user_image);


        loggedProfileContainer = (ScrollView) rootView.findViewById(R.id.logged_profile_container);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (DinrSession.getInstance().getUser(getContext()) != null) {
            nameGreetingTextView.setText(this.getString(R.string.hello) + DinrSession.getInstance().getUser(getContext()).getFirstName().toUpperCase() + "!");
            Glide.with(getActivity()).load(DinrSession.getInstance().getUser(getContext()).getPhotoUrl()).into(userPicture);

            loggedProfileContainer.setVisibility(View.VISIBLE);
            emailSignInContainer.setVisibility(View.INVISIBLE);
            loginChooserContainer.setVisibility(View.INVISIBLE);
        } else {

            loggedProfileContainer.setVisibility(View.INVISIBLE);
            emailSignInContainer.setVisibility(View.INVISIBLE);
            loginChooserContainer.setVisibility(View.VISIBLE);

        }
    }

    public void facebookLoggedIn(String token) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("fb_auth_token", token);

            StringEntity entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
            wrapper.signUpWithFacebook(entity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {

            if (state.isOpened()) {
            } else if (state.isClosed()) {
            }
        }
    };*/

    @Override
    public void onSuccess(JSONObject obj, String event) {
        if (EndPointUrl.DINR_EVENT_AUTHENTICATE_USER_EMAIL.equalsIgnoreCase(event)) {

            loggedProfileContainer.setVisibility(View.VISIBLE);
            emailSignInContainer.setVisibility(View.INVISIBLE);
            loginChooserContainer.setVisibility(View.INVISIBLE);

            nameGreetingTextView.setText(this.getString(R.string.hello) + DinrSession.getInstance().getUser(getContext()).getFirstName().toUpperCase() + "!");
            Glide.with(getActivity()).load(DinrSession.getInstance().getUser(getContext()).getPhotoUrl()).into(userPicture);

            getActivity().getSharedPreferences(DinrSession.PREFS_NAME, 0).edit().putInt(DinrSession.USER_ID_PREF_NAME, DinrSession.getInstance().getUser(getContext()).getId()).commit();

            try {
                wrapper.getPaymentInfo(DinrSession.getInstance().getUser(getContext()).getId());
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        if (EndPointUrl.DINR_EVENT_AUTHENTICATE_USER_FACEBOOK.equalsIgnoreCase(event)) {

            loggedProfileContainer.setVisibility(View.VISIBLE);
            emailSignInContainer.setVisibility(View.INVISIBLE);
            loginChooserContainer.setVisibility(View.INVISIBLE);

            nameGreetingTextView.setText(this.getString(R.string.hello) + DinrSession.getInstance().getUser(getContext()).getFirstName().toUpperCase() + "!");
            Glide.with(getActivity()).load(DinrSession.getInstance().getUser(getContext()).getPhotoUrl()).into(userPicture);

            getActivity().getSharedPreferences(DinrSession.PREFS_NAME, 0).edit().putInt(DinrSession.USER_ID_PREF_NAME, DinrSession.getInstance().getUser(getContext()).getId()).commit();

        }
    }

    @Override
    public void onFailure(JSONObject errorResponse, String event) {

        if (!errorResponse.toString().equalsIgnoreCase("{}")) {
            new ErrorDialog(getActivity(), "Error", errorResponse.toString());
        } else {
            new ErrorDialog(getActivity(), "Error", "Unknown username or password   ");
        }
    }


    public void OnBackPressed() {

        if (loginChooserContainer.getVisibility() == View.VISIBLE) {
            getActivity().finish();
            return;
        }

        if(loggedProfileContainer.getVisibility() == View.VISIBLE) {
            getActivity().finish();
            return;
        }

        loginChooserContainer.setVisibility(View.VISIBLE);
        emailSignInContainer.setVisibility(View.INVISIBLE);
    }


}