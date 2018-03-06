package com.example.otakuryo.pauseapp.dataSettings;

import android.provider.BaseColumns;

/**
 * Created by ryo on 25/11/17.
 */

public class ServerTable {
    public static abstract class ServerEntry implements BaseColumns {
        public static String TABLENAME = "Servers";

        public static String ID = "id";
        public static String NAME = "name";
        public static String URLSERV = "urlSrv";
        public static String NAMEUSER = "nameUsr";
        public static String PASSUSER = "passUsr";
    }
}
