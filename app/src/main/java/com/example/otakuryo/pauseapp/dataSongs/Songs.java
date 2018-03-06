package com.example.otakuryo.pauseapp.dataSongs;

import android.content.ContentValues;

/**
 * Created by Otakuryo on 13/01/2018.
 */

public class Songs {
    private String id;
    private String name;
    private String artist;
    private String album;
    private String img;
    private String path;
    private String playlist;
    public boolean checkboxIsVisible=false;
    public boolean isChecked=false;

    public Songs(String id,String name,String artist,String album,String path,String img,String playlist){
        //this.id = UUID.randomUUID().toString();
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.path = path;
        this.img = img;
        this.playlist = playlist;
    }

    public String getId() {
        return id;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }

    public String getPlaylist() {
        return playlist;
    }

    public boolean getcheckboxIsVisible(){return checkboxIsVisible;}

    public void setcheckboxIsVisible(boolean state){this.checkboxIsVisible = state;}
    public void setcheckboxVisible(){this.checkboxIsVisible = true;}
    public void setChecked(boolean state){this.isChecked = state;}
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(SongTable.SongEntry.ID, id);
        values.put(SongTable.SongEntry.NAME, name);
        values.put(SongTable.SongEntry.ARTIST, artist);
        values.put(SongTable.SongEntry.ALBUM, album);
        values.put(SongTable.SongEntry.PATH, path);
        values.put(SongTable.SongEntry.IMAGE, img);
        values.put(SongTable.SongEntry.PLAYLIST, playlist);
        return values;
    }
}
