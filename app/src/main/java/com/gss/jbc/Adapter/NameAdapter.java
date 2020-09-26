package com.gss.jbc.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gss.jbc.Model.StateModel;
import com.gss.jbc.Model.UserModel;
import com.gss.jbc.R;

import java.util.ArrayList;

public class NameAdapter extends ArrayAdapter<UserModel> {

    private ArrayList<UserModel> arrayList = new ArrayList<UserModel>();
    private Context context;
    private Activity activity;
    public NameAdapter(Context context, ArrayList<UserModel> arrayList) {
        super(context, R.layout.username_listitems, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public UserModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.username_listitems, parent, false);
        }
        TextView user_name = convertView.findViewById(R.id.user_name);
        user_name.setText(arrayList.get(position).getUser_name());

        return convertView;
    }
}
