package com.example.testmedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

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
        final TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        final TextView tvDetail = (TextView) findViewById(R.id.tvDetail);
        final VideoView vidView = (VideoView) findViewById(R.id.videoView);

        final Context context = this;
        mediaList.add(new MediaItem(this, "http://wallscollection.net/wp-content/uploads/2016/12/Wide-Anchor-Wallpapers.jpg",MediaItem.MEDIA_TYPE_IMAGE));
        mediaList.add(new MediaItem(this, "https://www.androidtutorialpoint.com/wp-content/uploads/2016/09/Beauty.jpg",MediaItem.MEDIA_TYPE_IMAGE));
        //mediaList.add(new MediaItem(this, "https://i.ytimg.com/vi/9uKJNjUYF7I/maxresdefault.jpg",MediaItem.MEDIA_TYPE_IMAGE)); //black
        mediaList.add(new MediaItem(this, "http://www.movieforkids.it/wp-content/gallery/piovono-polpette-2/piovono-polpette-57.jpg",MediaItem.MEDIA_TYPE_IMAGE));
        mediaList.add(new MediaItem(this, "http://video.webmfiles.org/video-h264.mkv",MediaItem.MEDIA_TYPE_VIDEO));
        MediaItem item = new MediaItem(this,"",MediaItem.MEDIA_TYPE_NEWS);
        item.setTitle("News title");
        item.setDetails("Spiffy News Details go here about Donald Trump.");
        mediaList.add(item);


        final int arraySize = mediaList.size();



        new Runnable(){
            int currentIndex = 0;

            @Override
            public void run() {
                if(currentIndex == arraySize){
                    currentIndex=0;
                }
                MediaItem img = mediaList.get(currentIndex);
                if(vidView.isPlaying()){
                    vidView.stopPlayback();
                    vidView.suspend();
                }
                tvTitle.setAlpha(0f);
                tvDetail.setAlpha(0f);

                if(img.getType()==MediaItem.MEDIA_TYPE_IMAGE) {
                    imageView.setAlpha(1f);

                    //vidView.setAlpha(0f);
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
                if(img.getType()==MediaItem.MEDIA_TYPE_NEWS){
                    vidView.setVisibility(View.GONE);
                    tvTitle.setText(img.getTitle());
                    tvDetail.setText(img.getDetails());
                    //tvTitle.setAlpha(1f);
                    //tvDetail.setAlpha(1f);
                    imageView.setImageDrawable(null);
                    tvTitle.animate().alpha(1f).setDuration(300);
                    tvDetail.animate().alpha(1f).setDuration(300);
                    imageView.postDelayed(this,img.getDuration());
                    //tvTitle.animate().alpha(0f).setDuration(300);
                    //tvDetail.animate().alpha(0f).setDuration(300);
                    //tvTitle.setAlpha(0f);
                    //tvDetail.setAlpha(0f);
                }
                if(img.getType()==MediaItem.MEDIA_TYPE_VIDEO) {
                    //tvTitle.setAlpha(0f);
                    //tvDetail.setAlpha(0f);
                    imageView.setAlpha((0f));
                    vidView.setVisibility(View.VISIBLE);

                    String a = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + img.getFileName();
                    File file = new File(a);
                    if (file.exists()) {
                        vidView.setVideoPath(a);
                        //vidView.setAlpha(1f);

                        vidView.start();

                        Log.d("VidView Duration",String.valueOf(vidView.getDuration()));
                        vidView.postDelayed(this,img.getDuration());
                        //vidView.stopPlayback();
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
