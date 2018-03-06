package com.example.otakuryo.pauseapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otakuryo.pauseapp.adapters.PlaylistAlbumArtistCursorAdapter;
import com.example.otakuryo.pauseapp.dataSongs.SongTable;
import com.example.otakuryo.pauseapp.dataSongs.Songs;
import com.example.otakuryo.pauseapp.dataSongs.SongsDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PlayListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView mTextView;

    ExpandableListView expandableListView;
    //ExpandableListAdapter expandableListAdapter;
    PlaylistAlbumArtistCursorAdapter playlistAlbumArtistCursorAdapter;
    // Creamos las variables de base de la base de datos.
    ArrayList<String> expandableListTitle;  //en este se apilaran el titulo de cada playlist
    ArrayList<Songs> playLists; //Se apilaran las canciones por cada playlist
    HashMap<String, List<Songs>> expandableListDetail;

    private static ExpandableListView adapterPlayList; //este nos dira como ordenar cada item de la lista
    //private SongsDBHelper playlistDBHelper; //instanciamos el modelo que tendra los playLists
    //private SQLiteDatabase db; // crearemos un espacio de trabajo (donde guardaremos y mostraremos nuestros datos)

    //animacion para actualizar los datos
    private SwipeRefreshLayout swipeContainer;


    public PlayListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PlayListFragment newInstance(String param1, String param2) {
        PlayListFragment fragment = new PlayListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_list, container,false);
        //android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //Animacion para actualizar lista
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.srlContainer);

        //Abrimos la base de datos, PlayListDB
        //playlistDBHelper = new SongsDBHelper(getContext());

        // y le decimos que vamos a leer la tabla
        //db = playlistDBHelper.getWritableDatabase();

        // le decimos donde tiene que crear la lista expandible o acordeon
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_artist);
        update();

        // obtenemos los datos estructurados de la BD de la playlist
        expandableListDetail = getAllSongsbyPlayList();

        // le asignamos un titulo a la grupo
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());

        // creamos el adaptador
        playlistAlbumArtistCursorAdapter = new PlaylistAlbumArtistCursorAdapter(view.getContext(),expandableListTitle,expandableListDetail);

        // y lo instanciamos
        expandableListView.setAdapter(playlistAlbumArtistCursorAdapter);

        //refresh
        swipeContainer.setOnRefreshListener(this);

        //instanciamos el toolbar para cambiar el nombre por la que se reproduce actualmente.
        //Activity activity = (Activity) getContext();
        //final TextView toolBarInfo = (TextView) activity.findViewById(R.id.cabezera);

        //instanciamos el fab del play y pause
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setImageResource(MainActivity.state());

        // en este apartado le diremos que hacer cuando se presiona una cancion
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                //recoge la posicion del item
                Songs playListModel=expandableListDetail.get(expandableListTitle.get(groupPosition))
                        .get(childPosition);
                //obtenemos los datos de la musica y los enviamos para que lo reproduzca
                //boolean play = PlayListActivity.setMusicPlay(view,songsModel.getPath());


                //creamos un nuevo metodo para la reproduccion continua.

                if (MainActivity.setMusicsPlays(view,expandableListDetail.get(expandableListTitle.get(groupPosition)),childPosition)){
                    fab.setImageResource(MainActivity.state());
                }else{
                    Toast.makeText(getContext(),"Musica no encontrada..."+groupPosition,Toast.LENGTH_SHORT).show();
                }
                //en esta parte le decimos que nombre o titulo colocar en la toolbar
                //toolBarInfo.setText(playListModel.getNameSong()+" - "+playListModel.getNamePL());

                return false;
            }
        });

        //al presionar un largo rato, se pondra la cancion por defecto!
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (l>=0){
                    String playlist="";
                    List<Songs> selected = new ArrayList<>();
                    Cursor cursor;
                    if (i<expandableListTitle.size()) {
                        cursor = MainActivity.getDbPlaylist().rawQuery("select * from " + SongTable.SongEntry.TABLENAMEPL + " where " + SongTable.SongEntry.PLAYLIST + "='" + expandableListTitle.get(i) + "'", null);
                    }else{
                        return true;
                    }

                    // Creamos los contenedores donde guardaremos las canciones
                    HashMap<String, List<Songs>> listParent = new HashMap<>();
                    List<Songs> listChild = new ArrayList<>();

                    if (cursor.moveToFirst()){
                        while (!cursor.isAfterLast()){

                            String id = cursor.getString(cursor.getColumnIndex("id"));
                            String name = cursor.getString(cursor.getColumnIndex("name"));
                            String artist = cursor.getString(cursor.getColumnIndex("artist"));
                            String album = cursor.getString(cursor.getColumnIndex("album"));
                            String img = cursor.getString(cursor.getColumnIndex("img"));
                            String path = cursor.getString(cursor.getColumnIndex("path"));
                            playlist = cursor.getString(cursor.getColumnIndex("playlist"));

                            //creamos un songplaylist donde se guardaran los datos de cada musica para la playlist
                            Songs songsPL = new Songs(id,name,artist,album,path,img,playlist);

                            selected.add(songsPL);
                            cursor.moveToNext();
                        }
                    }


                    //MainActivity.setDefaultPlayLists(selected);
                    ImageView imageViewDefault = view.findViewById(R.id.playlistDefault);
                    imageViewDefault.setImageResource(android.R.drawable.star_big_on);

                    SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit(); //le indicamos que editaremos las preferencias
                    editor.putString(MainActivity.defaultPlaylist,playlist); // ingresamos el valor
                    editor.apply(); // guardamos los datos

                    String temp = MainActivity.sharedPreferences.getString(MainActivity.defaultPlaylist,":("); // leemos los datos
                    Toast.makeText(getContext(), "Playlist "+temp+" seleccionada correctamente!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return true;
            }
        });

        final FloatingActionButton fabPlaylist = (FloatingActionButton) view.findViewById(R.id.fab_playlist); // creamos el boton del playlist

        fabPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
                viewPager.setCurrentItem(1);

                Toast.makeText(getContext(), "Para crear un playlist, presione (+) y añada las canciones que desee.", Toast.LENGTH_LONG).show();
            }
        });

        return view;
        //return inflater.inflate(R.layout.fragment_play_list_tab, container, false);
    }

    public void update(){
        if (expandableListDetail != null){
            expandableListDetail = getAllSongsbyPlayList();
        }
        //adapterPlayList.notify();
    }

    private HashMap<String, List<Songs>> getAllSongsbyPlayList(){
        Cursor cursor = MainActivity.getDbPlaylist().rawQuery("select * from "+ SongTable.SongEntry.TABLENAMEPL +" order by playlist",null);
        // List<Songs> list = new ArrayList<Songs>();
        // Creamos los contenedores donde guardaremos las canciones
        HashMap<String, List<Songs>> listParent = new HashMap<>();
        List<Songs> listChild = new ArrayList<>();

        String artistGroup = null; // este sera el nombre del artista que figurara en la BD
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){

                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String artist = cursor.getString(cursor.getColumnIndex("artist"));
                String album = cursor.getString(cursor.getColumnIndex("album"));
                String img = cursor.getString(cursor.getColumnIndex("img"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                String playlist = cursor.getString(cursor.getColumnIndex("playlist"));

                Songs song = new Songs(id,name,artist,album,path,img,playlist);

                //Artist artist1 = new Artist();
                if(listParent.get(playlist) == null){
                    listParent.put(playlist, new ArrayList<Songs>());
                }

                listParent.get(playlist).add(song);
                cursor.moveToNext();
            }
        }
        return listParent;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Log.e(TAG, "onAttach: error Fragment Playlist");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Update fragment
                //getActivity().recreate();

                Fragment frg = null;
                frg = getFragmentManager().findFragmentByTag(getTag());
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg).attach(frg).commit();
                swipeContainer.setRefreshing(false);
            }
        }, 1000);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
