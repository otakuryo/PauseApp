package com.example.otakuryo.pauseapp.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.otakuryo.pauseapp.R;
import com.example.otakuryo.pauseapp.dataSettings.Settings;

import java.util.ArrayList;

/**
 * Created by Otakuryo on 14/01/2018.
 */

public class SettingsCursorAdapter extends ArrayAdapter<Settings> implements View.OnClickListener {

    private ArrayList<Settings> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtArtist;
        TextView txtAlbum;
        ImageView imgSong;
        Switch switchSet;
    }

    public SettingsCursorAdapter(ArrayList<Settings> data, Context context) {
        super(context, R.layout.items_settings, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Settings dataModel=(Settings)object;

        switch (v.getId())
        {
            case R.id.img_song:
                Snackbar.make(v, "Release date " +dataModel.getImg_settings(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Settings dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.items_settings, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.title_set);
            viewHolder.txtArtist = (TextView) convertView.findViewById(R.id.info_set);
            viewHolder.imgSong = (ImageView) convertView.findViewById(R.id.img_song);
            //viewHolder.imgSong.setImageResource(R.drawable.ic_action_settings);
            viewHolder.switchSet = convertView.findViewById(R.id.switch_settings);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.gettitle_settings());
        viewHolder.txtArtist.setText(dataModel.getdes_settings());
        //viewHolder.imgSong.setOnClickListener(this);
        //viewHolder.imgSong.setTag(position);
        // Return the completed view to render on screen
        //identificara si el checkbox debe o no mostrarse.
        viewHolder.switchSet.setVisibility(dataModel.getSwitch_isVisible()?View.VISIBLE:View.GONE);
        return convertView;
    }
}
