package com.godynamo.dinr.controller;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.util.Pools;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.godynamo.dinr.R;
import com.godynamo.dinr.api.APICaller;
import com.godynamo.dinr.api.APIWrapper;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.City;
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
public class FragmentRestaurant extends Fragment implements APICaller {
    private ListView list;
    private LazyAdapter adapter;
    private APIWrapper wrapper;
    private RelativeLayout rootView;
    private SwipeRefreshLayout restaurantContainer;


    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private List<String> listDataHeader;
    private HashMap<String, List<City>> listDataChild;

    private Context context;


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
            boolean addRestaurant = false;

            List<Restaurant> restaurants = DinrSession.getInstance().getRestaurants();

            if (restaurants == null) {
                Intent intentSplash = new Intent(getActivity(), ActivitySplash.class);
                startActivity(intentSplash);
                getActivity().finish();
            }

            adapter = new LazyAdapter(getActivity(), restaurants);
        }


        if (wrapper == null) {
            wrapper = new APIWrapper(this, getActivity(), getActivity());
        }

        restaurantContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchRestaurants();
            }
        });


        expListView = (ExpandableListView) rootView.findViewById(R.id.expand_list);
        listDataHeader = new ArrayList<String>();
        if (DinrSession.getInstance().getSelectedCity() != null) {
            listDataHeader.add(DinrSession.getInstance().getSelectedCity().getName_en());
        } else {
            listDataHeader.add("Cities");
        }

        listDataChild = new HashMap<String, List<City>>();
        listDataChild.put(listDataHeader.get(0), DinrSession.getInstance().getCities(context).getCities());

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);

        //Handling the Taxonomy menu clicks
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Making sure that the home option is directed correctly

                DinrSession.getInstance().setSelectedCity((City) listAdapter.getChild(groupPosition, childPosition));
                listAdapter.notifyDataSetChanged();
                expListView.collapseGroup(0);

                fetchRestaurants();

                return false;
            }
        });


        wrapper.Restaurants();

        //    setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (DinrSession.getInstance().getUser(getContext()) != null) {

                    List<Restaurant> restaurants = DinrSession.getInstance().getRestaurants();
                    Intent intent = new Intent(getActivity(), ActivityRestaurantDetails.class);
                    intent.putExtra("clickedID", restaurants.get(position).getId());
                    startActivity(intent);

                } else {
                    ((ActivityMainNew) getActivity()).goToAccount();
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

        fetchRestaurants();
    }

    public void fetchRestaurants() {
        restaurantContainer.setRefreshing(true);
        wrapper.Restaurants();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_resturants, container, false);

        restaurantContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

        list = (ListView) rootView.findViewById(R.id.restaurant_list);

        return rootView;
    }

    @Override
    public void onSuccess(JSONObject obj, String event) {

        restaurantContainer.setRefreshing(false);

        updateRestaurants();

        if (getActivity() != null) {
            ((ActivityMainNew) getActivity()).updateMap();
        }
    }

    public void updateRestaurants() {

        List<Restaurant> restaurants = DinrSession.getInstance().getRestaurants();

        if (restaurants == null) {
            Intent intentSplash = new Intent(getActivity(), ActivitySplash.class);
            startActivity(intentSplash);
            getActivity().finish();
        }

        adapter.updateData(restaurants);
        adapter.notifyDataSetChanged();
    }

    public void refreshData() {

        adapter.setUpLocation();
        adapter.notifyDataSetChanged();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(JSONObject errorResponse, String event) {

        restaurantContainer.setRefreshing(false);

    }
}

