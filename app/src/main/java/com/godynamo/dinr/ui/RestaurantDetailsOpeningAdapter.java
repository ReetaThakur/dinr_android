package com.godynamo.dinr.ui;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.model.Opening;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailsOpeningAdapter extends BaseAdapter {

    private Context context;
    private List<Opening> items;
    private ArrayList<Boolean> results;


    public RestaurantDetailsOpeningAdapter(Context c, List<Opening> o) {
        context = c;
        this.items = o;

        results = new ArrayList<Boolean>();
        for (Opening opening : o) {
            results.add(false);
        }
    }

    public int getClickedID() {
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i) == true) {
                return i;
            }
        }
        return -1;
    }


    public int getClickedOpeniingID() {
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i) == true) {
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

        if (results.get(position)) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < results.size(); i++) {
                    if (i == position) {
                        results.set(i, !results.get(i));
                    } else {
                        results.set(i, false);
                    }

                    notifyDataSetChanged();
                }
            }
        };

        holder.container.setOnClickListener(clickListener);
        holder.radioButton.setOnClickListener(clickListener);
        holder.seatsOpens.setText(o.getTable_detail());

        if(o.getEnd_time() == null){
            holder.restaurantOpening.setText(String.format("%02d", o.getStart_time().getHours()) + ":" + String.format("%02d", o.getStart_time().getMinutes()));
        }else{
            holder.restaurantOpening.setText(String.format("%02d", o.getStart_time().getHours()) + ":" + String.format("%02d", o.getStart_time().getMinutes()) + " to " + String.format("%02d", o.getEnd_time().getHours()) + ":" + String.format("%02d", o.getEnd_time().getMinutes()));
        }

        return v;
    }

    private class ViewHolder {
        public RelativeLayout container;
        public RadioButton radioButton;
        public TextView seatsOpens;
        public TextView restaurantOpening;
    }

}