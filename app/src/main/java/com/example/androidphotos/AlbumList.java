package com.example.androidphotos;

import java.io.Serializable;
import java.io.*;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;

public class AlbumList implements Serializable {

    private static final long serialVersionUID = 0L;
    public static final String file = "albumlist.ser";
    public ArrayList<Album> albums;



    public static AlbumList load(Context context){
        AlbumList al = null;

        try{
            FileInputStream fs = context.openFileInput(file);
            ObjectInputStream os = new ObjectInputStream(fs);

            al = (AlbumList) os.readObject();

            if(al.albums == null)
                al.albums = new ArrayList<Album>();

            fs.close();
            os.close();

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
        return al;
    }

    public void save(Context context){
        try{
            FileOutputStream fs = context.openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(this);
            os.close();
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
