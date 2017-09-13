package com.example.testmedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //make app full screen (get rid of upper tool bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        final ArrayList<MediaItem> mediaList = new ArrayList<MediaItem>();
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final Context context = this;
        mediaList.add(new MediaItem(this, "http://wallscollection.net/wp-content/uploads/2016/12/Wide-Anchor-Wallpapers.jpg"));
        mediaList.add(new MediaItem(this, "https://www.androidtutorialpoint.com/wp-content/uploads/2016/09/Beauty.jpg"));
        mediaList.add(new MediaItem(this, "https://i.ytimg.com/vi/9uKJNjUYF7I/maxresdefault.jpg")); //black
        mediaList.add(new MediaItem(this, "http://www.movieforkids.it/wp-content/gallery/piovono-polpette-2/piovono-polpette-57.jpg"));

        final int arraySize = mediaList.size();
        new Runnable(){
            int currentIndex = 0;
            @Override
            public void run() {
                if(currentIndex == arraySize){
                    currentIndex=0;
                }
                MediaItem img = mediaList.get(currentIndex);
                if(img.getType()==MediaItem.MEDIA_TYPE_IMAGE) {
                    String a = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + img.getFileName();
                    File file = new File(a);
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                        imageView.setImageBitmap(bitmap);
                        imageView.postDelayed(this, img.getDuration());
                        //Log.v("FILE","Exists!");
                    } else {
                        if (img.getDownloadId() > 0) {
                            Log.v(img.getFileName(), "Still Downloading");
                            //check here to compare if modDate is different than the server's mod date.  If the Server's is newer, then download again.
                        } else {
                            Log.v(img.getFileName(), "Does not exist");
                        }
                    }
                }
                currentIndex++;
            }
        }.run();

    }
}
