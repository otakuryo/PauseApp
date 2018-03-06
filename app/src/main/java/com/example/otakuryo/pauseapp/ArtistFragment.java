package com.example.otakuryo.pauseapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.otakuryo.pauseapp.adapters.PlaylistAlbumArtistCursorAdapter;
import com.example.otakuryo.pauseapp.adapters.SongCursorAdapter;
import com.example.otakuryo.pauseapp.dataSongs.SongTable;
import com.example.otakuryo.pauseapp.dataSongs.Songs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ArtistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Creamos la base de la base de datos.
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    // Creamos las variables de base de la base de datos.
    ArrayList<String> expandableListTitle;
    ArrayList<Songs> songs;
    HashMap<String, List<Songs>> expandableListDetail;
    private static SongCursorAdapter adapterSong; //este nos dira como ordenar cada item de la lista
    //private SongsDBHelper songsDBHelper; //instanciamos el modelo que tendra los songs
    //private SQLiteDatabase db; // crearemos un espacio de trabajo (donde guardaremos y mostraremos nuestros datos)

    public ArtistFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ArtistFragment newInstance(String param1, String param2) {
        ArtistFragment fragment = new ArtistFragment();
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
        View view = inflater.inflate(R.layout.fragment_artist, container,false);
        //android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //listView=(ListView) view.findViewById(R.id.artist_list);
        //Abrimos la base de datos, DBSongs
        //songsDBHelper = new SongsDBHelper(getContext());

        // y le decimos que vamos a leer la tabla
        //db = songsDBHelper.getReadableDatabase();

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_atist);
        update();
        //songsGroup.putAll(getAllSongsbyArtist());
        expandableListDetail = getAllSongsbyAlbum();

        // pasamos los datos de la lista de canciones a la lista expandible para el titulo
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        Collections.sort(expandableListTitle); //ordenamos el titulo de la lista expandible

        expandableListAdapter = new PlaylistAlbumArtistCursorAdapter(view.getContext(),expandableListTitle,expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        //instanciamos el toolbar para cambiar el nombre por la que se reproduce actualmente.
        //Activity activity = (Activity) getContext();
        //final TextView toolBarInfo = (TextView) activity.findViewById(R.id.cabezera);

        //creamos el fab del play y pause
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setImageResource(MainActivity.state());

        //listView.setAdapter(adapter);
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
        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String album = expandableListTitle.get(position);

                Snackbar.make(view, album+"\n", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });


        /*/le decimos que hara cuando se presione un item
        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //recoge la posicion del item
                Songs songsModel=(Songs)songs.get(position);
                //obtenemos los datos de la musica y los enviamos para que lo reproduzca
                //boolean play = PlayListActivity.setMusicPlay(view,songsModel.getPath());
                if (PlayListActivity.setMusicPlay(view,songsModel.getPath())){
                    PlayListActivity.setMusicPlay(view,songsModel.getPath());
                }else{
                    Toast.makeText(getContext(),"Musica no encontrada...",Toast.LENGTH_SHORT).show();
                }
                //en esta parte le decimos que nombre o titulo colocar en la toolbar
                toolBarInfo.setText(songsModel.getName()+" - "+songsModel.getArtist());
                //view.setSelected(true);
            }
        });
        */
        return view;
        //return inflater.inflate(R.layout.fragment_artist_tab, container, false);
    }

    private void update() {
        //songsGroup.clear();
        //songsGroup.addAll(getAllSongsbyArtist());
        expandableListDetail = getAllSongsbyAlbum();
        //adapterSong.notifyDataSetChanged();
    }

    private HashMap<String, List<Songs>> getAllSongsbyAlbum(){
        Cursor cursor = MainActivity.getDb().rawQuery("select * from "+ SongTable.SongEntry.TABLENAME +" order by artist",null);
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
                if(listParent.get(artist) == null){
                    listParent.put(artist, new ArrayList<Songs>());
                }

                listParent.get(artist).add(song);
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
        if (context instanceof AlbumFragment.OnFragmentInteractionListener) {
            mListener = (ArtistFragment.OnFragmentInteractionListener) context;
        } else {
            Log.e(TAG, "onAttach: error Fragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
