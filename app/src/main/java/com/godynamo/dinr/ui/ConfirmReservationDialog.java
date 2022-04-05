package com.godynamo.dinr.ui;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.controller.ActivityRestaurantDetails;

/**
 * Created by dankovassev on 2015-02-12.
 */
public class ConfirmReservationDialog {

    public ConfirmReservationDialog(final Context context, String restaurantName, String reservationTime, String reservationDetails, String restaurantAddress, String restaunrantPhoneNumber){
        final Dialog dialog = new Dialog(context, R.style.success_dialog);

        dialog.setContentView(R.layout.dialog_reservation);

        TextView titleViewName = (TextView) dialog.findViewById(R.id.restaurant_name);
        titleViewName.setText(restaurantName);

        TextView textViewTime = (TextView) dialog.findViewById(R.id.restaurant_reservation_time);
        textViewTime.setText(reservationTime);

        TextView textViewDetails = (TextView) dialog.findViewById(R.id.restaurant_reservation_details);
        textViewDetails.setText(reservationDetails);

        TextView textViewAddress = (TextView) dialog.findViewById(R.id.restaurant_address);
        textViewAddress.setText(restaurantAddress);

        TextView textViewPhone = (TextView) dialog.findViewById(R.id.restaurant_phone);
        textViewPhone.setText(restaunrantPhoneNumber);

        Button dialogButtonReserve = (Button) dialog.findViewById(R.id.dialogButtonReserve);

        dialogButtonReserve.setOnClickListener(v -> {
            dialog.dismiss();
            ((ActivityRestaurantDetails)context).sendReservation();
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);

        dialogButtonCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
