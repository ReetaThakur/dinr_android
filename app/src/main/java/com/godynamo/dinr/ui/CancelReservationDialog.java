package com.godynamo.dinr.ui;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;

import com.godynamo.dinr.R;
import com.godynamo.dinr.controller.ActivityPreviousReservation;

/**
 * Created by dankovassev on 2015-02-12.
 */
public class CancelReservationDialog {

    public CancelReservationDialog(final Activity activity, final int id) {
        final Dialog dialog = new Dialog(activity, R.style.success_dialog);

        dialog.setContentView(R.layout.dialog_cancel_reservation);


        Button dialogCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);

        dialogCancel.setOnClickListener(v -> {
            ((ActivityPreviousReservation) activity).cancelReservation(id);
            dialog.dismiss();
        });

        Button dialogDismiss = (Button) dialog.findViewById(R.id.dialogButtonNo);

        dialogDismiss.setOnClickListener(v -> dialog.dismiss());


        dialog.show();
    }
}
