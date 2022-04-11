package com.godynamo.dinr.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.godynamo.dinr.R;

/**
 * Created by dankovassev on 2015-02-12.
 */
public class ErrorFinishDialog {

    public ErrorFinishDialog(final Context context, String title, String errorMessage){
        final Dialog dialog = new Dialog(context, R.style.error_dialog);

        dialog.setContentView(R.layout.dialog_error);
        dialog.setCancelable(false);
        TextView titleView = (TextView) dialog.findViewById(R.id.title);
        titleView.setText(title);

        TextView textView = (TextView) dialog.findViewById(R.id.text);
        textView.setText(errorMessage);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(v -> {
            dialog.dismiss();
            ((Activity)context).finish();
        });
        dialog.show();
    }
}
