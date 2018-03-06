package com.example.otakuryo.pauseapp.dataSettings;

import android.provider.BaseColumns;

/**
 * Created by Otakuryo on 14/01/2018.
 */

public class SettingsPauseApp {
    public static abstract class EntrySettings implements BaseColumns {
        public static final String MyPREFERENCES = "MySettings" ;
        public static final String Name = "nameKey";
        public static final String Phone = "phoneKey";
        public static final String Email = "emailKey";
    }
}
