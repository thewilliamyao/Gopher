package com.example.gavi.gopher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Suyi on 4/27/16.
 */
public class HumanAdapter extends ArrayAdapter<Human> {
    int resource;

    public HumanAdapter(Context ctx, int res, List<Human> items)
    {
        super(ctx, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        Human currentList = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        TextView name = (TextView) itemView.findViewById(R.id.user_name);
        name.setText(currentList.getName());

        TextView price = (TextView) itemView.findViewById(R.id.address);
        price.setText(currentList.getAddress());

        TextView address = (TextView) itemView.findViewById(R.id.distance);
        address.setText(currentList.getDistance());

        TextView rating = (TextView) itemView.findViewById(R.id.rating);
        rating.setText(currentList.getEmail());

//        TextView dairy = (TextView) itemView.findViewById(R.id.dairy);
//        if (currentList.getDairy()) {
//            dairy.setText("•Dairy Free");
//        }
//
//        TextView gluten = (TextView) itemView.findViewById(R.id.glut);
//        if (currentList.getGluten()) {
//            gluten.setText("•Gluten Free");
//        }
//
//        TextView nut = (TextView) itemView.findViewById(R.id.nut);
//        if (currentList.getNut()) {
//            nut.setText("•Nut Free");
//        }


        return itemView;


    }
}
