package com.example.otakuryo.pauseapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.example.otakuryo.pauseapp.adapters.SettingsCursorAdapter;
import com.example.otakuryo.pauseapp.dataSettings.Settings;
import com.example.otakuryo.pauseapp.dataSettings.SettingsServerManager;

import java.io.File;
import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

import static android.content.ContentValues.TAG;
import static com.example.otakuryo.pauseapp.MainActivity.barraEstado;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    ArrayList<Settings> dataModels; //estamos reutilizando el modelo de musica...
    ListView listView;
    private static SettingsCursorAdapter adapter;
    static final String SWITCH_TOOG_BUTT= "SWITCH_TOGGLEBUTTON";
    ToggleButton toggleButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
                             Bundle savedInstanceState) {     // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container,false);

        //instanciamos la toolbar  y tablayout para cambiar de color
        //final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        final TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        final Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        //final TextView toolBarInfo = (TextView) getActivity().findViewById(R.id.cabezera);



        //Toolbar toolbar =view.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.blanco));

        //return inflater.inflate(R.layout.fragment_settings_tab, container, false);

        listView=(ListView) view.findViewById(R.id.setting_lists);

        dataModels= new ArrayList<>();

        dataModels.add(new Settings(0, "Servidores", "Server default",false));
        dataModels.add(new Settings(0, "Tema - Navegacion", "Puedes encontrar el bug?",false));
        //dataModels.add(new DataModel(" ", "Tema - Reproductor","Old Times","September 15, 2009",false));
        //dataModels.add(new DataModel(" ", "Administrar Modulos", "Instalados:","2",false));
        dataModels.add(new Settings(0, "Reproduccion Aleatoria", "Activa la funcion de reproduccion aleatoria",true));
        dataModels.add(new Settings(0, "Reproduccion Automatica", "Activa la funcion de los cascos.",true));
        dataModels.add(new Settings(0, "Reproduccion Automatica Prioritaria", "Prevalece sobre la cancion en marcha.",true));
        dataModels.add(new Settings(0, "Cache", "Limpiar la base de datos.",false));

        adapter= new SettingsCursorAdapter(dataModels,getActivity().getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TextView textView = view.findViewById(R.id.info_set);

                Settings dataModel = dataModels.get(position);
                int itemPosition = position;
                switch (position) {
                    case 0:
                        Intent serverSet = new Intent(view.getContext(), SettingsServerManager.class);
                        startActivityForResult(serverSet, 0);
                        break;
                    case 1:
                        //Llamamos a la clase colorpicker y lo mostramos
                        ColorPicker colorPicker = new ColorPicker(getActivity());
                        colorPicker.setTitle("Selecciona un color");
                        colorPicker.setRoundColorButton(true);
                        colorPicker.show();
                        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {
                                //este parametro cambia el color de fondo...
                                //container.setBackground(new ColorDrawable(Color.BLACK));


                                if (color == 0) {
                                    return;
                                }
                                //toolbar.setBackgroundColor(getResources().getColor(R.color.color2));
                                //toolbar.setBackgroundColor(color);
                                tabLayout.setBackgroundColor(color);


                                CharSequence title = "Cambiado";
                                //SpannableString s = new SpannableString(title);
                                //s.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                //toolbar.setTitle(s);

                                if (color == getResources().getColor(R.color.color3) || color == getResources().getColor(R.color.color5) ||
                                        color == getResources().getColor(R.color.color12)) {

                                    toolbar.setBackgroundColor(color);

                                    //toolBarInfo.setTextColor(getResources().getColor(R.color.black_de));
                                } else {

                                    toolbar.setBackgroundColor(color);

                                    //toolBarInfo.setTextColor(getResources().getColor(android.R.color.white));
                                }
                                //PlayListActivity.setColorBar(color);
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                        //fin de colorpicker

                        break;
                        /*
                    case 2:
                        String PATH_SONGS = "/data/data/" + getContext().getPackageName() + "/databases/Songs.db";
                        File fileSongs = new File(PATH_SONGS);
                        if (fileSongs.exists()) {
                            long t = fileSongs.length();
                            double kb = t / 1024;
                            Toast.makeText(getActivity(), "Songs.db: " + kb, Toast.LENGTH_SHORT).show();
                        }
                        break;
                        */
                    case 2:
                        toggleButton = (ToggleButton) toggleButton.findViewById(R.id.switch_settings);
                        switch (view.getId()){
                            case R.id.switch_settings:
                                toggleButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        /*
                                        if (playListService.isShuffle()) {
                                            toggleButton.setChecked(false);
                                            playListService.setShuffle();

                                            Toast.makeText(getContext(), "is off",
                                                    Toast.LENGTH_SHORT).show();

                                        } else {
                                            playListService.setShuffle();
                                            toggleButton.setChecked(true);
                                            Toast.makeText(getContext(), "is on",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                        */
                                    }
                                });
                                break;
                            default:
                                break;
                        }
                        break;
                    case 3:
                        switch (view.getId()){
                            case R.id.switch_settings:

                                break;
                            default:
                                break;
                        }
                        break;
                    case 5:
                        dialogoDeLimpiezaOpc3().show();
                        break;
                    default:
                        Snackbar.make(view, dataModel.gettitle_settings() + "\n" + dataModel.getdes_settings() + dataModel.getdes_settings(), Snackbar.LENGTH_LONG)
                                .setAction("No action", null).show();
                        break;
                }
            }
        });
        return view;
    }

    public AlertDialog dialogoDeLimpiezaOpc3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        builder.setTitle("Limpieza de Cache")
                .setMessage("Esta seguro?\n(No eliminara las canciones del dispositivo)")
                .setPositiveButton("ACEPTAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String DB_PATH_SONGS = "/data/data/" + getContext().getPackageName() + "/databases/Songs.db";
                                File fileSong = new File(DB_PATH_SONGS);
                                if (fileSong.exists()){
                                    fileSong.delete();
                                    long t = fileSong.length();
                                    Toast.makeText(getActivity(), "Songs.db: "+t, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(), "No existe...Songs", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
                            }
                        });

        return builder.create();
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
}
