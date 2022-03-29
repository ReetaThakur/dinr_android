package com.godynamo.dinr.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.controller.ActivityPreviousReservation;

/**
 * Created by dankovassev on 2015-02-12.
 */
public class CancelReservationDialog {

    public CancelReservationDialog(final Activity activity, final int id){
        final Dialog dialog = new Dialog(activity, R.style.success_dialog);

        dialog.setContentView(R.layout.dialog_cancel_reservation);


        Button dialogCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityPreviousReservation)activity).cancelReservation(id);
                dialog.dismiss();
            }
        });

        Button dialogDismiss = (Button) dialog.findViewById(R.id.dialogButtonNo);

        dialogDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }
}
