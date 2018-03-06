package com.example.otakuryo.pauseapp.automaticPlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.otakuryo.pauseapp.MainActivity;
import com.example.otakuryo.pauseapp.dataSongs.Songs;

import java.util.ArrayList;
import java.util.List;

import static com.example.otakuryo.pauseapp.MainActivity.mp;

/**
 * Created by Pol Miro on 14/01/2018.
 * //TODO añadir que reproduzca la playList por defecto al enchufar los cascos, y que pare al quitarlos.
 */

public class HeadsetIntentReceiver extends BroadcastReceiver {
    private String TAG = "HeadSet";
    public HeadsetIntentReceiver() {
        Log.d(TAG, "Created");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            View view = new View(context);
            int state = intent.getIntExtra("state", -1);
            switch(state) {
                case(0):
                    MainActivity.pauseMusic();
                    Toast.makeText(context, "Auriculares desconectado...", Toast.LENGTH_SHORT).show();
                    break;
                case(1):
                    if (mp.getDuration()>0){
                        mp.start();
                    }else {
                        String playlistdef = MainActivity.sharedPreferences.getString(MainActivity.defaultPlaylist, ""); // leemos los datos
                        List<Songs> playlist = MainActivity.getDefaultPlaylist(playlistdef);
                        if (!playlistdef.equals("") && playlist.size() >= 0) {
                            MainActivity.setMusicsPlays(view, playlist, 0);
                        } else {
                            Toast.makeText(context, "No hay una playlist por defecto, manten pulsado encima de una playList para ponerla por defecto", Toast.LENGTH_LONG).show();
                        }
                    }
                    Toast.makeText(context, "Auriculares conectado...", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d(TAG, "Error");
            }
        }
    }

}