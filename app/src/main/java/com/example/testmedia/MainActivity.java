package com.example.testmedia;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //make app full screen (get rid of upper tool bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        MediaItem img = new MediaItem(this, "http://wallscollection.net/wp-content/uploads/2016/12/Wide-Anchor-Wallpapers.jpg");
        String a = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+img.getFileName();
        File file = new File(a);
        //Log.v("FILENAME",a);
        if(file.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
            imageView.setImageBitmap(bitmap);
            //Log.v("FILE","Exists!");
        }
        else {
            if(img.getDownloadId()>0){
                Log.v(img.getFileName(),"Still Downloading");
                //check here to compare if modDate is different than the server's mod date.
            }
            else{
                Log.v(img.getFileName(),"Does not exist");
            }
        }
    }
}
