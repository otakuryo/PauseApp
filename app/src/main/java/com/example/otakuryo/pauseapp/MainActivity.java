package com.example.otakuryo.pauseapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.otakuryo.pauseapp.automaticPlay.HeadsetIntentReceiver;
import com.example.otakuryo.pauseapp.dataSongs.PlaylistDBHelper;
import com.example.otakuryo.pauseapp.dataSongs.SongTable;
import com.example.otakuryo.pauseapp.dataSongs.Songs;
import com.example.otakuryo.pauseapp.dataSongs.SongsDBHelper;
import com.example.otakuryo.pauseapp.welcome.WelcomeActivity;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SongsDBHelper songsDBHelper; //instanciamos el modelo que tendra los songs
    private PlaylistDBHelper playlistDBHelper; //instanciamos el modelo que tendra los songs
    private static SQLiteDatabase db; // crearemos un espacio de trabajo (donde guardaremos y mostraremos nuestros datos)
    private static SQLiteDatabase dbPlaylist; // crearemos un espacio de trabajo (donde guardaremos y mostraremos nuestros datos)
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static List<Songs> defaultPlayLists;

    //Valores temporales para el reproductor
    public static List<Songs> listaDeReproduccion=null; //lista de reproduccion temporal
    public static Songs playingNows;
    public static int positionTemp=0; //posicion de la musica actual
    static View viewTemp;

    public static MediaPlayer mp = new MediaPlayer(); // instanciamos el reproductor sencillo
    private static int currentPosition=0;
    HeadsetIntentReceiver headsetIntentReceiver;


    //SharedPreferences Temporal space
    public static final String mySettings = "MySettings" ;
    public static final String defaultPlaylist = "playlistKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";

    public static SharedPreferences sharedPreferences;

    //permisos 4
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //final TextView cabezera = findViewById(R.id.cabezera);
        //toolbar.inflateMenu(R.menu.menu_main);
        //Comprobamos que es es la primera vez que se inicia o ya es un usuario antiguo
        String DB_PATH_SONGS = "/data/data/" + getPackageName() + "/databases/Songs.db";
        File fileSong = new File(DB_PATH_SONGS);
        //if (!fileSong.exists()){
        if (!fileSong.exists()){
            Intent welcome = new Intent(MainActivity.this,WelcomeActivity.class);
            startActivityForResult(welcome,0);
        }
        final SearchView searchViews = findViewById(R.id.action_search);
        searchViews.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    //cabezera.setVisibility(View.GONE);
                }else{
                    //cabezera.setVisibility(View.VISIBLE);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        CharSequence title = "Welcome PauseApp";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitleTextAppearance(this, R.style.Light2);
        toolbar.setTitle(s);
        searchViews.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "->"+query, Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(MainActivity.this, "->"+newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        sharedPreferences = getSharedPreferences(mySettings, Context.MODE_PRIVATE); //buscamos el archivo de prefrencias

        //Permisos bloque 2
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
        }
        //Pol Miro
        //llamar al metodo de recibir el intrnt de los cascos
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        headsetIntentReceiver = new HeadsetIntentReceiver();
        try {
            registerReceiver(headsetIntentReceiver, receiverFilter);
        }catch (Exception e){
            Log.e(TAG, "----->Problemas con el metodo de los cascos!!!" );
        }

        //Le informamos como estara estructurado la base de datos
        songsDBHelper = new SongsDBHelper(this);
        playlistDBHelper = new PlaylistDBHelper(this);

        // y le decimos que vamos a leer la tabla
        db = songsDBHelper.getReadableDatabase();
        dbPlaylist = playlistDBHelper.getReadableDatabase();

        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.playlistb);
                }
                if (tab.getPosition() == 4){
                    tab.setIcon(R.drawable.equalizerb);
                }
                //tab.setText("->"+tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.playlistc);
                }
                if (tab.getPosition() == 4){
                    tab.setIcon(R.drawable.equalizera);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //al presionar por un largo rato nos dirigmos al reproductor
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.toggleMusic();
                fab.setImageResource(state());
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent playerIntent = new Intent(MainActivity.this,PlayerBase.class);
                startActivity(playerIntent);
                return true;
            }
        });

    }

    public static void barraEstado(Color colorChange,Toolbar toolbar){
        CharSequence title = "Your App Name";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(s);
        toolbar.setBackgroundColor(Color.RED);
    }
    public static SQLiteDatabase getDb() {
        return db;
    }
    public static SQLiteDatabase getDbPlaylist(){
        return dbPlaylist;
    }

    //Reproduccion de musica
    public static boolean setMusicsPlays(final View view, final List<Songs> songList, int position){
        if (songList.size()<=position){
            Log.e(TAG, "......entro aca" );
            return false;
        }
        playingNows = songList.get(position);
        listaDeReproduccion = songList;
        positionTemp = position;
        viewTemp = view;
        Log.i(TAG, ".......: "+positionTemp+" ......: "+position);
        //comprobamos que el path no esta vacio

        //obtenemos la url completa de la cancion
        final File file = new File(songList.get(position).getPath());
        //paramos la cancion puesta
        if (mp != null){
            mp.stop();
            mp.release();
            mp = null;
        }
        //comenzamos reproducir la cancion
        Uri uri = Uri.fromFile(file);
        mp = MediaPlayer.create(view.getContext(), uri);
        try {
            if (!mp.isPlaying()) {
                Log.e(TAG, "...............: "+positionTemp );
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        positionTemp++;
                        setMusicsPlays(view,songList,positionTemp);
                    }
                });
                return true;
            }
        }catch (Exception exception){
            exception.printStackTrace();
            positionTemp++;
            setMusicsPlays(view,songList,positionTemp);
            return false;
        }
        return false;
    }
    //Este metodo sirve para poner play/pause
    public static void toggleMusic(){
        if (!mp.isPlaying()){
            mp.start();
        }else{
            mp.pause();
        }
    }
    //Este metodo sirve para poner play/pause
    public static void pauseMusic(){
        if (mp.isPlaying()){
            currentPosition = mp.getCurrentPosition();
            mp.pause();
        }
    }
    public static int state(){
        if (!mp.isPlaying()){
            return android.R.drawable.ic_media_play;
        }else{
            return android.R.drawable.ic_media_pause;
        }
    }

    public static boolean playNextSong(){
        if (listaDeReproduccion !=null && positionTemp<listaDeReproduccion.size()-1) {
            positionTemp++;
        }else{
            return false;
        }
        setMusicsPlays(viewTemp,MainActivity.listaDeReproduccion,positionTemp);
        return false;
    }

    public static boolean playPrevSong(){
        if (listaDeReproduccion !=null && positionTemp>0) {
            positionTemp--;
        }else{
            positionTemp=0;
            return false;
        }
        setMusicsPlays(viewTemp,MainActivity.listaDeReproduccion,positionTemp);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //permisos 3
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{

                }
            }break;
        }
    }

    public static List<Songs> getDefaultPlaylist(String pl){
        if (pl.equals("")){
            return null;
        }else{
            return inflatePlaylistDefault(pl);
            //SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit(); //le indicamos que editaremos las preferencias
            //editor.putString(MainActivity.defaultPlaylist,pl); // ingresamos el valor
            //editor.apply(); // guardamos los datos
        }
    }
    public static List<Songs> inflatePlaylistDefault(String namePL){

        List<Songs> selected = new ArrayList<>();
        Cursor cursor;

        cursor = MainActivity.getDbPlaylist().rawQuery("select * from " + SongTable.SongEntry.TABLENAMEPL + " where " + SongTable.SongEntry.PLAYLIST + "='" + namePL + "'", null);

        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){

                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String artist = cursor.getString(cursor.getColumnIndex("artist"));
                String album = cursor.getString(cursor.getColumnIndex("album"));
                String img = cursor.getString(cursor.getColumnIndex("img"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                String playlist = cursor.getString(cursor.getColumnIndex("playlist"));

                //creamos un songplaylist donde se guardaran los datos de cada musica para la playlist
                Songs songsPL = new Songs(id,name,artist,album,path,img,playlist);

                selected.add(songsPL);
                cursor.moveToNext();
            }
        }
        return selected;
    }

    @Override
    protected void onDestroy() {
        db.close();
        dbPlaylist.close();
        super.onDestroy();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            TabLayout tabLayout = findViewById(R.id.tabs);
            Fragment fragment = PlaceholderFragment.newInstance(position + 1);
            PlayListFragment playListFragment = new PlayListFragment();
            SongsFragment songsFragment = new SongsFragment();
            AlbumFragment albumFragment = new AlbumFragment();
            ArtistFragment artistFragment = new ArtistFragment();
            SettingsFragment settingsFragment = new SettingsFragment();

            switch (position){
                case 0:
                    return playListFragment;
                case 1:
                    return songsFragment;
                case 2:
                    return albumFragment;
                case 3:
                    return artistFragment;
                case 4:
                    return settingsFragment;
                default: break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
    }
}
