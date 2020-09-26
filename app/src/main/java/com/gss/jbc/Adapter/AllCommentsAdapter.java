package com.gss.jbc.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gss.jbc.DetailedComment;
import com.gss.jbc.Model.CommentsDataModel;
import com.gss.jbc.R;
import com.gss.jbc.UserComments;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllCommentsAdapter extends RecyclerView.Adapter<AllCommentsAdapter.MyViewHolder> {
    private ArrayList<CommentsDataModel> CommentsArrayList;
    private Activity activity1;


    public AllCommentsAdapter(FragmentActivity detailComments, ArrayList<CommentsDataModel> commentModelArrayList) {
        activity1 = detailComments;
        CommentsArrayList = commentModelArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView adapterCommentorName, adapterCommentContent;

        public MyViewHolder(View v) {
            super(v);
            adapterCommentorName = (TextView) v.findViewById(R.id.adapterCommentorName);
            adapterCommentContent = (TextView) v.findViewById(R.id.adapterCommentContent);

        }
    }

    @NonNull
    @Override
    public AllCommentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_comments_list, parent, false);
        return new MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull AllCommentsAdapter.MyViewHolder holder, int position) {

        final CommentsDataModel Items = CommentsArrayList.get(position);

        holder.adapterCommentorName.setText(Items.getFname());
        holder.adapterCommentContent.setText(Items.getComment());

    }

    @Override
    public int getItemCount() {
        return CommentsArrayList.size();
    }

}
