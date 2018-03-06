package com.example.otakuryo.pauseapp.dataSettings;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakuryo.pauseapp.R;

import java.util.ArrayList;

class ServerAdapter extends ArrayAdapter<Servers> implements View.OnClickListener {

    private ArrayList<Servers> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtid;
        TextView txtname;
        TextView txturlSrv;
        TextView txtnameUsr;
        TextView txtpassUsr;
        ImageView txtImg;
    }

    public ServerAdapter(ArrayList<Servers> data, Context context) {
        super(context, R.layout.items, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Servers serverModel=(Servers) object;

        switch (v.getId())
        {
            case R.id.img_song:
                Snackbar.make(v, "Img " +serverModel.getNameUsr(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Servers serverModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ServerAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.items, parent, false);

            viewHolder.txtname = (TextView) convertView.findViewById(R.id.title_song);
            //viewHolder.txtnameUsr = (TextView) convertView.findViewById(R.id.title_song);
            viewHolder.txturlSrv = (TextView) convertView.findViewById(R.id.artist_album_song);
            //viewHolder.txtPath = (TextView) convertView.findViewById(R.id.album_song);
            viewHolder.txtImg = (ImageView) convertView.findViewById(R.id.img_song);
            //viewHolder.txtImg.setImageResource(R.drawable.ic_action_settings);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ServerAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtname.setText(serverModel.getName());
        viewHolder.txturlSrv.setText(serverModel.getNameUsr()+" - "+serverModel.getUrlSrv());
        viewHolder.txtImg.setOnClickListener(this);
        viewHolder.txtImg.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}