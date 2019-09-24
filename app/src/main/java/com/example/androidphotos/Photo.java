package com.example.androidphotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.*;


public class Photo implements Serializable {

	private String name;
	private String location; //stock or one of the users
	private String caption;
	private int imageResource;
	private Bitmap image;
	private int photoID;
	private int albumID;

	public int getPhotoID() {
		return photoID;
	}

	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}

	public int getAlbumID() {
		return albumID;
	}

	public void setAlbumID(int albumID) {
		this.albumID = albumID;
	}

	public ArrayList<Tags> tags;
	private static final long serialVersionUID = 0L;

	public ArrayList<Tags> getTags() {
		return tags;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public Photo(String name) {
	    this.name = name;
		this.tags = new ArrayList<Tags>();
		this.tags.add(new Tags("person")); //index 0
		this.tags.add(new Tags("location")); //index 1
		this.setImage(null);
	}

	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * @param tagType tag type
	 * @param tagValue tag value to add
	 * adds tag value to tag list
	 */
	public void addTagValue(String tagType, String tagValue){
		for (int i = 0; i < tags.size(); i++){
			if (tags.get(i).getTagType().equals(tagType)){
				tags.get(i).addTagValue(tagValue);
			}
		}
	}
	/**
	 * @param tagType tag type
	 * @param tagValue tag value to delete
	 * deletes tag value from tag list
	 */
	public void deleteTagValue(String tagType, String tagValue){
		for (int i = 0; i < tags.size(); i++){
			if (tags.get(i).getTagType().toLowerCase().equals(tagType.toLowerCase())){
				for (int k = 0; k < tags.get(i).getTagValues().size(); k++){
					if (tags.get(i).getTagValues().get(k).toLowerCase().equals(tagValue.toLowerCase())){
						tags.get(i).deleteTagValue(tagValue);
					}
				}
			}
		}
	}

	public String getFirstTagByType(String tagType) {
		for(int i = 0; i <  tags.size(); i++){
			if(tags.get(i).getTagType().toLowerCase().equals(tagType.toLowerCase())){
				if(tags.get(i).getFirstTag() != null){
					return tags.get(i).getFirstTag();
				} else {
					return null;
				}
			}
		}
		return null;
	}
	public ArrayList<String> getTagArray(String tagType){

		for(int i = 0; i <  tags.size(); i++){
			if(tags.get(i).getTagType().toLowerCase().equals(tagType.toLowerCase())){
				return tags.get(i).getTagValues();
			}
		}
		return null;
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		int b;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		while((b = ois.read()) != -1)
			byteStream.write(b);
		byte bitmapBytes[] = byteStream.toByteArray();
		image = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		if(image != null){
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
			byte bitmapBytes[] = byteStream.toByteArray();
			oos.write(bitmapBytes, 0, bitmapBytes.length);
		}
	}

}
