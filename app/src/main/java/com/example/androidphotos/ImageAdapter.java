package com.example.androidphotos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Photo> photos;
    // 1
    public ImageAdapter(Context context, ArrayList<Photo> photoAlbum) {
        this.mContext = context;
        this.photos = photoAlbum;
    }
    @Override
    public int getCount() {
        return photos.size();
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

        final Photo photo = photos.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_photo, null);
        }

        // 3
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_photo_thumb);
        final TextView photoName = (TextView)convertView.findViewById(R.id.textview_photo_name);

        // 4
        imageView.setImageBitmap(photo.getImage());
        photoName.setText(photo.getName());

        return convertView;
    }

}