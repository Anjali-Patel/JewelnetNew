package com.gss.jbc.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gss.jbc.Edit_user_post;
import com.gss.jbc.Model.MypostDataModel;
import com.gss.jbc.MyPosts;
import com.gss.jbc.R;
import com.gss.jbc.VideoWebViewIframe;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.MyViewHolder> {
    private Activity activity1;
    private ArrayList<MypostDataModel> MyPostsArrayList;

    public MyPostsAdapter(MyPosts myposts, ArrayList<MypostDataModel> mypostsModelArrayList) {
        activity1 = myposts;
        MyPostsArrayList = mypostsModelArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView adapterNewsId, adapterComment, adapterTitle, adapterName, adapterDescription, SeeMore;
        ImageView adapterImage, drawableforplay;
        Button edit, delete;

        public MyViewHolder(View v) {
            super(v);
            adapterTitle = (TextView) v.findViewById(R.id.adapterTitle);
            adapterDescription = (TextView) v.findViewById(R.id.adapterDescription);
            adapterImage = (ImageView) v.findViewById(R.id.adapterImage);
            drawableforplay = (ImageView) v.findViewById(R.id.drawableforplay);
            SeeMore = (TextView) v.findViewById(R.id.SeeMore);
            edit = (Button) v.findViewById(R.id.edit);
            delete = (Button) v.findViewById(R.id.delete);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post_list, parent, false);
        return new MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final MypostDataModel Items = MyPostsArrayList.get(position);

        holder.adapterTitle.setText(Items.getPostTitle());
        holder.adapterDescription.setText(Items.getPostDetails());

        //String base = "http://demo1.geniesoftsystem.com/newweb/jewelnetnewone/assets/upload/update/";

        final String imageurl = "http://jewelnet.in/assets/upload/update" + "/" + Items.getPostImage();


        if(Items.getFileType().equalsIgnoreCase("img")){
            String image_file_path ="http://jewelnet.in/assets/upload/update" + "/" + Items.getPostImage().replaceAll(" ","%20");
            Picasso.with(activity1).load(image_file_path).into(holder.adapterImage);
        }
        else{
            holder.drawableforplay.setVisibility(View.VISIBLE);

            String image_file_path ="http://jewelnet.in/assets/upload/update" + "/" + Items.getPostImage()+ ".png";
            Picasso.with(activity1).load(image_file_path).into(holder.adapterImage);

            holder.drawableforplay.setImageDrawable(activity1.getResources()
                    .getDrawable(R.drawable.ic_play_circle_outline_red_24dp));

            holder.adapterImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity1, VideoWebViewIframe.class);
                    i.putExtra("url", imageurl);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity1.startActivity(i);
                }
            });

        }


//        image_file_path ="http://jewelnet.in/assets/upload/update" + "/" + Items.getPostImage();
//        Picasso.with(activity1).load(image_file_path).into(holder.adapterImage);

           holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity1, Edit_user_post.class);
                i.putExtra("post_id", Items.getPostId());
                activity1.startActivity(i);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PostId = Items.getPostId();
                DeleteMyPost(PostId);

            }
        });

//        holder.SeeMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(activity1, DetailedComment.class);
//
//                i.putExtra("post_id", Items.getNews_id());
//                i.putExtra("image_file_path", image_file_path);
//                i.putExtra("title",Items.getTitle());
//                i.putExtra("description",Items.getNews_description());
//
//                activity1.startActivity(i);
//            }
//        });

    }

    public void DeleteMyPost(String PostId) {

        //progess_load.setVisibility(View.VISIBLE);
        String url = "http://jewelnet.in/index.php/Api/delete_post";

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("post_id", PostId)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                final String myResponse = responseBody.string();

                activity1.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(myResponse);

                            String success = json.getString("status");

                            if (success.equalsIgnoreCase("1")) {

                                Toast.makeText(activity1, "Post deleted successfully!",
                                        Toast.LENGTH_LONG).show();

                                Intent i = new Intent(activity1, MyPosts.class);
                                activity1.startActivity(i);

                            }

                            else {

                                Toast.makeText(activity1, "Opps! Some problem occured while deleting your post, please try again after some time.",
                                        Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });



//        http://demo1.geniesoftsystem.com/newweb/jewelnetnewone/index.php/Apivi/edit_news
//
//        post_id:4201
//        title:hello
//        message:demo
//        file_type:mp4


    }

    @Override
    public int getItemCount() { return MyPostsArrayList.size(); }
}
