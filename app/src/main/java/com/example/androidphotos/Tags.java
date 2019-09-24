package com.example.androidphotos;

import java.io.Serializable;
import java.util.ArrayList;

public class Tags implements Serializable {
    private String tagType;
    private ArrayList<String> tagValue;

    public Tags(String tagType) {
        setTagType(tagType);
        this.tagValue= new ArrayList<>();
        this.tagValue.add(tagType);
    }
    public String getTagType() {
        return tagType;
    }
    public void setTagType(String tagType) {
        if (tagType.toLowerCase().equals("person") || tagType.toLowerCase().equals("location")){
            this.tagType = tagType;
        } else{
            throw new IllegalArgumentException("Invalid tag type");
        }
    }
    public ArrayList<String> getTagValues() {
        return tagValue;
    }
    public String getFirstTag() {
        if(tagValue.size() != 0){
            return tagValue.get(0);
        } else{
            return null;
        }
    }
    public void addTagValue(String tagValue) {
        this.tagValue.add(tagValue);
    }
    public void deleteTagValue(String tagValue) {
        this.tagValue.remove(tagValue);
    }

    public String toString() {
        return  String.format("(%s, %s)", this.tagType,this.tagValue);
    }

}
