package com.example.androidphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.support.design.widget.FloatingActionButton;
import android.net.Uri;
import java.io.File;
import android.database.Cursor;
import android.content.ContentResolver;
import android.content.Context;

public class PhotosActivity extends AppCompatActivity {
    static Album selectedAlbum;
    public static ImageAdapter imgAdap;
    private Uri tempURI;
    private GridView gridView;
    private Bitmap bitmap;
    static int albumID;
    private static final int PICK_IMAGE_REQUEST = 1;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Photos");

        String s;
        if ((s = getIntent().getStringExtra("albumID")) != null) {
            Log.e("test",s);
            albumID = Integer.parseInt(s);

            gridView = (GridView)findViewById(R.id.gridview);
            imgAdap = new ImageAdapter(this, MainActivity.albumlist.albums.get(albumID).getPhotos());
            gridView.setAdapter(imgAdap);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(PhotosActivity.this, SlideshowActivity.class);
                    intent.putExtra("photoID", Integer.toString(position));
                    //Start details activity
                    startActivity(intent);
                }
            });

        } else {
            finish();
            //continue;
        }


        FloatingActionButton fab = findViewById(R.id.albums_menu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDialog();
            }
        });


    }
    private void photoDialog() {
        openFileExplorer();
        imgAdap.notifyDataSetChanged();
    }

    private void openFileExplorer(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
        gridView.setAdapter(imgAdap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
           Uri imgUri = data.getData();

           ImageView im = new ImageView(this);

           im.setImageURI(imgUri); //imageview is set to the uri

            BitmapDrawable drawable = (BitmapDrawable) im.getDrawable();
            Bitmap selectedImage = drawable.getBitmap(); //convert the image view to a bitmap

            File f = new File(imgUri.getPath());
            String path = f.getAbsolutePath();
            String filename = pathToFileName(imgUri);

            Photo tempPhoto = new Photo(filename);
            tempPhoto.setImage(selectedImage);  //assign the bitmap to the photo object

            tempPhoto.setCaption(filename);
            tempPhoto.setAlbumID(albumID);
            tempPhoto.setPhotoID(MainActivity.albumlist.albums.get(albumID).getCount());
            MainActivity.albumlist.albums.get(albumID).addPhoto(tempPhoto);

            if ( MainActivity.albumlist.albums.get(PhotosActivity.albumID).getCount() == 1){
                MainActivity.albumAdapter.notifyDataSetChanged();
            }
            MainActivity.albumlist.save(this);
            imgAdap.notifyDataSetChanged();

        }

    }

    private String pathToFileName(Uri selectedImage){


        String filename = "not found";

        String[] column = {MediaStore.MediaColumns.DISPLAY_NAME};

        ContentResolver contentResolver = getApplicationContext().getContentResolver();

        Cursor cursor = contentResolver.query(selectedImage, column,
                null, null, null);

        if(cursor != null) {
            try {
                if (cursor.moveToFirst()){
                    filename = cursor.getString(0);
                }
            } catch (Exception e){

            }
        }

        return filename;

    }

}

