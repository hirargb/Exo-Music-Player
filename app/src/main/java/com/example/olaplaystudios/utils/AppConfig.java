package com.example.olaplaystudios.utils;

import com.example.olaplaystudios.models.Songs;

/**
 * Created by AMIT KUMAR KARN on 16-12-2017.
 */

public class AppConfig {
    //Constants and static fields to use throughout the app
    public static final String KEY_SONG = "song_list";
    public static final String SONGS_LIST_API = "http://starlord.hackerearth.com/studio";
    public static final String SONG_NAME = "song";
    public static final String SONG_COVER_IMAGE = "cover_image";
    public static final String SONG_ARTIST_NAMES = "artists";
    public static final String SONG_URL = "url";
    public static Songs CURRENT_SONG = new Songs();
    public static int PlayingSongId = -1;
}
