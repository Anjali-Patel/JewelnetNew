package com.gss.jbc.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gss.jbc.Constant;
import com.gss.jbc.Model.StateModel;
import com.gss.jbc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StateAdapter extends ArrayAdapter<StateModel> {

    private ArrayList<StateModel> arrayList = new ArrayList<StateModel>();
    private Context context;
    private Activity activity;
    public StateAdapter(Context context, ArrayList<StateModel> arrayList) {
        super(context, R.layout.state_listitems, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public StateModel getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
           /* LayoutInflater in = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = in.inflate(R.layout.country_listitems, null);*/

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.state_listitems, parent, false);
        }
        TextView state_name = convertView.findViewById(R.id.state_name);
        state_name.setText(arrayList.get(position).getState_name());

        return convertView;
    }
}


