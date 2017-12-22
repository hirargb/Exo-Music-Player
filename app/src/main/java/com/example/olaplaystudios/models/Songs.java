package com.example.olaplaystudios.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by AMIT KUMAR KARN on 16-12-2017.
 */

public class Songs implements Serializable {
    private int songID;
    private String songName;
    private String songImageUrl;
    private String streamUrl;
    private String artistNames;
    private boolean isPlaying;
    private boolean isPaused;
    private int isAFav;
    private boolean storagePath;
    private boolean isFromDataBase;
    private Bitmap picture;

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongImageUrl() {
        return songImageUrl;
    }

    public void setSongImageUrl(String songImageUrl) {
        this.songImageUrl = songImageUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getArtistNames() {
        return artistNames;
    }

    public void setArtistNames(String artistNames) {
        this.artistNames = artistNames;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int isAFav() {
        return isAFav;
    }

    public void setAFav(int AFav) {
        isAFav = AFav;
    }

    public boolean isStoragePath() {
        return storagePath;
    }

    public void setStoragePath(boolean storagePath) {
        this.storagePath = storagePath;
    }

    public boolean isFromDataBase() {
        return isFromDataBase;
    }

    public void setFromDataBase(boolean fromDataBase) {
        isFromDataBase = fromDataBase;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
