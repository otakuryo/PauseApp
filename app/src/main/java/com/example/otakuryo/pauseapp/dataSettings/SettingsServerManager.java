package com.example.otakuryo.pauseapp.dataSettings;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.otakuryo.pauseapp.R;
import com.example.otakuryo.pauseapp.dataSongs.Songs;
import com.example.otakuryo.pauseapp.dataSongs.SongsDBHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by ryo on 25/11/17.
 */

//forma de agustin.
public class SettingsServerManager extends AppCompatActivity {

    // Creamos la base de la base de datos.
    ArrayList<Servers> servers;
    ArrayList<Songs> songsList = new ArrayList<>();
    ListView listView;
    private ServerAdapter adapterServer;
    private ServerDBHelper serversDBHelper;
    private SongsDBHelper songsDBHelper;
    private SQLiteDatabase db;
    private SQLiteDatabase songsDB;
    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_server_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Asignamos el color negro a la barra de estado
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //decor.setSystemUiVisibility(0);

        //final Window window = this.getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(ContextCompat.getColor(this, R.color.black_de));
        //fin de color de barra de estado...

        // instanciamos la lista en nuestro layout creado anteriormente (server_list ubicado en Activity_Setting_Server.xml)
        listView=(ListView) findViewById(R.id.server_list);

        // creamos el nuevo modelo
        servers = new ArrayList<Servers>();

        //Abrimos la base de datos, DBSongs
        serversDBHelper = new ServerDBHelper(this);
        songsDBHelper = new SongsDBHelper(this);

        // y le decimos que vamos a leer la tabla
        songsDB = songsDBHelper.getWritableDatabase();
        db = serversDBHelper.getWritableDatabase();

        //Le indicamos como debe mostrar la informacion
        adapterServer= new ServerAdapter(servers,SettingsServerManager.this);
        listView.setAdapter(adapterServer);

        update();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabs);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("resource/folder");
                //startActivityForResult(intent, 0);
                //Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(Intent.createChooser(i, "Seleccione un directorio: "), 9999);
                //create();
            }
        });
        ImageView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //esto actualizara la musica dentro de los servidores y mostrara un dialogo
                ProgressDialog progress = new ProgressDialog(SettingsServerManager.this);
                progress.setMessage("Actualizando datos, por favor espere...");
                progress.setCancelable(false);
                new MyTask(progress, SettingsServerManager.this,songsDBHelper,songsDB,servers,getContentResolver()).execute();

                //refreshAllDB(servers);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String uriExt;
        switch(requestCode) {
            case 9999:
                if (data != null){
                    String uriStr = String.valueOf(data.getData());
                    //String uriExt = String.valueOf(data.getData()).replace("content://com.android.externalstorage.documents/tree/","/");

                    if (uriStr.contains("tree/primary")){
                        uriExt = uriStr.replace("content://com.android.externalstorage.documents/tree/primary%3A","/sdcard/");
                    }else{
                        uriExt = uriStr.replace("content://com.android.externalstorage.documents/tree/","/storage/");
                    }
                    uriExt = uriExt.replace("%3A","/");
                    Log.i("Test", "Result URI " + uriExt);
                    addPath(uriExt);
                }
                break;
        }
    }

    private void addPath(String extUri){
        ContentValues nuevoReg = new ContentValues();
        nuevoReg.put("id", UUID.randomUUID().toString());
        nuevoReg.put("name","Local Storage");
        nuevoReg.put("urlSrv",extUri);
        nuevoReg.put("nameUsr","Nothing");
        nuevoReg.put("passUsr","Nothing");
        db.insert("Servers",null,nuevoReg);
        //dataServer(db);
        update();
        Toast.makeText(SettingsServerManager.this, "Agregado con exito", Toast.LENGTH_SHORT).show();
    }

    private void update() {
        servers.clear();
        servers.addAll(getAllServers());
        adapterServer.notifyDataSetChanged();
    }

    private List<Servers> getAllServers(){
        Cursor cursor = db.rawQuery("SELECT * FROM Servers",null);
        List<Servers> list = new ArrayList<Servers>();

        if (cursor.moveToFirst()){
            while (cursor.isAfterLast() == false){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                Log.i(String.valueOf(this),"......" +id);
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String urlSrv = cursor.getString(cursor.getColumnIndex("urlSrv"));
                String nameUsr = cursor.getString(cursor.getColumnIndex("nameUsr"));
                String passUsr = cursor.getString(cursor.getColumnIndex("passUsr"));

                list.add(new Servers(id,name,urlSrv,nameUsr,passUsr));
                cursor.moveToNext();
            }
        }
        return list;
    }
    //hay dos formas de ingresar datos...
    //la primera es... uno a uno
    private void create(){
        setDialogPlaylist();
    }

    public void setDialogPlaylist(){
        // creamos el cuadro de dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsServerManager.this);
        builder.setTitle("Añadir y buscar una carpeta");
        builder.setMessage("Escribe un nombre de una carpeta\n");
        builder.setCancelable(false); // Este comando previene que al tocar fuera o atras se salga del dialogo

        // Creamos el campo para ingresar texto
        final EditText input = new EditText(SettingsServerManager.this);

        // Especificamos el tipo de texto a introducir/mostrar ejemplo...
        // input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Creamos los botones
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ContentValues nuevoReg = new ContentValues();
                nuevoReg.put("id", UUID.randomUUID().toString());
                nuevoReg.put("name",input.getText().toString());
                nuevoReg.put("urlSrv","/musica/carpeta de prueba");
                nuevoReg.put("nameUsr","Ryo");
                nuevoReg.put("passUsr","123321");
                db.insert("Servers",null,nuevoReg);
                //dataServer(db);
                update();
                Toast.makeText(SettingsServerManager.this, "Agregado con exito", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Limpia la playlist temporal
                // OCcultaremos el boton para crear playlist
                //fabPlaylist.setVisibility(View.GONE);
                dialog.cancel();
            }
        });

        builder.show();
    }

    //la seguna es todo en una linea... aunque para ello necesitamos 2 clases extra...
    private void dataServer(SQLiteDatabase sqLiteDatabase) {
        mockServer(sqLiteDatabase,new Servers(UUID.randomUUID().toString(),"Local Path","file://extSd/","Ryo","pass"));
    }

    public long mockServer(SQLiteDatabase sqLiteDatabase, Servers server) {
        return sqLiteDatabase.insert(ServerTable.ServerEntry.TABLENAME,null, server.toContentValues());
    }

}
