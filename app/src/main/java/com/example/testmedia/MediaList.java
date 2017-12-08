package com.example.testmedia;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by E6520 on 12/7/2017.
 */

public class MediaList {
    private String deviceId;
    private String listName;
    private String clientId;
    public ArrayList<MediaItem> mediaList;

    public MediaList(){
        this.mediaList = new ArrayList<MediaItem>();
        this.deviceId = getMacAddr();
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ArrayList<MediaItem> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ArrayList<MediaItem> mediaList) {
        this.mediaList = mediaList;
    }

    public void addMediaItem(MediaItem item){
        this.mediaList.add(item);
    }
}
