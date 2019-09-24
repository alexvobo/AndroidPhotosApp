package com.example.androidphotos;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;

public class deleteAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<String> tags;

    public deleteAdapter(Context context, ArrayList<String> tagsList){
        this.mContext = context;
        this.tags = tagsList;
    }


    @Override
    public int getCount() {
        return tags.size();
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        final String tag = tags.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_deletetag, null);
        }

        final TextView textView = (TextView)convertView.findViewById(R.id.delete_tag_value);

        textView.setText(tag);

        return convertView;
    }
}
