package com.example.otakuryo.pauseapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.otakuryo.pauseapp.R;
import com.example.otakuryo.pauseapp.dataSongs.Songs;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ryo on 7/01/18.
 */

public class PlaylistAlbumArtistCursorAdapter extends BaseExpandableListAdapter {


    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<Songs>> expandableListDetail;

    public PlaylistAlbumArtistCursorAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<Songs>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    //int listPosition, int expandedListPosition
    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.list_group_title);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    //En esta parte rellenamos los datos de cada item, de cada cancion.
    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Songs expandedListText = (Songs) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.items, null);

        }
        TextView expandedListSongTitle= (TextView) convertView.findViewById(R.id.title_song);
        TextView expandedListSongAlbum = (TextView) convertView.findViewById(R.id.artist_album_song);
        expandedListSongTitle.setText(expandedListText.getName());
        String albumArtist = expandedListText.getAlbum()+" - "+expandedListText.getArtist();
        expandedListSongAlbum.setText(albumArtist);
        return convertView;
        //return null;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}

