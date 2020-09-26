package com.gss.jbc.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gss.jbc.AllLikeList;
import com.gss.jbc.Model.LikeModel;
import com.gss.jbc.R;

import java.util.ArrayList;

public class LikeAdapter extends  RecyclerView.Adapter<LikeAdapter.MyViewHolder> {

    private Activity activity1;
    private ArrayList<LikeModel> CommentsArrayList;

//String file_path = "http://jewelnet.in/assets/upload/update" + "/" + news_file_name;


    public LikeAdapter(AllLikeList userComments, ArrayList<LikeModel> commentModelArrayList) {
        activity1 = userComments;
        CommentsArrayList = commentModelArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,company_name;


        public MyViewHolder(View v) {
            super(v);
            name=v.findViewById(R.id.name);
            company_name=v.findViewById(R.id.company);

        }
    }

    @NonNull
    @Override
    public LikeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_list, parent, false);
        return new LikeAdapter.MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull LikeAdapter.MyViewHolder holder, int position) {


        final LikeModel Items = CommentsArrayList.get(position);

        holder.name.setText(Items.getFname());
        holder.company_name.setText(Items.getCompany_name());


    }

    @Override
    public int getItemCount() { return CommentsArrayList.size(); }
}
