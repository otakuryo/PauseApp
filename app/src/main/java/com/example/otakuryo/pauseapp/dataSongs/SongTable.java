package com.example.otakuryo.pauseapp.dataSongs;

import android.provider.BaseColumns;

/**
 * Created by Otakuryo on 13/01/2018.
 */

public class SongTable {
    public static abstract class SongEntry implements BaseColumns {
        public static String TABLENAME = "Songs";
        public static String TABLENAMEPL = "Playlist";

        public static String ID = "id";
        public static String NAME = "name";
        public static String ARTIST = "artist";
        public static String ALBUM = "album";
        public static String IMAGE = "img";
        public static String PATH = "path";
        public static String PLAYLIST = "playlist";
    }
}