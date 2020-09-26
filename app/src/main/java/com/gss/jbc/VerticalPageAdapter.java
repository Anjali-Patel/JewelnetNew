package com.gss.jbc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gss.jbc.Extra.RestJsonClient;
import com.gss.jbc.Utility.CommonUtils;
import com.gss.jbc.Utility.SharedPreferenceUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class VerticalPageAdapter extends PagerAdapter {
    private static long onClick;
    TextView textView, description, profile_name, read, address, comp_name,Countviews,like,CommentButton,likecount, CommentCount, ShareCount, Share;
    JSONObject json;
    private Handler handler = new Handler();
    RoundedImageView profile;
    private ImageView news, drawableforplay;
    private ImageView play_bt;
    private ImageView imageAdv;
    private ArrayList<NewsItemStructure> newsItemStructures;
    private ArrayList<NewsCountData> newsCount;
    RelativeLayout profileselect;
    int VpagerPosition;
    String str[] = {"Item one", "Content Description"};
    Context context;
    private LayoutInflater inflater;
    View item;
    String url, userid, NewsId, NewNewsId;
    RelativeLayout frame_layout;
    BitmapDrawable drawable, bd;
    private Activity vpActivity;
    private android.support.v7.app.ActionBar actionBar;
    RelativeLayout descriptionlayout;
    String NewsCount, Liked, NextLike, FinalNewscount = "0";
    int IsLiked, NewsIdNumber;
    SharedPreferenceUtils preferances;
    SharedPreferences sharedPreferences;
    String PagePosition;
    Context mContext = new ItemDisplay();
    private Context context1;
    RelativeLayout commentRelative, UserInteractionRelative;
    String encodedUrl, ImgShare;
    String imageurl;
    private FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    int CurrentPosition, LikeInt;
    int IntPosition;


    VerticalPageAdapter(Activity vpActivity, Context context, ArrayList<NewsItemStructure> newsItemStructures) {
        this.vpActivity = vpActivity;
        this.context = context;
        this.newsItemStructures = newsItemStructures;
        this.inflater = (LayoutInflater) vpActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context1=context;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        item = inflater.inflate(R.layout.app_bar_main, container, false);

        Log.e("pager", String.valueOf(position));

        textView = item.findViewById(R.id.textView);
        description = item.findViewById(R.id.description);
        play_bt = item.findViewById(R.id.play_bt);
        profile = item.findViewById(R.id.profile);
        profileselect = item.findViewById(R.id.profileselect);
        descriptionlayout = item.findViewById(R.id.descriptionlayout);
        news = item.findViewById(R.id.imageView);
        imageAdv = item.findViewById(R.id.image_advertise);
        address = item.findViewById(R.id.address);
        comp_name = item.findViewById(R.id.tvcompny);
        profile_name = item.findViewById(R.id.profile_name);
        drawableforplay = item.findViewById(R.id.drawableforplay);
        frame_layout = item.findViewById(R.id.frame_layout);
        textView.setText(newsItemStructures.get(position).getnTitle());
        read = item.findViewById(R.id.readmore);
        like= item.findViewById(R.id.Like);
        CommentButton = item.findViewById(R.id.CommentButton);
        Countviews= item.findViewById(R.id.Countviews);
        likecount = item.findViewById(R.id.LikeCount);
        CommentCount = item.findViewById(R.id.CommentCount);
        commentRelative = item.findViewById(R.id.commentRelative);
        ShareCount = item.findViewById(R.id.ShareCount);
        Share = item.findViewById(R.id.Share);
        UserInteractionRelative = item.findViewById(R.id.UserInteractionRelative);
        progressBarHolder =  item.findViewById(R.id.progressBarHolder);

        preferances = SharedPreferenceUtils.getInstance(context1);

        PagePosition= preferances.getStringValue(CommonUtils.POSITION,"");
        if (!PagePosition.equalsIgnoreCase("")){
            IntPosition = Integer.parseInt(PagePosition);
        }

        sharedPreferences = context.getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");
        NewsId = newsItemStructures.get(position).getnId();



        int CurrentNewsNumber = Integer.parseInt(NewsId);

        if (IntPosition == 1) {
            NewsIdNumber = (CurrentNewsNumber + 1);

            if (position > 1){
                CurrentPosition = position - 1;
                NextLike = newsItemStructures.get(CurrentPosition).getIsLiked();
                if(!NextLike.equalsIgnoreCase("")){
                    LikeInt = Integer.parseInt(NextLike);
                }
            }

            PostNewsView(NewsIdNumber);
        }
        else{
            NewsIdNumber = (CurrentNewsNumber - 1);

                CurrentPosition = position + 1;
                NextLike = newsItemStructures.get(CurrentPosition).getIsLiked();
            if(!NextLike.equalsIgnoreCase("")){
                LikeInt = Integer.parseInt(NextLike);
            }

            PostNewsView(NewsIdNumber);
        }


        NewNewsId = String.valueOf(NewsIdNumber);

            Liked = newsItemStructures.get(position).getIsLiked();


        if(Liked != null && !Liked.isEmpty() && !Liked.equalsIgnoreCase("null")){
                IsLiked = Integer.parseInt(Liked);
            }

        if (IsLiked > 0) {
            like.setTextColor(Color.parseColor("#58535e"));
            likecount.setTextColor(Color.parseColor("#58535e"));
        }

        String count = newsItemStructures.get(position).getLikeCount();
        String Commcount = newsItemStructures.get(position).getCommentCount();
        String shares = newsItemStructures.get(position).getTotalShares();
        likecount.setText(count);
        CommentCount.setText(Commcount);
        ShareCount.setText(shares);


//        play.setMediaController(new MediaController(vpActivity));
//        actionBar = vpActivity.getSupportActionBar();
        final String desc = newsItemStructures.get(position).getnMessage();

        if (desc.length() > 150) {
            read.setVisibility(View.VISIBLE);
        }
        else read.setVisibility(View.GONE);
        description.setText(desc);

        Countviews.setText(newsItemStructures.get(position).getCount());
        NewsCount = newsItemStructures.get(position).getCount();

        if(NewsCount != null && !NewsCount.isEmpty() && !NewsCount.equalsIgnoreCase("null")
                && !NewsCount.equalsIgnoreCase(" ") ){
                int CountNewsint = Integer.parseInt(NewsCount) * 100;
                FinalNewscount = String.valueOf(CountNewsint);
            preferances.setValue(CommonUtils.NEWSCOUNT, FinalNewscount);
        }
        else{
            preferances.setValue(CommonUtils.NEWSCOUNT, "0");
        }


//        if(!NewsCount.equalsIgnoreCase("null")) {
//            int CountNewsint = Integer.parseInt(NewsCount) * 100;
//            FinalNewscount = String.valueOf(CountNewsint);
//        }

        //int CountNewsint = 100;




//        description.setText(desc.length() > 350 ? desc.substring(0, 350) : desc);

        String fname = newsItemStructures.get(position).getnFname();
        profile_name.setText(fname.length() > 21 ? fname.substring(0, 21) + "..." : fname.replace("null", ""));
        address.setText(newsItemStructures.get(position).getnCompanyAddress().replace("null", ""));
        comp_name.setText(newsItemStructures.get(position).getnCompanyName().replace("null", ""));



        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(item.getContext(), ZoomImageActivity.class);
                if (onClick + 1000 > System.currentTimeMillis()) {
                    intent.putExtra("img", newsItemStructures.get(position).getnFileName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    item.getContext().startActivity(intent);
                } else
//                    Toast.makeText(item.getContext(), "Click again to view post", Toast.LENGTH_SHORT).show();
                    onClick = System.currentTimeMillis();
            }
        });

        if (newsItemStructures.get(position).getnFileType().equalsIgnoreCase("adv")) {

         }
        else if (newsItemStructures.get(position).isnHasProfilePic()) {
            Picasso.with(item.getContext()).load(newsItemStructures.get(position).getnProfilePic())
                    .error(R.drawable.user) .resize(250, 250)
                    .skipMemoryCache().into(profile);
        }

        profileselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(item.getContext(), profile.class);
                i.putExtra("user_id", newsItemStructures.get(position).getnUserId());
                i.putExtra("position", position);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                item.getContext().startActivity(i);
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(item.getContext(), news_more.class);
                i.putExtra("id", newsItemStructures.get(position).getnId());
                i.putExtra("type", newsItemStructures.get(position).getnFileType());
                i.putExtra("img", newsItemStructures.get(position).getnFileName());
                i.putExtra("title", newsItemStructures.get(position).getnTitle());
                i.putExtra("desc", desc);
                i.putExtra("profile", newsItemStructures.get(position).getnProfilePic());
                i.putExtra("profile_name", profile_name.getText().toString());
                i.putExtra("add", address.getText().toString());
                i.putExtra("videoimage",newsItemStructures.get(position).getLargeImage());
                item.getContext().startActivity(i);


            }
        });

        if (newsItemStructures.get(position).getnFileType().equalsIgnoreCase("img")) {
            if (!newsItemStructures.get(position).isnIsAdvertise()) {
                //actionBar.show();
                textView.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                profileselect.setVisibility(View.VISIBLE);
                descriptionlayout.setVisibility(View.VISIBLE);
                news.setVisibility(View.VISIBLE);
                imageAdv.setVisibility(View.GONE);

                if (IntPosition == 1) {
                    ImgShare = newsItemStructures.get(position-1).getnFileName();
                }
                else{
                    ImgShare = newsItemStructures.get(position+1).getnFileName();

                }


                //Load news and profile picture if its news
                Picasso.with(context).load(newsItemStructures.get(position).getnFileName()).skipMemoryCache()
                        .error(R.drawable.default_image).into(news);

            }
        } else if (newsItemStructures.get(position).getnFileType().equalsIgnoreCase("adv")) {
            //Load advertise image if its an advertise
            //actionBar.hide();
            textView.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            profileselect.setVisibility(View.GONE);
            news.setVisibility(View.GONE);
            descriptionlayout.setVisibility(View.GONE);

            imageAdv.setVisibility(View.VISIBLE);
            url = newsItemStructures.get(position).getAdvertisementImageName();
            encodedUrl = newsItemStructures.get(position).getAdvertisementbasepath()+"/"+url.replaceAll(" ","%20");

//            new DownloadImageTask(imageAdv).execute(encodedUrl);
            Picasso.with(item.getContext()).load(encodedUrl).into(imageAdv);
            imageAdv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(item.getContext(), AdvWebview.class);
                    if (onClick + 1000 > System.currentTimeMillis()) {
                        i.putExtra("adv_link", newsItemStructures.get(position).getnAdvLink());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        item.getContext().startActivity(i);
                    } else
                        onClick = System.currentTimeMillis();
                }
            });

        } else {
            imageurl = newsItemStructures.get(position).getLargeImage() + ".png";
            news.setVisibility(View.GONE);
            play_bt.setVisibility(View.VISIBLE);
            drawableforplay.setVisibility(View.VISIBLE);
            descriptionlayout.setVisibility(View.VISIBLE);
            profileselect.setVisibility(View.VISIBLE);
            Picasso.with(item.getContext()).load(imageurl).into(play_bt);

            drawableforplay.setImageDrawable(item.getContext().getResources()
                    .getDrawable(R.drawable.ic_play_circle_outline_red_24dp));

            play_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(item.getContext(), VideoWebViewIframe.class);
                    i.putExtra("url", newsItemStructures.get(position).getLargeImage());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    item.getContext().startActivity(i);
                }
            });
        }


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                likecount.setTextColor(Color.parseColor("#58535e"));
//                likecount.setText("3");
//
//                Log.e("position", String.valueOf((position)));
                if (LikeInt == 0) {

                    LikeAPost();


                } else {
                    ViewLikes();

                }
            }
        });



        CommentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, CommentOnPost.class);
                i.putExtra("textView", newsItemStructures.get(position).getnTitle());
                i.putExtra("description", desc);
                i.putExtra("NewNewsId", NewNewsId);
                i.putExtra("userid", userid);
                i.putExtra("ImgShare", ImgShare);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                vpActivity.finish();
            }
        });

        Share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ShareNews();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\n"+newsItemStructures.get(position).getnTitle()+"\n\n";
                shareMessage = shareMessage +desc+"\n\n";
                shareMessage = shareMessage + "To get updated with more news please download our app by clicking on the link below\nhttps://play.google.com/store/apps/details?id=com.gss.jbc&hl=en \n\n";

                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);


                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


        container.addView(item);
        return item;
    }


    @Override
    public int getCount() {
        return newsItemStructures.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }



    private void LikeAPost() {

        String url= "http://jewelnet.in/index.php/Api/add_likes?news_id="+NewsIdNumber+"&user_id="+userid;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                final String myResponse = responseBody.string();

                vpActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(myResponse);
                            String success = json.getString("status");
                                if (success.equalsIgnoreCase("1")) {

                                Intent i = new Intent(context, ItemDisplay.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                                vpActivity.finish();

//                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(vpActivity);
//                        alertDialogBuilder.setMessage("You have liked successfully.Do You want to see all likes?");
//                        alertDialogBuilder.setCancelable(false);
//
//                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                Intent i = new Intent(vpActivity, AllLikeList.class);
//                                i.putExtra("news_id", NewNewsId);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(i);
//                                vpActivity.finish();
//
//                            }
//                        });
//                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                Intent i = new Intent(context, ItemDisplay.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(i);
//                                vpActivity.finish();
//                            }
//                        });
//
//
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
                          }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });
    }


    private void ViewLikes() {
        Intent i = new Intent(vpActivity, AllLikeList.class);
        i.putExtra("news_id", NewNewsId);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        vpActivity.finish();
    }

    private void PostNewsView(int NewsIdNumber) {

        String url = Constant.BASE_URL + "add_views?news_id=" +NewsIdNumber+"&user_id="+userid;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                final String myResponse = responseBody.string();

                vpActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(myResponse);
                            String success = json.getString("status");
                            if (success.equalsIgnoreCase("1")) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });
    }


    private void ShareNews() {

        //http://jewelnet.in/index.php/Api/add_shares?news_id=4203&user_id=15063
        String url = Constant.BASE_URL + "add_shares?news_id=" +NewsIdNumber+"&user_id="+userid;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                final String myResponse = responseBody.string();

                vpActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(myResponse);
                            String success = json.getString("status");
                            if (success.equalsIgnoreCase("1")) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });
    }

    private void runThread() {

        new Thread() {
            public void run() {
                    try {
                        vpActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                inAnimation = new AlphaAnimation(0f, 1f);
                                inAnimation.setDuration(200);
                                progressBarHolder.setAnimation(inAnimation);
                                progressBarHolder.setVisibility(View.VISIBLE);
                            }
                        });
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

            }
        }.start();
    }

}
