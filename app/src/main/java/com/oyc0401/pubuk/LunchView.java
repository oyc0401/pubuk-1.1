package com.oyc0401.pubuk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oyc0401.pubuk.databinding.ActivityLunchSearchBinding;
import com.oyc0401.pubuk.databinding.ActivityLunchViewBinding;
import com.oyc0401.pubuk.databinding.ActivityMainBinding;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LunchView extends AppCompatActivity {

    SubsamplingScaleImageView imageView;

    private static final String TAG = "로그";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_view);



        imageView = findViewById(R.id.bigLunchView);
        imageView.setImage(ImageSource.resource(R.drawable.loading));

        Intent secondIntent = getIntent();
        String uri = secondIntent.getStringExtra("uri");
        String menu = secondIntent.getStringExtra("lunchmenu");
        int month =secondIntent.getIntExtra("month",0);
        int date =secondIntent.getIntExtra("date",0);

        TextView bigLunchViewText = findViewById(R.id.bigLunchViewText);
        bigLunchViewText.setText(menu);

        TextView bigLunchViewdate=findViewById(R.id.bigLunchViewdate);
        if(month!=0&&date!=0) bigLunchViewdate.setText(month+"월"+date+"일");

        Log.d(TAG, "onCreate: " + menu);


        new DownloadFilesTask().execute(uri);


    }


    private class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            try {
                imageView.setImage(ImageSource.bitmap(result));
            } catch (Exception e) {
                imageView.setImage(ImageSource.resource(R.drawable.no_lunch));
            }


            Log.d(TAG, "onPostExecute: " + result);
        }
    }


}