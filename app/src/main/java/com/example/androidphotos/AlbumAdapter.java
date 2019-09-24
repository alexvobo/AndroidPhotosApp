package com.example.androidphotos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlbumAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Album> albums;
    // 1
    public AlbumAdapter(Context context, ArrayList<Album> albums) {
        this.mContext = context;
        this.albums = albums;
    }
    @Override
    public int getCount() {
        return albums.size();
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Album album = albums.get(position);


        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_album, null);
        }

        // 3
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_album_thumb);
        final TextView albumName = (TextView)convertView.findViewById(R.id.textview_album_name);

        // 4
        if (album.getPhotos().size() > 0){
            imageView.setImageBitmap(album.getThumbnail());
        } else {
            imageView.setImageResource(android.R.drawable.spinner_background);
        }
        albumName.setText(album.getName());

        return convertView;
    }

}