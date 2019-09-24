package com.example.androidphotos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.content.Context;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    //public static ArrayList<Album> userAlbums = new ArrayList<>();
    public static AlbumAdapter albumAdapter;
    public static AlbumList albumlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumlist = AlbumList.load(context);

        if(albumlist == null)
            albumlist = new AlbumList();

        if(albumlist.albums == null)
            albumlist.albums = new ArrayList<Album>();

        setContentView(R.layout.album_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView gridView = (GridView)findViewById(R.id.gridview);
        albumAdapter = new AlbumAdapter(this, albumlist.albums);
        gridView.setAdapter(albumAdapter);

        getSupportActionBar().setTitle("Albums");

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PhotosActivity.class);
                intent.putExtra("albumID", Integer.toString(position));
                //Start details activity
                startActivity(intent);
            }
        });

       FloatingActionButton fab = findViewById(R.id.albums_menu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumDialog();
            }
        });
    }
    private void albumDialog() {
        String[] values = {"Create", "Delete","Rename"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Your Choice");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    //Create has been clicked
                    case 0:
                        Album newAlbum = new Album("Untitled");
                        newAlbum.setAlbumID(albumlist.albums.size());
                        albumlist.albums.add(newAlbum);
                        albumAdapter.notifyDataSetChanged();
                        //^refreshes view
                        break;
                    //Delete has been clicked
                    case 1:
                        if (albumlist.albums.size() == 0){
                            break;
                        }
                        final ArrayList<String> nameList = new ArrayList<>();
                        for (Album a : albumlist.albums){
                            nameList.add(a.getName());
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setItems(nameList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                albumlist.albums.remove(index);
                                albumlist.save(context);
                                dialog.dismiss();
                                albumAdapter.notifyDataSetChanged();
                            }
                        });

                        AlertDialog dialogDelete = builder.create();
                        dialogDelete.show();
                        break;
                    //Rename has been clicked
                    case 2:
                        //allow selection of album, pop up dialog to get new name, rename it.
                        ArrayList<String> albumListRename = new ArrayList<>();
                        String newName = "";
                        if (albumlist.albums.size() == 0){
                            break;
                        }
                        for (Album a : albumlist.albums){
                            albumListRename.add(a.getName());
                        }

                        AlertDialog.Builder builderRename = new AlertDialog.Builder(MainActivity.this);

                        builderRename.setItems(albumListRename.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int index) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Rename");

                                final EditText input = new EditText(MainActivity.this);
                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                builder.setView(input);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String newName = input.getText().toString();
                                        albumlist.albums.get(index).setName(newName);
                                        albumlist.save(context);
                                        albumAdapter.notifyDataSetChanged();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                                dialog.dismiss();

                            }
                        });

                        AlertDialog builderRenameDialog = builderRename.create();
                        builderRenameDialog.show();
                        break;
                }
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog1 = builder.create();
        alertDialog1.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
           // intent.putExtra("albumID", Integer.toString(position));
            //Start details activity
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
