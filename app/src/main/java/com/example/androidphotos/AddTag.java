package com.example.androidphotos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddTag extends AppCompatDialogFragment {
    private EditText tagType;
    private EditText tagValue;
    Photo photo = SlideshowActivity.photo;
    private AddTagListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addtag, null);

        builder.setView(view).setTitle("Add Tags")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mTagType = tagType.getText().toString();
                        String mTagValue =tagValue.getText().toString();


                        if(!mTagType.isEmpty() && !mTagValue.isEmpty()){
                            if(mTagType.toLowerCase().equals("person") || mTagType.toLowerCase().equals("location")){
                                photo.addTagValue(mTagType,mTagValue);
                                MainActivity.albumlist.save(getContext());
                                listener.UpdateTags();
                                SlideshowActivity.locationAdapter.notifyDataSetChanged();
                                SlideshowActivity.personAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Improper Tag Type", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        tagType = view.findViewById(R.id.add_tagType);
        tagValue = view.findViewById(R.id.add_tagValue);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddTagListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "AddTagListener not implemented.");
        }
    }

    public interface AddTagListener{
        void UpdateTags();
    }
}
