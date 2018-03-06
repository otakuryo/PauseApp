package com.example.otakuryo.pauseapp.dataSettings;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.otakuryo.pauseapp.dataSongs.Songs;
import com.example.otakuryo.pauseapp.dataSongs.SongsDBHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ryo and Pol on 6/01/18.
 */
//Esta tarea guarda la informacion a la base de datos en segundo plano, mientras muestra una pantalla de carga.
public class MyTask extends AsyncTask<Void, Void, Void> {

    ArrayList<Servers> servers;
    private SongsDBHelper songsDBHelper;
    private SQLiteDatabase songsDB;

    ProgressDialog progress;
    SettingsServerManager act;
    ContentResolver musicResolver;

    public MyTask(ProgressDialog progress, SettingsServerManager act, SongsDBHelper songsDBHelper, SQLiteDatabase songsDB,ArrayList<Servers> servers, ContentResolver contentResolver) {
        this.progress = progress;
        this.act = act;
        this.songsDBHelper = songsDBHelper;
        this.songsDB = songsDB;
        this.servers = servers;
        this.musicResolver = contentResolver;
    }

    public void onPreExecute() {
        progress.show();
        //aquí se puede colocar código a ejecutarse previo
        //a la operación
    }

    public void onPostExecute(Void unused) {
        //aquí se puede colocar código que
        //se ejecutará tras finalizar
        //Toast.makeText(act, "Escaneo finalizado.", Toast.LENGTH_SHORT).show();
        progress.dismiss();
    }

    protected Void doInBackground(Void... params) {
        //refreshAllDB(servers);
        getSongList();
        //realizar la operación aquí

        return null;
    }
    public void getSongList() {
        //retrieve song info
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);


        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);

            int uriColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.DATA);

            do {
                String thisId = String.valueOf(musicCursor.getLong(idColumn));
                String thisUri = musicCursor.getString(uriColumn);

                try {
                    File song = new File(thisUri);

                    String[] fileMetaData = getSongData(song.getPath());
                    songsDBHelper.mockSongs(songsDB,new Songs(thisId,fileMetaData[0],fileMetaData[1],fileMetaData[2],thisUri,null,null));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (musicCursor.moveToNext());
        }

    }

    private String[] getSongData(String file){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String[] nom = new String[3];
        String[] separated = file.split("/");
        int textSize = separated.length;
        nom[0] = separated[textSize-1];
        nom[1] = "Artista Desconocido";
        nom[2] = "Album Desconocido";
        try {
            mmr.setDataSource(file);
        }catch (Exception e){
            e.printStackTrace();
            return nom;
        }
        String[] songName = new String[3];
        songName[0] = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)).trim();
        songName[1] = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)).trim();
        songName[2] = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)).trim();
        if (songName[0]==null||songName[0].equals("")){
            songName[0] = nom[0];
        }
        if (songName[1]==null||songName[1].equals("")){
            songName[1] = nom[1];
        }
        if (songName[2]==null||songName[2].equals("")){
            songName[2] = nom[2];
        }
        return songName;
    }
}