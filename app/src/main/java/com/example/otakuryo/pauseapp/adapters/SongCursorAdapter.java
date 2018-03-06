package com.example.otakuryo.pauseapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otakuryo.pauseapp.R;
import com.example.otakuryo.pauseapp.dataSongs.Songs;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Otakuryo on 13/01/2018.
 */

public class SongCursorAdapter extends ArrayAdapter<Songs> implements View.OnClickListener {

    private ArrayList<Songs> dataSet;
    Context mContext;
    static List<Songs> playListTemp = new ArrayList<>();
    // View lookup cache
    private static class ViewHolder {
        TextView txtId;
        TextView txtName;
        TextView txtArtistAlbum;
        TextView txtPath;
        ImageView txtImg;
        TextView txtPlaylist;
        CheckBox checkBox;

        public CheckBox getCheckBox() {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox)
        {
            this.checkBox = checkBox;
        }
    }
    public static List<Songs> getplayListTemp(){return playListTemp;}

    public SongCursorAdapter(ArrayList<Songs> data, Context context) {
        super(context, R.layout.items, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        CheckBox checkBox = (CheckBox) v;
        int position = (Integer) v.getTag();
        Songs songsModel=getItem(position);
        getItem(position).setChecked(checkBox.isChecked());
        //playListTemp.add(songsModel);
        if (checkBox.isChecked()){
            playListTemp.add(songsModel);
            Toast.makeText(this.getContext(), "Total añadidos: "+playListTemp.size(), Toast.LENGTH_SHORT).show();
        }else{
            try{
                playListTemp.remove(searchSong(songsModel));
                Toast.makeText(this.getContext(), "Total añadidos: "+playListTemp.size(), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e(TAG, "playlistTemp:  Error -1" );
            }
        }
    }

    public int searchSong(Songs songsModel){
        for (int i = 0; i < playListTemp.size(); i++) {
            if (songsModel.getId().equals(playListTemp.get(i).getId())){
                return i;
            }
        }
        return -1;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Songs songsModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        SongCursorAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new SongCursorAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.items, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.title_song);
            viewHolder.txtArtistAlbum = (TextView) convertView.findViewById(R.id.artist_album_song);
            viewHolder.txtImg = (ImageView) convertView.findViewById(R.id.img_song);
            //viewHolder.txtImg.setImageResource(R.drawable.ic_song_img);

            viewHolder.checkBox = convertView.findViewById(R.id.checkPlaylist);
            //viewHolder.checkBox.setChecked(songsModel.isChecked);

            //viewHolder.checkBox.setVisible(songsModel.checkboxIsVisible?View.VISIBLE:View.GONE);
            //viewHolder.checkBox.setChecked(songsModel.isChecked);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SongCursorAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(songsModel.getName());
        viewHolder.txtArtistAlbum.setText(songsModel.getArtist()+" - "+songsModel.getAlbum());
        viewHolder.checkBox.setChecked(songsModel.isChecked);

        viewHolder.getCheckBox().setTag(position);
        viewHolder.getCheckBox().setChecked(songsModel.isChecked);
        viewHolder.getCheckBox().setOnClickListener(this);

        //identificara si el checkbox debe o no mostrarse.
        viewHolder.checkBox.setVisibility(songsModel.checkboxIsVisible?View.VISIBLE:View.GONE);

        return convertView;
    }

}
