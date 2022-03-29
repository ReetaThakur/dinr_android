package com.godynamo.dinr.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.controller.ActivityRestaurantDetails;

/**
 * Created by dankovassev on 2015-02-12.
 */
public class ConfirmReservationNumberDialog extends Dialog{


    public ConfirmReservationNumberDialog(Context context) {
        super(context, R.style.number_picker_dialog);

        setContentView(R.layout.dialog_number_picker);
    }

    public void setConfirmReservationNumberDialog(int minPeople, int maxPeople) {



    }



//    public ConfirmReservationNumbersDialog(final Context context, int minPeople, int maxPeople){
//        final Dialog dialog = new Dialog(context, R.style.success_dialog);
//
//        dialog.setContentView(R.layout.dialog_number_picker);
//
//        Button dialogButtonReserve = (Button) dialog.findViewById(R.id.dialogButtonReserve);
//
//        dialogButtonReserve.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                ((ActivityRestaurantDetails)context).sendReservation();
//            }
//        });
//
//        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
//
//        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }
}
