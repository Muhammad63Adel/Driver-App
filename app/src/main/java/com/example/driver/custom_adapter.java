package com.example.driver;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class custom_adapter extends ArrayAdapter<target> {

    private static final String LOG_TAG = custom_adapter.class.getSimpleName();

    public custom_adapter(Activity context, ArrayList<target> order){

        super(context, 0, order);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list, parent, false);
        }

        TextView latitudeTextView = convertView.findViewById(R.id.latitude);
        TextView longitudeTextView = convertView.findViewById(R.id.longitude);


        target current_target = getItem(position);

        assert current_target != null;
        latitudeTextView.setText(current_target.getLatitude());
        longitudeTextView.setText(current_target.getLongitude());
        return convertView;


    }
}
