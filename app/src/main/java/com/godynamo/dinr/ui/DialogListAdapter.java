package com.godynamo.dinr.ui;

import android.content.Context;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.model.City;

import java.util.ArrayList;

public class DialogListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<City> items;
    LocationManager locationManager;


    public DialogListAdapter(Context c, ArrayList<City> cities) {
        context = c;
        this.items = cities;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int getCount() {
        if(items != null) {
            return items.size();
        }else{
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.dialog_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) v.findViewById(R.id.name);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        City c = items.get(position);

        holder.name.setText(c.getName_en());

        return v;
    }

    private static class ViewHolder {
        public TextView name;
    }

}