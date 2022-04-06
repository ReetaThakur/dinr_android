package com.godynamo.dinr.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.controller.ActivityPayment;

/**
 * Created by dankovassev on 2015-02-12.
 */
public class ErrorDialog {

    public ErrorDialog(final Context context, String title, final String errorMessage){
        final Dialog dialog = new Dialog(context, R.style.error_dialog);

        dialog.setContentView(R.layout.dialog_error);

        TextView titleView = (TextView) dialog.findViewById(R.id.title);
        titleView.setText(title);

        TextView textView = (TextView) dialog.findViewById(R.id.text);
        textView.setText(errorMessage);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(v -> {

            if(errorMessage.equalsIgnoreCase("card not valid")){

                Intent intent = new Intent(context, ActivityPayment.class);
                context.startActivity(intent);

            }

            dialog.dismiss();
        });

        dialog.show();

    }
}
