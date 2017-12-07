package com.example.testmedia;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Date;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by slingwal on 9/13/2017.
 */

public class MediaItem {
    private int order;
    private int type;
    private String url;
    private String fileName;
    private int duration; //in milliseconds
    private int transitionType; //0 will be no transition, 1 =fade, then we can get cheesy with fancy wipes, etc.
    private Date timeToPlay;
    private Boolean playImmediate;//used to make the task play the next
    private Boolean isInRotation;
    private Date modDate;
    private DownloadManager downloadManager;
    private long downloadId;
    private String title;
    private String details;

    //only media types so far.  We can later define a weather, sports or news feed type that would behave as a different media item.
    public final static int MEDIA_TYPE_IMAGE = 0;
    public final static int MEDIA_TYPE_VIDEO = 1;
    public final static int MEDIA_TYPE_NEWS = 2;
    public final static int MEDIA_TYPE_WEATHER = 3;
    public final static int MEDIA_TYPE_SPORTS = 4;

    //can define more transitions here if needed
    public final static int MEDIA_TRANSITION_TYPE_NONE = 0;
    public final static int MEDIA_TRANSITION_TYPE_FADE = 1;

    public MediaItem(Context context, String url, int Type, Date modDate ){
        this.type=Type;
        this.url = url;
        this.modDate = modDate;
        if(url.length()>0) {
            this.fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
            String a = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName;

            File file = new File(a);
            Date lastModified = new Date(file.lastModified());
            Log.v("FILE CHECK",file.toString()+" modified:"+lastModified.toString()+" modDate:"+modDate.toString());

            if (!file.exists()) {
                Uri uri = Uri.parse(url);
                downloadId = DownloadData(context, uri, fileName);
                //Date lastModified2 = new Date(file.lastModified());
                Log.v("FILE NO EXIST",file.toString()+" modified:"+lastModified.toString());
            } else {//check if modDate from the server is newer
                //Date lastModified2 = new Date(file.lastModified());
                Log.v("FILE CHECK",file.toString()+" modified:"+lastModified.toString());
                if (this.getModDate().after(lastModified)){
                    Uri uri = Uri.parse(url);
                    downloadId = DownloadData(context, uri, fileName);
                    Log.v("FILE CHECK","Downloading and OverWriting");
                }
            }
        }
        this.duration= 3*1000; //10 seconds
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(int transitionType) {
        this.transitionType = transitionType;
    }

    public Date getTimeToPlay() {
        return timeToPlay;
    }

    public void setTimeToPlay(Date timeToPlay) {
        this.timeToPlay = timeToPlay;
    }

    public Boolean getPlayImmediate() {
        return playImmediate;
    }

    public void setPlayImmediate(Boolean playImmediate) {
        this.playImmediate = playImmediate;
    }

    public Boolean getInRotation() {
        return isInRotation;
    }

    public void setInRotation(Boolean inRotation) {
        isInRotation = inRotation;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    private long DownloadData (Context context, Uri uri, String name) {

        long downloadReference;

        // Create request for android download manager
        downloadManager = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("Data Download");

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");

        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,name);

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);




        return downloadReference;
    }
}
