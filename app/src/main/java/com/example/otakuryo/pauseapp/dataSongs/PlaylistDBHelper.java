package com.example.otakuryo.pauseapp.dataSongs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

/**
 * Created by Otakuryo on 14/01/2018.
 */

public class PlaylistDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Playlist.db";

    public PlaylistDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //comandos sql
        sqLiteDatabase.execSQL("CREATE TABLE " + SongTable.SongEntry.TABLENAMEPL + " ( "
                + SongTable.SongEntry.ID + " TEXT PRIMARY KEY,"
                + SongTable.SongEntry.NAME + " TEXT NOT NULL,"
                + SongTable.SongEntry.ARTIST + " TEXT NOT NULL,"
                + SongTable.SongEntry.ALBUM + " TEXT NOT NULL,"
                + SongTable.SongEntry.PATH + " TEXT NOT NULL,"
                + SongTable.SongEntry.IMAGE + " TEXT, "
                + SongTable.SongEntry.PLAYLIST + " TEXT NOT NULL, "
                + " UNIQUE ("+ SongTable.SongEntry.ID +"))"
        );

        //dataSongs(sqLiteDatabase);

    }

    private void dataSongs(SQLiteDatabase sqLiteDatabase) {
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img","Favoritos"));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img","Favoritos"));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img","Favoritosw"));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img","Favoritosw"));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img","Favoritosw"));
    }

    public Cursor getAllSongs(){
        return getReadableDatabase()
                .query(
                        SongTable.SongEntry.TABLENAMEPL,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getSongsbyId(String id){
        Cursor c = getReadableDatabase().query(
                SongTable.SongEntry.TABLENAMEPL,
                null,
                SongTable.SongEntry.ID + "LIKE ?",
                new String[]{id},
                null,
                null,
                null);
        return c;
    }

    public long mockSongs(SQLiteDatabase sqLiteDatabase, Songs songs) {
        return sqLiteDatabase.insert(SongTable.SongEntry.TABLENAMEPL,null, songs.toContentValues());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //para actualizar la tabla, primero borramos y luego creamos, esto solo en entorno de desarollo
        db.execSQL("DROP TABLE IF EXIST "+SongTable.SongEntry.TABLENAMEPL);
        db.execSQL("CREATE TABLE " + SongTable.SongEntry.TABLENAMEPL + " ( "
                + SongTable.SongEntry.ID + " TEXT PRIMARY KEY,"
                + SongTable.SongEntry.NAME + " TEXT NOT NULL,"
                + SongTable.SongEntry.ARTIST + " TEXT NOT NULL,"
                + SongTable.SongEntry.ALBUM + " TEXT NOT NULL,"
                + SongTable.SongEntry.PATH + " TEXT NOT NULL,"
                + SongTable.SongEntry.IMAGE + " TEXT, "
                + " UNIQUE ("+ SongTable.SongEntry.ID +"))"
        );

    }
}
