package com.example.otakuryo.pauseapp;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otakuryo.pauseapp.dataSongs.Songs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.example.otakuryo.pauseapp.MainActivity.listaDeReproduccion;
import static com.example.otakuryo.pauseapp.MainActivity.mp;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReproductorBaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReproductorBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReproductorBaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Handler handler;
    Runnable runnable;
    View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    static boolean playing = false;
    int beginTime=0;
    int endTime=0;
    Songs songsRep=null;

    public ReproductorBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReproductorCPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReproductorBaseFragment newInstance(String param1, String param2) {
        ReproductorBaseFragment fragment = new ReproductorBaseFragment();
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
        view =inflater.inflate(R.layout.fragment_reproductor_base, container, false);
        final SeekBar seekBarSongs;

        seekBarSongs = view.findViewById(R.id.seekBar1);

        //creamos el fab del play y pause
        final ImageView imageViewPlay = view.findViewById(R.id.play);
        imageViewPlay.setImageResource(MainActivity.state());

        //handler = new Handler(); // parte de el sub proceso de la seekbar.
        final TextView songTimeBegin = view.findViewById(R.id.now);
        final TextView songTimeEnd = view.findViewById(R.id.end);
        ImageView playListImg = view.findViewById(R.id.playlist);

        Activity activity = (Activity) getContext();

        final TextView textViewTitle1 = activity.findViewById(R.id.toolbar_title1);
        final TextView textViewTitle2 = activity.findViewById(R.id.toolbar_title2);
        final TextView textViewplayingSong = view.findViewById(R.id.playingSong);
        final TextView textViewplayingArtistAlbum = view.findViewById(R.id.playingArtistAlbum);

        //tiempo de la musica a mostrar
        endTime = mp.getDuration();
        //beginTime = mp.getCurrentPosition();
        //songTimeEnd.setText(String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes((long) endTime), TimeUnit.MILLISECONDS.toSeconds((long) endTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) endTime))));
        //songTimeBegin.setText(""+beginTime);
        changeAllUi(view,endTime,textViewTitle1,textViewTitle2,textViewplayingSong,textViewplayingArtistAlbum,songTimeEnd);
        seekBarSongs.setMax(endTime);

        final Runnable updateSeekBarTime = new Runnable() {
            public void run() {
                handler = new Handler();

                //get current position
                int timeElapsed = mp.getCurrentPosition();
                //int finalTime = mp.getDuration();
                //int timeRemaining = endTime - timeElapsed;
                endTime = mp.getDuration();
                seekBarSongs.setMax(endTime);
                if (endTime==mp.getCurrentPosition()){
                    seekBarSongs.setMax(endTime);
                    changeAllUi(view,endTime,textViewTitle1,textViewTitle2,textViewplayingSong,textViewplayingArtistAlbum,songTimeEnd);
                }

                //set seekbar progress using time played
                seekBarSongs.setProgress((int) timeElapsed);

                //set time remaining in minutes and seconds
                songTimeBegin.setText(String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
                songTimeEnd.setText(String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes((long) endTime), TimeUnit.MILLISECONDS.toSeconds((long) endTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) endTime))));

                //holding time for 100 miliseconds
                handler.postDelayed(this, 1000);
            }
        };
        if (endTime>0) {
            updateSeekBarTime.run();
        }
        //changeAllUi(view);

        seekBarSongs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if (input){
                    mp.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        ImageView prev = view.findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.playPrevSong();
                changeAllUi(view,endTime,textViewTitle1,textViewTitle2,textViewplayingSong,textViewplayingArtistAlbum,songTimeEnd);
            }
        });
        ImageView next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.playNextSong();
                changeAllUi(view,endTime,textViewTitle1,textViewTitle2,textViewplayingSong,textViewplayingArtistAlbum,songTimeEnd);
            }
        });

        ImageView play = view.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.toggleMusic();
                statePlaying(view);
            }
        });
        ImageView addPlaylistFav = view.findViewById(R.id.item1);
        addPlaylistFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardamos la playlistTemporal en la BD
                List<Songs> heart = new ArrayList<>();
                heart.add(MainActivity.playingNows);
                //if (heart.size()<0) {
                if(MainActivity.mp.isPlaying()){
                    SongsFragment.addToPlaylist(MainActivity.getDbPlaylist(), heart, "Favoritos");
                    Toast.makeText(getContext(), "Añadido a Favoritos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView additem3 = view.findViewById(R.id.item3);
        enContruccion(additem3);
        ImageView additem4 = view.findViewById(R.id.item4);
        enContruccion(additem4);
        ImageView additem5 = view.findViewById(R.id.item5);
        enContruccion(additem5);


        playListImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
                viewPager.setCurrentItem(1);
            }
        });

        return view;
    }

    private void enContruccion(ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "No tiene ningún modulo!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void changeAllUi(View view, int timeElapsed,TextView textViewTitle1,TextView textViewTitle2,TextView textViewplayingSong,TextView textViewplayingArtistAlbum,TextView songTimeEnd){
        String filepath = " ";
        String title = "PauseApp";
        String artist = "Artist";
        String album = "Album";
        String end = "00:00";

        if (MainActivity.listaDeReproduccion != null && listaDeReproduccion.size()>MainActivity.positionTemp) {
            title = MainActivity.listaDeReproduccion.get(MainActivity.positionTemp).getName();
            artist = MainActivity.listaDeReproduccion.get(MainActivity.positionTemp).getArtist();
            album = MainActivity.listaDeReproduccion.get(MainActivity.positionTemp).getAlbum();
            end = String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed)));
        }
        if (title == null){
            title = "Error Titulo";
        }
        if (artist == null){
            artist = "Error Artist";
        }
        if (album == null){
            album = "Error Album";
        }
        if (end == null){
            end = "00;00";
        }

        textViewTitle1.setText(title);
        textViewTitle2.setText(album);
        textViewplayingSong.setText(title);
        textViewplayingArtistAlbum.setText(artist + " - " + album);
        songTimeEnd.setText(end);
    }

    private void statePlaying(View view){
        ImageView play = view.findViewById(R.id.play);
        //handler.postDelayed(updateSeekBarTime,1000);
        //servicePlaySeek();
        if(mp.isPlaying()){
            play.setImageResource(R.drawable.pause);
        }else if (!mp.isPlaying()){
            play.setImageResource(android.R.drawable.ic_media_play);
        }
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
            //throw new RuntimeException(context.toString()+ " must implement OnFragmentInteractionListener");
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
