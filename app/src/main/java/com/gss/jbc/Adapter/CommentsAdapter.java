package com.gss.jbc.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gss.jbc.DetailedComment;
import com.gss.jbc.PostActivity;
import com.gss.jbc.R;


import com.gss.jbc.Model.CommentsDataModel;
import com.gss.jbc.UserComments;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{
    private Activity activity1;
    private ArrayList<CommentsDataModel> CommentsArrayList;

    //String file_path = "http://jewelnet.in/assets/upload/update" + "/" + news_file_name;


    public CommentsAdapter(UserComments userComments, ArrayList<CommentsDataModel> commentModelArrayList) {
        activity1 = userComments;
        CommentsArrayList = commentModelArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  adapterNewsId, adapterComment, adapterTitle, adapterName, adapterDescription, SeeMore;
        ImageView adapterImage;

        public MyViewHolder(View v) {
            super(v);
            adapterTitle = (TextView) v.findViewById(R.id.adapterTitle);
            adapterDescription = (TextView) v.findViewById(R.id.adapterDescription);
            adapterName = (TextView) v.findViewById(R.id.CommentorName);
            adapterComment = (TextView) v.findViewById(R.id.adapterComment);
            adapterImage = (ImageView) v.findViewById(R.id.adapterImage);
            SeeMore = (TextView) v.findViewById(R.id.SeeMore);

        }
    }

    @NonNull
    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_view, parent, false);
        return new MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.MyViewHolder holder, int position) {
        final String image_file_path;

        final CommentsDataModel Items = CommentsArrayList.get(position);

        holder.adapterTitle.setText(Items.getTitle());
        holder.adapterDescription.setText(Items.getNews_description());
        holder.adapterName.setText(Items.getFname());
        holder.adapterComment.setText(Items.getComment());

        image_file_path ="http://jewelnet.in/assets/upload/update" + "/" + Items.getNews_file_name();
        Picasso.with(activity1).load(image_file_path).into(holder.adapterImage);

        holder.SeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i = new Intent(activity1, DetailedComment.class);

                i.putExtra("post_id", Items.getNews_id());
                i.putExtra("image_file_path", image_file_path);
                i.putExtra("title",Items.getTitle());
                i.putExtra("description",Items.getNews_description());

                activity1.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() { return CommentsArrayList.size(); }
}
