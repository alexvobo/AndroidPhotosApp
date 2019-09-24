package com.example.androidphotos;

import android.content.Intent;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    static ArrayList<Photo> matches = new ArrayList<>();
    Boolean person_okay = false, location_okay = false;
    static ImageAdapter imgAdap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");

        final TextInputEditText input_person = (TextInputEditText) findViewById(R.id.input_person);
        final TextInputLayout layout_person = (TextInputLayout) findViewById(R.id.layout_person);

        final TextInputLayout layout_location = (TextInputLayout) findViewById(R.id.layout_location);
        final TextInputEditText input_location = (TextInputEditText) findViewById(R.id.input_location);

        GridView gridView = (GridView)findViewById(R.id.gridview);
        imgAdap = new ImageAdapter(this, matches);
        gridView.setAdapter(imgAdap);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, SlideshowActivity.class);
                intent.putExtra("searchPhotoID", Integer.toString(matches.get(position).getAlbumID()));
                //Start details activity
                startActivity(intent);
                imgAdap.notifyDataSetChanged();
            }
        });
        input_person.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() != 0){
                    layout_person.setError(null);
                    person_okay = true;
                } else {
                    person_okay = false;
                }
            }
        });

        input_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() != 0)
                {
                    layout_location.setError(null);
                    location_okay = true;
                } else {
                    location_okay = false;

                }

            }
        });


        final Button search = findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matches.clear();
                imgAdap.notifyDataSetChanged();

                String person = "", location = "";
                if (person_okay || location_okay){
                    if (person_okay){
                        person = Objects.requireNonNull(input_person.getText()).toString();

                    }
                    if (location_okay){
                        location = Objects.requireNonNull(input_location.getText()).toString();

                    }

                    search(person,location);
                    hideSoftKeyboard();
                    imgAdap.notifyDataSetChanged();
                }
            }
        });

    }

    public void search(String person, String location) {
        boolean personExists = false, locationExists = false, personMatched = false;

        if (!person.equals("")) {
            personExists = true;
        }
        if (!location.equals("")) {
            locationExists = true;
        }
        if (!personExists && !locationExists) {
            throw new IllegalArgumentException("Both tags null. Cannot complete search.");
        }

        for (int i = 0; i < MainActivity.albumlist.albums.size(); i++) {
            //for all albums
            for (int j = 0; j < MainActivity.albumlist.albums.get(i).getCount(); j++) {
                //for all pictures in album
                Photo photo = MainActivity.albumlist.albums.get(i).getPhoto(j);
                ArrayList<Tags> tags = photo.getTags();
                if (personExists) {
                    //person
                    Tags personTag = tags.get(0);
                    for (int k = 0; k < personTag.getTagValues().size(); k++) {
                        if (personTag.getTagValues().get(k).toLowerCase().startsWith(person.toLowerCase())) {
                            matches.add(photo);
                            personMatched = true;
                        }
                    }
                }
                if (locationExists) {
                    //location
                    Tags locationTag = tags.get(1);
                    for (int k = 0; k < locationTag.getTagValues().size(); k++) {
                        if (locationTag.getTagValues().get(k).toLowerCase().startsWith(location.toLowerCase())) {
                            //If photo has not already been added for person tag match, add it for location
                            if (!personMatched) {
                                matches.add(photo);
                            }
                        }
                    }
                }

            }
        }
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}