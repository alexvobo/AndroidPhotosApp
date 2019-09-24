package com.example.androidphotos;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private ArrayList<Photo> photos;
    private String name;
    private int albumID;
    private static final long serialVersionUID = 0L;


    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }


    public Album(String name) {
        this.name = name;
        photos = new ArrayList<>();
    }
    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }
    public Photo getPhoto(int index) {
        return this.photos.get(index);
    }

    public ArrayList<Photo> getPhotos() {
        return this.photos;
    }

    public String getName() {
        return this.name;
    }

    public Bitmap getThumbnail() {
        return photos.get(0).getImage();
    }

    public int getCount() {
        return this.photos.size();
    }

    public void setName(String newName) {
        this.name = newName;
    }


    public void addPhoto(Photo photo) {
        photo.setPhotoID(getCount());
        photos.add(photo);
    }
    public void deletePhoto(Photo photo) {
        for (int i = photo.getPhotoID(); i < getCount(); i++){
            this.photos.get(i).setPhotoID(getPhotos().get(i).getPhotoID());
        }
        photos.remove(photo);
    }

}
