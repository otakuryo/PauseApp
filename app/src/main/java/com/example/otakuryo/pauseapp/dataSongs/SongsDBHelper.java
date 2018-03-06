package com.example.otakuryo.pauseapp.dataSongs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

/**
 * Created by Otakuryo on 13/01/2018.
 */

public class SongsDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Songs.db";

    public SongsDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //comandos sql
        sqLiteDatabase.execSQL("CREATE TABLE " + SongTable.SongEntry.TABLENAME + " ( "
                + SongTable.SongEntry.ID + " TEXT PRIMARY KEY,"
                + SongTable.SongEntry.NAME + " TEXT NOT NULL,"
                + SongTable.SongEntry.ARTIST + " TEXT NOT NULL,"
                + SongTable.SongEntry.ALBUM + " TEXT NOT NULL,"
                + SongTable.SongEntry.PATH + " TEXT NOT NULL,"
                + SongTable.SongEntry.IMAGE + " TEXT, "
                + SongTable.SongEntry.PLAYLIST + " TEXT, "
                + " UNIQUE ("+ SongTable.SongEntry.ID +"))"
        );

        //dataSongs(sqLiteDatabase);

    }

    private void dataSongs(SQLiteDatabase sqLiteDatabase) {
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
        mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img",null));
    }

    public Cursor getAllSongs(){
        return getReadableDatabase()
                .query(
                        SongTable.SongEntry.TABLENAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getSongsbyId(String id){
        Cursor c = getReadableDatabase().query(
                SongTable.SongEntry.TABLENAME,
                null,
                SongTable.SongEntry.ID + "LIKE ?",
                new String[]{id},
                null,
                null,
                null);
        return c;
    }

    public long mockSongs(SQLiteDatabase sqLiteDatabase, Songs songs) {
        return sqLiteDatabase.insert(SongTable.SongEntry.TABLENAME,null, songs.toContentValues());
    }
    /*
    Para obtener los registros de nuestra tabla usaremos el método query()

    query (String table,
       String[] columns,
       String selection,
       String[] selectionArgs,
       String groupBy,
       String having,
       String orderBy)

    existe otro método alternativo para realizar consultas llamado rawQuery(). Con le pasas como
    parámetro un String del código SQL de la consulta.

    db.rawQuery("select * from " + LawyerEntry.TABLE_NAME, null);

    Si deseas crear una consulta generalizada usa el placeholder '?' en la cláusula WHERE.
    Luego asigna los valores a cada incógnita en el segundo parámetro:

    String query = "select * from " + LawyerEntry.TABLE_NAME + " WHERE _id=?";
    database.rawQuery(query, new String[]{"3"});

    * */


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //para actualizar la tabla, primero borramos y luego creamos, esto solo en entorno de desarollo
        db.execSQL("DROP TABLE IF EXIST "+SongTable.SongEntry.TABLENAME);
        db.execSQL("CREATE TABLE " + SongTable.SongEntry.TABLENAME + " ( "
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
