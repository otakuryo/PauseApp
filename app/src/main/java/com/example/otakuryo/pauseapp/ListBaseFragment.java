package com.example.otakuryo.pauseapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.otakuryo.pauseapp.adapters.SongCursorAdapter;
import com.example.otakuryo.pauseapp.dataSongs.Songs;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListBaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListBaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view; 
    // Creamos las variables de base de la base de datos.
    List<Songs> songs;
    ListView listView;
    List<Songs> playListTemp = new ArrayList<>();
    private static SongCursorAdapter adapterSong; //este nos dira como ordenar cada item de la lista
    private OnFragmentInteractionListener mListener;

    public ListBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListBaseFragment newInstance(String param1, String param2) {
        ListBaseFragment fragment = new ListBaseFragment();
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
        view =inflater.inflate(R.layout.fragment_list_base, container, false);
        //return inflater.inflate(R.layout.fragment_list_base, container, false);
        // instanciamos la lista en nuestro layout creado anteriormente (artist_list ubicado en album_tab.xml)
        listView=(ListView) view.findViewById(R.id.list_songs_now);

        // creamos el nuevo modelo
        if (MainActivity.listaDeReproduccion !=null){
            songs = MainActivity.listaDeReproduccion;
        }else {
            return view;
        }

        //Le indicamos como debe mostrar la informacion
            adapterSong= new SongCursorAdapter((ArrayList<Songs>) songs,getActivity().getApplicationContext()){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    // Recogemos  la posicion del item, como sus dependencias
                    View view = super.getView(position, convertView, parent);

                    // Iniciamos el textvievv del item
                    ImageView img_song = (ImageView) view.findViewById(R.id.img_song);
                    TextView title_song = (TextView) view.findViewById(R.id.title_song);
                    TextView artist_album_song = (TextView) view.findViewById(R.id.artist_album_song);

                    // Asiganmos un color al TextView (ListView Item)
                    img_song.setBackgroundResource(R.drawable.ic_song_img_white);
                    title_song.setTextColor(Color.WHITE);
                    artist_album_song.setTextColor(Color.WHITE);

                    // Devolvemos una vista modificada
                    return view;
                }
            };
            listView.setAdapter(adapterSong);
            update();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // OCcultaremos el boton para crear playlist
                //fabPlaylist.setVisibility(View.GONE);
                //Limpia la playlist temporal
                playListTemp.clear();
                //recoge la posicion del item
                if (MainActivity.setMusicsPlays(view,songs,position)){
                    Toast.makeText(getContext(), "Reproduciendo", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Musica no encontrada...",Toast.LENGTH_LONG).show();
                }
            }
        });
        
        
        return view;
    }

    //metodo para el sqlite, actualiza la vista y la BD
    private void update() {
        adapterSong.notifyDataSetChanged();
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
            Log.e(TAG, "onAttach: error Fragment Base List");
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
