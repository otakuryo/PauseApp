package com.example.otakuryo.pauseapp.dataSettings;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

public class ServerDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Servers.db";

    public ServerDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //comandos sql
        sqLiteDatabase.execSQL("CREATE TABLE " + ServerTable.ServerEntry.TABLENAME + " ( "
                + ServerTable.ServerEntry.ID + " TEXT PRIMARY KEY,"
                + ServerTable.ServerEntry.NAME + " TEXT NOT NULL,"
                + ServerTable.ServerEntry.URLSERV + " TEXT NOT NULL,"
                + ServerTable.ServerEntry.NAMEUSER+ " TEXT,"
                + ServerTable.ServerEntry.PASSUSER + " TEXT,"
                + " UNIQUE ("+ ServerTable.ServerEntry.ID +"))"
        );

        //dataServer(sqLiteDatabase);

    }

    private void dataServer(SQLiteDatabase sqLiteDatabase) {
        mockServer(sqLiteDatabase,new Servers(UUID.randomUUID().toString(),"External Storage","/mnt/extSdCard","Ryo","pass"));
        mockServer(sqLiteDatabase,new Servers(UUID.randomUUID().toString(),"Internal Storage","/sdcard","Ryo","pass"));
        mockServer(sqLiteDatabase,new Servers(UUID.randomUUID().toString(),"Root Storage","/storage/0000-0000","Ryo","pass"));
    }
    public long mockServer(SQLiteDatabase sqLiteDatabase, Servers server) {
        return sqLiteDatabase.insert(ServerTable.ServerEntry.TABLENAME,null, server.toContentValues());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //para actualizar la tabla, primero borramos y luego creamos, esto solo en entorno de desarollo
        db.execSQL("DROP TABLE IF EXIST "+ServerTable.ServerEntry.TABLENAME);
        db.execSQL("CREATE TABLE " + ServerTable.ServerEntry.TABLENAME + " ( "
                + ServerTable.ServerEntry.ID + " TEXT PRIMARY KEY,"
                + ServerTable.ServerEntry.NAME + " TEXT NOT NULL,"
                + ServerTable.ServerEntry.URLSERV + " TEXT NOT NULL,"
                + ServerTable.ServerEntry.NAMEUSER+ " TEXT,"
                + ServerTable.ServerEntry.PASSUSER + " TEXT,"
                + " UNIQUE ("+ ServerTable.ServerEntry.ID +"))"
        );

    }
}

