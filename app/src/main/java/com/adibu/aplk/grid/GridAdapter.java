package com.adibu.aplk.grid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adibu.aplk.R;

public class GridAdapter extends ArrayAdapter<GridModel> {

    Context context;
    public GridAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View gridItemView = convertView;
        if(gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_grid, parent, false);
        }

        GridModel currentItem = getItem(position);

        TextView title = gridItemView.findViewById(R.id.grid_item_text);
        title.setText(context.getString(currentItem.getNameId()));

        ImageView image = (ImageView)gridItemView.findViewById(R.id.grid_item_image);
        image.setImageResource(currentItem.getImageId());


        return gridItemView;
    }

}
