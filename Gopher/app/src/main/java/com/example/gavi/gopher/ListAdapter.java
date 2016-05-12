package com.example.gavi.gopher;

/**
 * Created by wyao on 16-04-27.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class ListAdapter extends ArrayAdapter<ListItem> {

    int resource;

    public ListAdapter(Context ctx, int res, List<ListItem> items)
    {
        super(ctx, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        ListItem currentList = getItem(position);


        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }


        TextView title = (TextView) itemView.findViewById(R.id.food_title);
        title.setText(currentList.getTitle());

        TextView chefName = (TextView) itemView.findViewById(R.id.chef_name);
        chefName.setText(currentList.getChefName());

        TextView price = (TextView) itemView.findViewById(R.id.price);
        price.setText(Double.toString(currentList.getPrice()));

        TextView address = (TextView) itemView.findViewById(R.id.address);
        address.setText(currentList.getAddress());

//        TextView rating = (TextView) itemView.findViewById(R.id.rating);
//        rating.setText(Double.toString(currentList.getRating()));

        return itemView;


    }
}
