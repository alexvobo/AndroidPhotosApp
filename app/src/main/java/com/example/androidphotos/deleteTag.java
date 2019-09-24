package com.example.androidphotos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;



import java.util.ArrayList;

public class deleteTag extends AppCompatDialogFragment {
    private ListView deleteList;
    Photo photo = SlideshowActivity.photo;
    private ArrayList<String> tagList;
    private DeleteTagListener listener;
    public deleteAdapter delAdap;
    private ListView listView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.deletetag, null);
        builder.setTitle("Select a Tag to Delete");
        tagList = tagList();
        delAdap = new deleteAdapter(getActivity(), tagList);
        listView = view.findViewById(R.id.deleteListView);
        listView.setAdapter(delAdap);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = tagList.get(position).substring(0, tagList.get(position).indexOf(":")-1);
                String value = tagList.get(position).substring(tagList.get(position).indexOf(":")+2);
                MainActivity.albumlist.albums.get(PhotosActivity.albumID).getPhoto(SlideshowActivity.photoID).deleteTagValue(type,value);
                MainActivity.albumlist.save(getContext());
                listener.UpdateTags();
                SlideshowActivity.locationAdapter.notifyDataSetChanged();
                SlideshowActivity.personAdapter.notifyDataSetChanged();
                dismiss();
            }
        });
        builder.setView(view);

        return builder.create();
    }

    private ArrayList<String> tagList(){
        ArrayList<String> tagList = new ArrayList<String>();
        if(photo.tags.isEmpty())
            return null;

        for(int i = 0; i < photo.tags.size(); i++){
            for(int j = 0; j < photo.tags.get(i).getTagValues().size(); j++){
                String temp = String.format("%s : %s", photo.tags.get(i).getTagType(), photo.tags.get(i).getTagValues().get(j));
                tagList.add(temp);
            }
        }
        return tagList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DeleteTagListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "DeleteTagListener not implemented.");
        }
    }


    public interface DeleteTagListener{
        void UpdateTags();
    }
}
