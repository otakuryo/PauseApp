package com.example.otakuryo.pauseapp;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.media.MediaPlayer;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.AlertDialog;
        import android.text.InputType;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.example.otakuryo.pauseapp.adapters.SongCursorAdapter;
        import com.example.otakuryo.pauseapp.dataSongs.SongTable;
        import com.example.otakuryo.pauseapp.dataSongs.Songs;
        import com.example.otakuryo.pauseapp.dataSongs.SongsDBHelper;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.UUID;

        import static android.content.ContentValues.TAG;


public class SongsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Creamos las variables de base de la base de datos.
    ArrayList<Songs> songs;
    ListView listView;
    List<Songs> playListTemp = new ArrayList<>();
    private static SongCursorAdapter adapterSong; //este nos dira como ordenar cada item de la lista
    //private SongsDBHelper songsDBHelper; //instanciamos el modelo que tendra los songs
    //private SongsDBHelper playlistDBHelper; //instanciamos el modelo que tendra los songs para la playlist
    //private SQLiteDatabase db; // crearemos un espacio de trabajo (donde guardaremos y mostraremos nuestros datos)
    //private SQLiteDatabase dbPlaylist; // crearemos un espacio de trabajo (donde guardaremos y mostraremos nuestros datos)

    private MediaPlayer mp = new MediaPlayer(); // instanciamos el reproductor

    private String namePlaylist = "";
    public SongsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongTab.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFragment newInstance(String param1, String param2) {
        SongsFragment fragment = new SongsFragment();
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
        // Aqui es donde añadiremos cualquier modificacion
        //primero creamos una vista (view)
        View view = inflater.inflate(R.layout.fragment_songs, container,false);

        //creamos la barra inferior (toolbar), en este caso sera el boton de play y pausa
        //android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //creamos el fab del play y pause
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setImageResource(MainActivity.state());

        // instanciamos la lista en nuestro layout creado anteriormente (artist_list ubicado en album_tab.xml)
        listView=(ListView) view.findViewById(R.id.list_songs);

        // creamos el nuevo modelo
        songs = new ArrayList<>();

        //Abrimos la base de datos, DBSongs
        //songsDBHelper = new SongsDBHelper(getContext());
        //playlistDBHelper = new SongsDBHelper(getContext());

        // y le decimos que vamos a leer la tabla
        //db = songsDBHelper.getReadableDatabase();
        //dbPlaylist = playlistDBHelper.getWritableDatabase();

        //Le indicamos como debe mostrar la informacion
        adapterSong= new SongCursorAdapter(songs,getActivity().getApplicationContext());
        listView.setAdapter(adapterSong);

        update();

        //instanciamos el toolbar para cambiar el nombre por la que se reproduce actualmente.
        //Activity activity = (Activity) getContext();
        //final Toolbar toolBarInfo = activity.findViewById(R.id.toolbar);

        //le decimos que hara cuando se detecte una posicion larga
        //EN ESTE APARTADO SELECCIONAREMOS Y AÑADIREMOS LAS CANCIONES A LA PLAYLIST

        final FloatingActionButton fabPlaylist = (FloatingActionButton) view.findViewById(R.id.fab_playlist); // creamos el boton del playlist

        //le decimos que hara cuando se presione un item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // OCcultaremos el boton para crear playlist
                //fabPlaylist.setVisibility(View.GONE);
                //Limpia la playlist temporal
                playListTemp.clear();
                //recoge la posicion del item
                if (MainActivity.setMusicsPlays(view,songs,position)){
                    fab.setImageResource(MainActivity.state());
                }else{
                    Toast.makeText(getContext(),"Musica no encontrada...",Toast.LENGTH_LONG).show();
                }
                //obtenemos los datos de la musica y los enviamos para que lo reproduzca

                /*/boolean play = PlayListActivity.setMusicPlay(view,songsModel.getPath());
                if (MainActivity.setMusicPlay(view,songsModel.getPath())){
                    //PlayListActivity.setMusicPlay(view,songsModel.getPath());
                    MainActivity.setMusicsPlaysSongs(view,songs,position);

                }else{
                    Toast.makeText(getContext(),"Musica no encontrada...",Toast.LENGTH_SHORT).show();
                }
                */
                fab.setImageResource(MainActivity.state());
                //en esta parte le decimos que nombre o titulo colocar en la toolbar
                //toolBarInfo.setText(songsModel.getName().trim()+" - "+songsModel.getArtist().trim());
                //view.setSelected(true);
            }
        });

        final CheckBox checkBox = view.findViewById(R.id.checkPlaylist);
        // Se declara que hara el boton fab_playlist
        fabPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                playListTemp = SongCursorAdapter.getplayListTemp();

                if (playListTemp.size() != 0 && !songs.get(0).getcheckboxIsVisible()){ //se verifica que se hayan selccionado canciones y se crea la playlist
                    // se crea el cuadro de dialogo
                    // no me gusta mucho la palabra recuperado
                    setDialogPlaylist(fabPlaylist," (Recuperada)");
                    adapterSong.notifyDataSetChanged();

                    //refrescamos y limpiamos la pantalla/fragmento
                    Fragment frg = null;
                    frg = getFragmentManager().findFragmentByTag(getTag());
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(frg).attach(frg).commit();

                    //ocultamos todos los checkbox
                    for(Songs song: songs){
                        song.setcheckboxIsVisible(false);
                    }
                    adapterSong.notifyDataSetChanged();
                }else if (playListTemp.size() != 0){ //se verifica que se hayan selccionado canciones y se crea la playlist
                    // se crea el cuadro de dialogo
                    setDialogPlaylist(fabPlaylist,"");
                    adapterSong.notifyDataSetChanged();

                    //refrescamos y limpiamos la pantalla/fragmento
                    Fragment frg = null;
                    frg = getFragmentManager().findFragmentByTag(getTag());
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(frg).attach(frg).commit();

                    //ocultamos todos los checkbox
                    for(Songs song: songs){
                        song.setcheckboxIsVisible(false);
                    }
                    adapterSong.notifyDataSetChanged();
                }else if(songs.size() != 0 && songs.get(0).getcheckboxIsVisible()){ //si no hay canciones seleccionadas
                    //ocultamos todos los checkbox
                    for(Songs song: songs){
                        song.setcheckboxIsVisible(false);
                    }
                    adapterSong.notifyDataSetChanged();
                }else{
                    //obtenemos todas las canciones y lo volvemos visible
                    for(Songs song: songs){
                        song.setcheckboxIsVisible(true);
                    }
                    adapterSong.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Seleccione las canciones", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;

        //return inflater.inflate(R.layout.fragment_song_tab, container, false);
    }

    private void refresh(){
        //refrescamos y limpiamos la pantalla/fragmento
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag(getTag());
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg).attach(frg).commit();
    }

    //metodo para el sqlite, actualiza la vista y la BD
    private void update() {
        songs.clear();
        songs.addAll(getAllSongs());
        adapterSong.notifyDataSetChanged();
    }


    //obtiene los datos de la SQLite
    private List<Songs> getAllSongs(){
        Cursor cursor = MainActivity.getDb().rawQuery("select * from "+ SongTable.SongEntry.TABLENAME+" order by name",null);
        List<Songs> list = new ArrayList<Songs>();

        if (cursor.moveToFirst()){
            while (cursor.isAfterLast() == false){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String artist = cursor.getString(cursor.getColumnIndex("artist"));
                String album = cursor.getString(cursor.getColumnIndex("album"));
                String img = cursor.getString(cursor.getColumnIndex("img"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                String playlist = cursor.getString(cursor.getColumnIndex("playlist"));

                list.add(new Songs(id,name,artist,album,path,img,playlist));
                cursor.moveToNext();
            }
        }
        return list;
    }

    public void setDialogPlaylist(final FloatingActionButton fabPlaylist,String recuperado){
        // creamos el cuadro de dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Añadir/Crear"+recuperado);
        builder.setMessage("Escribe un nombre para la playlist\n(Si no existe se creara uno nuevo)");
        builder.setCancelable(false); // Este comando previene que al tocar fuera o atras se salga del dialogo

        // Creamos el campo para ingresar texto
        final EditText input = new EditText(getActivity());

        // Especificamos el tipo de texto a introducir/mostrar ejemplo...
        // input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Creamos los botones
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                namePlaylist = input.getText().toString();
                //Snackbar.make(getView(), "Playlist "+namePlaylist+" añadido.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                //Guardamos la playlistTemporal en la BD
                addToPlaylist(MainActivity.getDbPlaylist(),playListTemp,namePlaylist);
                Toast.makeText(getActivity(), "Playlist "+namePlaylist+" añadido.", Toast.LENGTH_SHORT).show();

                // buscamos nuestro fragmento y refrescamos la vista.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(getFragmentManager().getFragments().get(0)).attach(getFragmentManager().getFragments().get(0)).commit();
                // mover el boton de songs -> playlist add a playlist, no se entiende bien que hacer... y hacer
                // que si no hay nada explique que hacer.

                //Limpia la playlist temporal
                playListTemp.clear();
                // OCcultaremos el boton para crear playlist
                //fabPlaylist.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Limpia la playlist temporal
                playListTemp.clear();
                // OCcultaremos el boton para crear playlist
                //fabPlaylist.setVisibility(View.GONE);
                dialog.cancel();
            }
        });

        builder.show();
    }

    //añadimos a la BD de playlist los elementos seleccionados -> cuidado que aun sigue con la base de datos songs :(
    public static void addToPlaylist(SQLiteDatabase sqLiteDatabase, List<Songs> playListTemp, String playlistName) {
        try {
            for (int i = 0; i < playListTemp.size(); i++){
//            mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),playListTemp.get(i).getName(),playListTemp.get(i).getArtist(),playListTemp.get(i).getAlbum(),playListTemp.get(i).getPath(),playListTemp.get(i).getImg(),playlistName));
                mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),playListTemp.get(i).getName(),playListTemp.get(i).getArtist(),playListTemp.get(i).getAlbum(),playListTemp.get(i).getPath(),null,playlistName));
                //mockSongs(sqLiteDatabase,new Songs(UUID.randomUUID().toString(),"Girls Just want to Have Fun","Cyndy Luper","EP 80's","/mnt/extSdCard/f.mp3","img","Favoritos"));
                String temp = UUID.randomUUID().toString()+" "+playListTemp.get(i).getName()+" "+playListTemp.get(i).getArtist()+" "+playListTemp.get(i).getAlbum()+" "+playListTemp.get(i).getPath()+" "+playListTemp.get(i).getImg()+" "+playListTemp.get(i).getPlaylist();
                Log.i(TAG, temp);
            }
        }catch (Exception e){
            Log.e(TAG, "error al añadir canciones a la lista de reproduccion");
        }
        // mockSongs(sqLiteDatabase,new PlayList(UUID.randomUUID().toString(),playlistName,playListTemp.get(0).getId(),playListTemp.get(0).getName(),playListTemp.get(0).getPath()));
        // mockSongs(sqLiteDatabase,new PlayList(UUID.randomUUID().toString(),"Playlist 1","xzxzxzzx","Cyndy Luper","f.mp3"));
    }

    public static long mockSongs(SQLiteDatabase sqLiteDatabase, Songs songs) {
        return sqLiteDatabase.insert(SongTable.SongEntry.TABLENAMEPL,null, songs.toContentValues());
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
            Log.e(TAG, "onAttach: error Fragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

