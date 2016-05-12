package com.example.gavi.gopher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Suyi on 4/27/16.
 */
public class ListViewAdapter extends ArrayAdapter<ListItem> {
    int resource;

    public ListViewAdapter(Context ctx, int res, List<ListItem> items)
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
        chefName.setText("Cooked by " + currentList.getChefName());

        TextView price = (TextView) itemView.findViewById(R.id.price);
        price.setText("$" + String.format("%.2f", currentList.getPrice()));

        TextView address = (TextView) itemView.findViewById(R.id.distance);
        if (currentList.getPrice() > 20) {
            address.setText("$$$");
        } else if (currentList.getPrice() > 10) {
            address.setText("$$");
        } else {
            address.setText("$");
        }
        TextView rating = (TextView) itemView.findViewById(R.id.rating);
        rating.setText(currentList.getAddress());




        return itemView;


    }
}
