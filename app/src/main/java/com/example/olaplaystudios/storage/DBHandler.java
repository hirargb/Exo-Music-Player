package com.example.olaplaystudios.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.olaplaystudios.models.Songs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit kumar karn on 20-12-2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "musicManager";
    private static final String TABLE_MUSIC = "music";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_IMAGE_URL = "imgurl";
    private static final String KEY_STREAM_URL = "streamurl";
    private static final String KEY_OFFLINE_STORAGE_PATH = "storagepath";
    private static final String KEY_A_FAVORITE = "isafav";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MUSIC_TABLE = "CREATE TABLE " + TABLE_MUSIC + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_ARTIST + " TEXT," + KEY_IMAGE_URL + " TEXT,"
                + KEY_STREAM_URL + " TEXT," + KEY_OFFLINE_STORAGE_PATH + " TEXT,"
                + KEY_A_FAVORITE + " INTEGER " + ")";
        sqLiteDatabase.execSQL(CREATE_MUSIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_MUSIC;
        sqLiteDatabase.execSQL(DROP_TABLE);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Adding new Song
    void addSong(Songs song) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, song.getSongName()); // Song Name
        values.put(KEY_ARTIST, song.getArtistNames()); // Song artist
        values.put(KEY_IMAGE_URL, song.getSongImageUrl());
        values.put(KEY_STREAM_URL, song.getStreamUrl());
        values.put(KEY_A_FAVORITE, song.isAFav());
        // Inserting Row
        db.insert(TABLE_MUSIC, null, values);
        db.close(); // Closing database connection
    }

    // Getting single song
    Songs getSong(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MUSIC, new String[]{KEY_ID,
                        KEY_TITLE, KEY_ARTIST, KEY_IMAGE_URL, KEY_STREAM_URL, KEY_A_FAVORITE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Songs songs = new Songs();
        songs.setSongID(Integer.parseInt(cursor.getString(0)));
        songs.setSongName(cursor.getString(1));
        songs.setArtistNames(cursor.getString(2));
        songs.setSongImageUrl(cursor.getString(3));
        songs.setStreamUrl(cursor.getString(4));
        songs.setAFav(Integer.parseInt(cursor.getString(5)));
        // return contact
        return songs;
    }

    // Getting All Songs
    public List<Songs> getAllSongs() {
        List<Songs> songList = new ArrayList<Songs>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MUSIC;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Songs songs = new Songs();
                songs.setSongID(Integer.parseInt(cursor.getString(0)));
                songs.setSongName(cursor.getString(1));
                songs.setArtistNames(cursor.getString(2));
                songs.setSongImageUrl(cursor.getString(3));
                songs.setStreamUrl(cursor.getString(4));
                songs.setAFav(Integer.parseInt(cursor.getString(5)));
                // Adding song to list
                songList.add(songs);
            } while (cursor.moveToNext());
        }

        // return songs list
        return songList;
    }

    // Updating single song
    public int updateSong(Songs songs) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, songs.getSongID());
        values.put(KEY_TITLE, songs.getSongName());
        values.put(KEY_ARTIST, songs.getArtistNames());
        values.put(KEY_IMAGE_URL, songs.getSongImageUrl());
        values.put(KEY_A_FAVORITE, songs.isAFav());

        // updating row
        return db.update(TABLE_MUSIC, values, KEY_ID + " = ?",
                new String[]{String.valueOf(songs.getSongID())});
    }

    // Deleting single song
    public void deleteSong(Songs songs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MUSIC, KEY_ID + " = ?",
                new String[]{String.valueOf(songs.getSongID())});
        db.close();
    }


    // Getting songs Count
    public int getSongsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MUSIC;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
