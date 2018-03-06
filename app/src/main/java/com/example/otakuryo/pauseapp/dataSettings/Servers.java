package com.example.otakuryo.pauseapp.dataSettings;

import android.content.ContentValues;

/**
 * Created by Otakuryo on 14/01/2018.
 */


public class Servers {
    private String id;
    private String name;
    private String urlSrv;
    private String nameUsr;
    private String passUsr;

    public Servers(String id,String name,String urlSrv,String nameUsr,String passUsr){
        //this.id = UUID.randomUUID().toString();
        this.id = id;
        this.name = name;
        this.urlSrv = urlSrv;
        this.nameUsr = nameUsr;
        this.passUsr = passUsr;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameUsr() {
        return nameUsr;
    }

    public String getPassUsr() {
        return passUsr;
    }

    public String getUrlSrv() {
        return urlSrv;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ServerTable.ServerEntry.ID, id);
        values.put(ServerTable.ServerEntry.NAME, name);
        values.put(ServerTable.ServerEntry.URLSERV, urlSrv);
        values.put(ServerTable.ServerEntry.NAMEUSER, nameUsr);
        values.put(ServerTable.ServerEntry.PASSUSER, passUsr);
        return values;
    }
}
