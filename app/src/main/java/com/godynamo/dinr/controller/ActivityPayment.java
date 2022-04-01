package com.godynamo.dinr.controller;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.EndPointUrl;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.PaymentInfo;
import com.godynamo.dinr.tools.ExpiryValidator;
import com.godynamo.dinr.ui.ErrorDialog;
import com.godynamo.dinr.ui.SuccessDialog;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.json.JSONObject;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by dankovassev on 2015-02-05.
 */
public class ActivityPayment extends BaseDinrActivity {

    int MY_SCAN_REQUEST_CODE = 132;
    EditText creditCardEditText;
    EditText cvv;
    EditText expiryDateMonth;
    EditText expiryDateYear;

    EditText phoneNumber;
    JSONObject rootJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        try {
            wrapper.getPaymentInfo(DinrSession.getInstance().getUser(this).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar_back_button);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.custom_action_bar_title);
        textView.setText(this.getResources().getString(R.string.payment_info));

        ImageView backButton = (ImageView) actionBar.getCustomView().findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        creditCardEditText = (EditText) findViewById(R.id.cardNumber);
        cvv = (EditText) findViewById(R.id.cvv);
        expiryDateMonth = (EditText) findViewById(R.id.date_month);
        expiryDateMonth.addTextChangedListener(new ExpiryValidator());
        expiryDateYear = (EditText) findViewById(R.id.date_year);
        expiryDateYear.addTextChangedListener(new ExpiryValidator());
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);

        creditCardEditText.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No need
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // chech that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrecntString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });


        if (DinrSession.getInstance().getPaymentInfo() != null) {
            PaymentInfo pi = DinrSession.getInstance().getPaymentInfo();

            if (pi.getCreditCardNumber() != null) {
                creditCardEditText.setHint("**** **** **** " + pi.getCreditCardNumber());
            }
        }
    }

    public void onScanPress(View v) {

        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: true
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    public void submitPayment(View v) {

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("cc_number", creditCardEditText.getText().toString());

            jsonObject.put("cc_expiry_month", expiryDateMonth.getText().toString());
            jsonObject.put("cc_expiry_year", expiryDateYear.getText().toString());
            jsonObject.put("cvv", cvv.getText().toString());
            jsonObject.put("phone", phoneNumber.getText().toString());

            rootJsonObject = new JSONObject();
            rootJsonObject.put("payment_infos", jsonObject);

            StringEntity entity = new StringEntity(rootJsonObject.toString(), HTTP.UTF_8);
            wrapper.sendPaymentInfo("" + DinrSession.getInstance().getUser(this).getId(), entity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                creditCardEditText.setHint(scanResult.getFormattedCardNumber());

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                    cvv.setText(scanResult.cvv);
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }

            } else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultStr);
        }
        // else handle other activity results
    }

    @Override
    public void onSuccess(JSONObject obj, String event) {
        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_CREATE_UPDATE_USER_PAYMENT)) {

            try {
                wrapper.getPaymentInfo(DinrSession.getInstance().getUser(this).getId());
                new SuccessDialog(this, this.getResources().getString(R.string.we_have_successfully_stored_your_credit_card_number_), "You are set to reserve restaurants now!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (event.equalsIgnoreCase(EndPointUrl.DINR_EVENT_RETRIEVE_USER_PAYMENT)) {

            PaymentInfo pi = DinrSession.getInstance().getPaymentInfo();
            if (pi.getCreditCardNumber() != null) {
                creditCardEditText.setHint("**** **** **** " + pi.getCreditCardNumber());
            }
        }
    }

    @Override
    public void onFailure(JSONObject errorResponse, String event) {

        try {
            new ErrorDialog(this, this.getResources().getString(R.string.error), errorResponse.get("notice").toString());
        } catch (Exception e) {
            new ErrorDialog(this, this.getResources().getString(R.string.error), this.getResources().getString(R.string.looks_like_your_payment_information_is_invalid_));
            e.printStackTrace();
        }

    }
}

