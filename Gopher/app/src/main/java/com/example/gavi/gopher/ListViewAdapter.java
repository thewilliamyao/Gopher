package com.example.gavi.gopher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        chefName.setText(currentList.getChefName());

        TextView price = (TextView) itemView.findViewById(R.id.price);
        price.setText(Double.toString(currentList.getPrice()));

        TextView address = (TextView) itemView.findViewById(R.id.distance);
        address.setText(currentList.getDistance() + "km");

        TextView rating = (TextView) itemView.findViewById(R.id.rating);
        rating.setText(Double.toString(currentList.getRating()));

        TextView dairy = (TextView) itemView.findViewById(R.id.textView3);
        if (currentList.getDairy()) {
            dairy.setText("•Dairy Free");
        }

        TextView gluten = (TextView) itemView.findViewById(R.id.textView2);
        if (currentList.getGluten()) {
            gluten.setText("•Gluten Free");
        }

        TextView nut = (TextView) itemView.findViewById(R.id.textView4);
        if (currentList.getNut()) {
            nut.setText("•Nut Free");
        }
        return itemView;


    }
}
