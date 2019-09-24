package com.example.androidphotos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SlideshowActivity extends AppCompatActivity implements AddTag.AddTagListener , deleteTag.DeleteTagListener{
    Album album;
    static Photo photo; //this will probably need to be changed
    static public int photoID;
    private ImageView img;
    private TextView photoName;
    private ListView person;
    private ListView location;
    static ArrayAdapter<String> personAdapter, locationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slideshow);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Slideshow");

        photoName = (TextView)findViewById(R.id.photoName);

        person = (ListView)findViewById(R.id.peopleView);
        location = (ListView)findViewById(R.id.locationView);

        String s;
        img = (ImageView) findViewById(R.id.imgView);
        if ((s = getIntent().getStringExtra("searchPhotoID")) != null){
            Album search = new Album("SearchAlbum");
            search.setPhotos(SearchActivity.matches);
            album = search;
            photoID = Integer.parseInt(s);
            photo = search.getPhoto(photoID);
            displayPic(photo);
        } else if ((s = getIntent().getStringExtra("photoID")) != null) {
            album =  MainActivity.albumlist.albums.get(PhotosActivity.albumID);
            photoID = Integer.parseInt(s);
            photo = album.getPhoto(photoID);
            displayPic(photo);
        } else {
            //go back
            finish();
        }

        Button prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIndex(photoID,'-')){
                    //load prev photo + tags
                    photoID = photoID-1;
                    photo = album.getPhoto(photoID);
                    displayPic(photo);
                }
            }
        });
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIndex(photoID,'+')){
                    //load next photo + tags
                    photoID = photoID+1;
                    photo = album.getPhoto(photoID);
                    displayPic(photo);
                }
            }
        });
    }
    private void tagDialog(){
        String[] values = {"Add Tag", "Delete Tag"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SlideshowActivity.this);
        builder.setTitle("Select Your Choice");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item){
                switch(item){
                    case 0:
                        addTagDialog();
                        break;
                    case 1:
                        deleteTagDialog();
                        break;
                }
                dialog.dismiss();
            }

        });
        AlertDialog alertDialog1 = builder.create();
        alertDialog1.show();
    }

    private void addTagDialog(){
        AddTag addTag = new AddTag();
        addTag.show(getSupportFragmentManager(), "Add Tag");
    }

    private void deleteTagDialog(){
        deleteTag deletetag = new deleteTag();
        deletetag.show(getSupportFragmentManager(), "Delete Tag");
    }

    private void displayPic(Photo displayPhoto) {
        try {
            photo = displayPhoto;
            personAdapter = new ArrayAdapter<String>(SlideshowActivity.this, android.R.layout.simple_list_item_1, photo.getTagArray("person"));
            locationAdapter = new ArrayAdapter<String>(SlideshowActivity.this,android.R.layout.simple_list_item_1, photo.getTagArray("location"));
            person.setAdapter(personAdapter);
            location.setAdapter(locationAdapter);
            photoName.setText(photo.getName());
            UpdateTags();
            img.setImageBitmap(photo.getImage());
        } catch (NullPointerException e){
            Log.getStackTraceString(e);
        }

    }
    private boolean checkIndex(int index, char op){
        int temp;
        boolean tf = false;
        switch(op){
            case '+':
                temp = index + 1;
                if (temp < album.getCount()){
                    tf = true;
                }
                break;
            case '-':
                temp = index - 1;
                if (temp >= 0){
                    tf = true;
                }
                break;
        }
        return tf;
    }

    @Override
    public void UpdateTags() {
        personAdapter.notifyDataSetChanged();
        locationAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Delete) {
            MainActivity.albumlist.albums.get(PhotosActivity.albumID).deletePhoto(photo);
            PhotosActivity.imgAdap.notifyDataSetChanged();
            MainActivity.albumAdapter.notifyDataSetChanged();
            finish();
        } else if (id == R.id.EditTags){
            tagDialog();
        } else if (id == R.id.MovePhoto){
            moveDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    private void moveDialog(){
        ArrayList<String> albumList = new ArrayList<>();

        for (Album a : MainActivity.albumlist.albums){
            albumList.add(a.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(SlideshowActivity.this);

        builder.setItems(albumList.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                int oldID = photo.getAlbumID();
                photo.setAlbumID(index);
                MainActivity.albumlist.albums.get(index).addPhoto(photo);

                MainActivity.albumlist.albums.get(oldID).deletePhoto(photo);
                PhotosActivity.imgAdap.notifyDataSetChanged();
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog dialogMove = builder.create();
        dialogMove.show();
    }
}