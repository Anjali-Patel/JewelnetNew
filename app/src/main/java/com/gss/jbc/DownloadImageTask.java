package com.gss.jbc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask< String, Void, Bitmap>{
    ImageView bmImage = null;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {


        String urldisplay = urls[0];
        Bitmap icon = null;
        try {

            InputStream inStream = new java.net.URL(urldisplay).openStream();
            if(inStream!=null)
                icon = BitmapFactory.decodeStream(inStream);
            inStream.close();
        } catch (OutOfMemoryError e){
            Log.e("Error", e.getStackTrace().toString());
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return icon;

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            bmImage.setImageBitmap(bitmap);
            bitmap.recycle();
        }
    }
}