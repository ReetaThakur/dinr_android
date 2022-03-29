package com.godynamo.dinr.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.godynamo.dinr.R;

/**
 * Created by dankovassev on 2015-02-12.
 */
public class SuccessDialog {

    public SuccessDialog(final Context context, String title, String message){
        final Dialog dialog = new Dialog(context, R.style.success_dialog);

        dialog.setContentView(R.layout.dialog_success);

        TextView titleView = (TextView) dialog.findViewById(R.id.title);
        titleView.setText(title);

        TextView textView = (TextView) dialog.findViewById(R.id.text);
        textView.setText(message);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity)context).finish();
            }
        });

        dialog.show();
    }
}
