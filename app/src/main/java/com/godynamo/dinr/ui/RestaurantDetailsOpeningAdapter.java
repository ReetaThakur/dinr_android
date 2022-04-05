package com.godynamo.dinr.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.model.Opening;
import com.godynamo.dinr.tools.Utils;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailsOpeningAdapter extends BaseAdapter {

    private final Context context;
    private final List<Opening> items;
    private final ArrayList<Boolean> results;


    public RestaurantDetailsOpeningAdapter(Context c, List<Opening> o) {
        context = c;
        this.items = o;

        results = new ArrayList<>();
        for (Opening opening : o) {
            results.add(false);
        }
    }

    public int getClickedID() {
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i)) {
                return i;
            }
        }
        return -1;
    }


    public int getClickedOpeniingID() {
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i)) {
                return items.get(i).getId();
            }
        }
        return -1;
    }
    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        View v = convertView;

        if (v == null) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.restaurant_detail_hour_item, null);
            holder = new ViewHolder();

            holder.radioButton = (RadioButton) v.findViewById(R.id.radioButton);
            holder.seatsOpens = (TextView) v.findViewById(R.id.seats_open);
            holder.restaurantOpening = (TextView) v.findViewById(R.id.restaurant_opening);
            holder.container = (RelativeLayout) v.findViewById(R.id.opening_container);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Opening o = items.get(position);

        holder.radioButton.setChecked(results.get(position));

        View.OnClickListener clickListener = v1 -> {

            for (int i = 0; i < results.size(); i++) {
                if (i == position) {
                    results.set(i, !results.get(i));
                } else {
                    results.set(i, false);
                }

                notifyDataSetChanged();
            }
        };

        holder.container.setOnClickListener(clickListener);
        holder.radioButton.setOnClickListener(clickListener);
        holder.seatsOpens.setText(o.getTable_detail());

        String startTime = null, endTime = null;
        if (o.getStart_time() != null)
            startTime = Utils.getTimeFromDate(o.getStart_time());

        if (o.getEnd_time() != null)
            endTime = Utils.getTimeFromDate(o.getEnd_time());

        if (startTime != null && endTime != null)
            holder.restaurantOpening.setText(startTime + " to " + endTime);
        else if (startTime != null)
            holder.restaurantOpening.setText(startTime);


        return v;
    }

    private static class ViewHolder {
        public RelativeLayout container;
        public RadioButton radioButton;
        public TextView seatsOpens;
        public TextView restaurantOpening;
    }

}