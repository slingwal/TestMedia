package com.example.testmedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String serverName = "192.168.7.158";
    final String zip = "84118";
    final String clientId = "test";


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

        //these variables will eventually need to be configured preferences.

        String deviceId = getMacAddr();
        Log.v("MAC ADDRESS",deviceId);



        final ArrayList<MediaItem> mediaList = new ArrayList<MediaItem>();
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        final TextView tvDetail = (TextView) findViewById(R.id.tvDetail);
        final VideoView vidView = (VideoView) findViewById(R.id.videoView);

        final Context context = this;
        mediaList.add(new MediaItem(this, "http://wallscollection.net/wp-content/uploads/2016/12/Wide-Anchor-Wallpapers.jpg",MediaItem.MEDIA_TYPE_IMAGE));
        //mediaList.add(new MediaItem(this, "https://i.ytimg.com/vi/9uKJNjUYF7I/maxresdefault.jpg",MediaItem.MEDIA_TYPE_IMAGE)); //black
        mediaList.add(new MediaItem(this, "http://www.movieforkids.it/wp-content/gallery/piovono-polpette-2/piovono-polpette-57.jpg",MediaItem.MEDIA_TYPE_IMAGE));
        mediaList.add(new MediaItem(this, "http://video.webmfiles.org/video-h264.mkv",MediaItem.MEDIA_TYPE_VIDEO));
        mediaList.add(new MediaItem(this, "https://www.androidtutorialpoint.com/wp-content/uploads/2016/09/Beauty.jpg",MediaItem.MEDIA_TYPE_IMAGE));
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

                tvTitle.setAlpha(0f);
                tvDetail.setAlpha(0f);

                if(img.getType()==MediaItem.MEDIA_TYPE_IMAGE) {
                    //imageView.setAlpha(1f);

                    //vidView.setAlpha(0f);
                    String a = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + img.getFileName();
                    File file = new File(a);
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                        //Drawable d = new BitmapDrawable(getResources(),bitmap);
                        //vidView.setBackground(d);
                        //vidView.setVisibility(View.GONE);

                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.bringToFront();
                        //Log.v("FILE","Exists!");
                        //Log.d("VidView Duration",String.valueOf(vidView.getDuration()));
                        imageView.postDelayed(this,img.getDuration());
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
                    imageView.setVisibility(View.GONE);
                    vidView.setVisibility(View.GONE);
                    tvTitle.setText(img.getTitle());
                    tvDetail.setText(img.getDetails());
                    //tvTitle.setAlpha(1f);
                    //tvDetail.setAlpha(1f);
                    //imageView.setImageDrawable(null);

                    tvTitle.animate().alpha(1f).setDuration(300);
                    tvDetail.animate().alpha(1f).setDuration(300);
                    imageView.postDelayed(this,img.getDuration());
                    //tvTitle.animate().alpha(0f).setDuration(300);
                    //tvDetail.animate().alpha(0f).setDuration(300);
                    //tvTitle.setAlpha(0f);
                    //tvDetail.setAlpha(0f);
                }
                if(vidView.isPlaying()){
                    imageView.bringToFront();
                    //imageView.invalidate();
                    //imageView.setImageDrawable(null);
                    //vidView.setBackground(null);
                    //vidView.setZOrderOnTop(false);
                    //vidView.pause();
                    vidView.stopPlayback();

                    vidView.suspend();
                }
                if(img.getType()==MediaItem.MEDIA_TYPE_VIDEO) {
                    //tvTitle.setAlpha(0f);
                    //tvDetail.setAlpha(0f);
                    //imageView.setAlpha((0f));
                    vidView.setVisibility(View.VISIBLE);

                    String a = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + img.getFileName();
                    File file = new File(a);
                    if (file.exists()) {

                        vidView.setVideoPath(a);
                        //vidView.requestFocus();
                        //vidView.seekTo(1);
                        //vidView.setZOrderOnTop(true);
                        //vidView.setAlpha(1f);
                        vidView.bringToFront();
                        vidView.setZOrderOnTop(true);
                        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer mp) {
                                //vidView.setZOrderOnTop(true);
                                vidView.start();
                                vidView.setZOrderOnTop(false);
                            }
                        });

                        //vidView.setBackground(null);
                        //imageView.setVisibility(View.GONE);

                        //Log.d("VidView Duration",String.valueOf(duration[0]));
                        vidView.postDelayed(this,img.getDuration());

                        //imageView.setAlpha(1f);
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

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    private class MediaServicesTask extends AsyncTask<Void, Void, Void> {
        String deviceId;
        protected Void onPreExecute(Void... Void){
            deviceId = getMacAddr();
            Log.v("MAC ADDRESS",deviceId);
         return null;
        }
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://"+serverName);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET"); //use post when going secure
                client.setRequestProperty("deviceId",deviceId);
                client.setDoOutput(true);

                InputStream in = new BufferedInputStream(client.getInputStream());
                readStream(in);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException error) {
                error.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... progress){
        }

        protected void onPostExecute(Void result) {

        }

        private String readStream(InputStream is) {
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                int i = is.read();
                while(i != -1) {
                    bo.write(i);
                    i = is.read();
                }
                return bo.toString();
            } catch (IOException e) {
                return "";
            }
        }
    }

}
