package com.gss.jbc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;

public class ZoomGalleryImageActivity extends AppCompatActivity {

    Button btnSave;
    PhotoView imgZoomImage;
    Bitmap bitmap;
    String imageUrlOld="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_gallery_image);

        imgZoomImage = (PhotoView) findViewById(R.id.gallery_image_zoom);
        btnSave = (Button) findViewById(R.id.btn_save_image);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              imgZoomImage.setDrawingCacheEnabled(true);
                imgZoomImage.buildDrawingCache(true); //this might hamper performance use hardware acc if available. see: http://developer.android.com/reference/android/view/View.html#buildDrawingCache(boolean)

                //create the bitmaps
                Bitmap zoomedBitmap = Bitmap.createScaledBitmap(imgZoomImage.getDrawingCache(true), 50, 50, true);
                imgZoomImage.setDrawingCacheEnabled(false);
                String resultString = convertBitMapToString(zoomedBitmap);
                  bitmap = BitmapFactory.decodeFile(resultString);
                imgZoomImage.setImageBitmap(bitmap);

                Intent iResult = new Intent();
                iResult.putExtra("image_data", "resultString");
                setResult(1,  iResult);
                finish();
            }
        });


    }

    public String convertBitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);
    }

}
