package com.godynamo.dinr.controller;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.APICaller;
import com.godynamo.dinr.api.APIWrapper;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.City;
import com.godynamo.dinr.model.Opening;
import com.godynamo.dinr.model.Restaurant;
import com.godynamo.dinr.ui.ExpandableListAdapter;
import com.godynamo.dinr.ui.LazyAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dankovassev on 15-01-26.
 */
public class FragmentNotifyRestaurant extends Fragment implements APICaller {
    private ListView list;
    private LazyAdapter adapter;
    private APIWrapper wrapper;
    private RelativeLayout rootView;
    private SwipeRefreshLayout restaurantContainer;

    private Context context;

    private LinearLayout message;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();

        if (adapter == null) {
            adapter = new LazyAdapter(getActivity(), null);
        }


        if (wrapper == null) {
            wrapper = new APIWrapper(this, getActivity(), getActivity());
        }

        restaurantContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotifyRestaurants();
            }
        });

        wrapper.getNotifyRestaurants(DinrSession.getInstance().getUser(context));

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (DinrSession.getInstance().getUser(context) != null) {

                    List<Restaurant> restaurants = DinrSession.getInstance().getNotifyRestaurants().getRestaurants();
                    Intent intent = new Intent(getActivity(), ActivityRestaurantDetails.class);

                    intent.putExtra("clickedID", restaurants.get(position).getId());

                    startActivity(intent);

                } else {
                    ((ActivityMain) getActivity()).goToAccount();
                }
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        fetchNotifyRestaurants();
    }

    public void fetchNotifyRestaurants() {
        restaurantContainer.setRefreshing(true);
        wrapper.getNotifyRestaurants(DinrSession.getInstance().getUser(context));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_notify_resturants, container, false);

        restaurantContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

        list = (ListView) rootView.findViewById(R.id.restaurant_list);

        message = (LinearLayout) rootView.findViewById(R.id.notify_message);

        return rootView;
    }

    @Override
    public void onSuccess(JSONObject obj, String event) {

        restaurantContainer.setRefreshing(false);
        updateNotifyRestaurants();
    }


    public void updateNotifyRestaurants() {
        restaurantContainer.setRefreshing(false);

        if (DinrSession.getInstance().getNotifyRestaurants() == null) {
            message.setVisibility(View.VISIBLE);
            return;
        }

        if (DinrSession.getInstance().getNotifyRestaurants().getRestaurants() == null) {
            return;
        }

        List<Restaurant> notifyRestaurants = DinrSession.getInstance().getNotifyRestaurants().getRestaurants();

        if (notifyRestaurants.size() > 0) {
            message.setVisibility(View.GONE);
        } else {
            message.setVisibility(View.VISIBLE);
        }

        adapter.updateData(notifyRestaurants);
        adapter.notifyDataSetChanged();


    }

    public void refreshData() {
        adapter.setUpLocation();
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onFailure(JSONObject errorResponse, String event) {

        updateNotifyRestaurants();

    }
}

