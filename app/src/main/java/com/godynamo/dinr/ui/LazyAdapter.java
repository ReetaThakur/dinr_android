package com.godynamo.dinr.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.godynamo.dinr.R;
import com.godynamo.dinr.model.Opening;
import com.godynamo.dinr.model.Restaurant;

import java.util.List;

public class LazyAdapter extends BaseAdapter {

    private Context context;
    private List<Restaurant> items;
    private Location location;
    LocationManager locationManager;


    public LazyAdapter(Context c, List<Restaurant> r) {
        context = c;
        this.items = r;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        setUpLocation();

    }

    public void setUpLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        }
    }

    public void updateData(List<Restaurant> r) {
        this.items = r;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        View v = convertView;

        if (v == null) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.restaurant_raw, null);
            holder = new ViewHolder();

            holder.name = (TextView) v.findViewById(R.id.restaurant_name);
            holder.distance = (TextView) v.findViewById(R.id.restaurant_distance);
            holder.backgroundPicture = (ImageView) v.findViewById(R.id.background_picture);
            holder.restaurantOpenings = (TextView) v.findViewById(R.id.restaurant_openings);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Restaurant r = items.get(position);

        holder.name.setText(r.getName());
        Glide.with(context).load(r.getMainPhoto()).into(holder.backgroundPicture);

        String hours = "";
        for (Opening opening : r.getOpenings()) {
            if (opening.getState().equalsIgnoreCase("free")) {
                hours = hours + String.format("%02d", opening.getStart_time().getHours()) + ":" + String.format("%02d", opening.getStart_time().getMinutes()) + "/";
            }
        }
        if (hours.length() > 0) {
            hours = hours.substring(0, hours.length() - 1);

            holder.name.setAlpha(1f);
            holder.distance.setAlpha(1f);
            holder.backgroundPicture.setAlpha(1f);
            holder.restaurantOpenings.setAlpha(1f);

        } else if (r.getStatus() != null && r.getStatus().equalsIgnoreCase(Restaurant.STATUS_OPEN)) {

            hours = context.getResources().getString(R.string.check_back_later);

            holder.name.setAlpha(0.6f);
            holder.distance.setAlpha(0.6f);
            holder.backgroundPicture.setAlpha(0.6f);
            holder.restaurantOpenings.setAlpha(0.6f);

        } else if (r.getStatus() != null && r.getStatus().equalsIgnoreCase(Restaurant.STATUS_CLOSED)) {
            hours = context.getResources().getString(R.string.closed);

            holder.name.setAlpha(0.2f);
            holder.distance.setAlpha(0.2f);
            holder.backgroundPicture.setAlpha(0.2f);
            holder.restaurantOpenings.setAlpha(0.2f);
        }

        holder.restaurantOpenings.setText(hours);

        Location locationRestaurant = new Location("");
        locationRestaurant.setLatitude(Double.valueOf(r.getLatitude()));
        locationRestaurant.setLongitude(Double.valueOf(r.getLongitude()));

        if (location != null) {
            float distance = location.distanceTo(locationRestaurant);

            holder.distance.setText("" + String.format("%.2f", distance / 1000) + " km");
        } else {
            holder.distance.setText("");
        }

        return v;
    }

    private class ViewHolder {
        public TextView name;
        public ImageView backgroundPicture;
        public TextView distance;
        public TextView restaurantOpenings;

    }

}