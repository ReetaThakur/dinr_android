package com.godynamo.dinr.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.godynamo.dinr.R;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.City;

import java.util.HashMap;
import java.util.List;

/**
 * Created by danko on 4/25/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<String> listDataHeader; // header titles
    private ImageView downArrow;

    // child data in format of header title, child title
    private final HashMap<String, List<City>> listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<City>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = ((City) getChild(groupPosition, childPosition)).getName_en();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.cities_exandable_list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/MarkSmallCaps-Regular.otf");

        txtListChild.setText(childText);
        txtListChild.setTypeface(myTypeface);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/MarkSmallCaps-Regular.otf");
        lblListHeader.setTypeface(myTypeface);

        downArrow = (ImageView) convertView.findViewById(R.id.down_arrow);

        if (listDataChild.get(this.listDataHeader.get(groupPosition)).size() != 0) {
            downArrow.setVisibility(View.VISIBLE);
        }else{
            downArrow.setVisibility(View.INVISIBLE);
        }

        if(isExpanded){
            downArrow.setRotation(180f);
            lblListHeader.setText("Cities");
        }else{
            downArrow.setRotation(0f);
            if(DinrSession.getInstance().getSelectedCity() != null) {
                lblListHeader.setText(DinrSession.getInstance().getSelectedCity().getName_en());
            }else{
                lblListHeader.setText("Cities");
            }
        }


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}