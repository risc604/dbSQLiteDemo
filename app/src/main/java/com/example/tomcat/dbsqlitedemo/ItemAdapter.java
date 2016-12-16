package com.example.tomcat.dbsqlitedemo;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tomcat on 2016/12/13.
 */

public class ItemAdapter extends ArrayAdapter<Item>
{
    private int resource;
    private List<Item> items;

    public ItemAdapter(Context context, int resource, List<Item> items)
    {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout itemView;
        final Item item = getItem(position);

        if (convertView == null)
        {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, itemView, true);
        }
        else
        {
            itemView = (LinearLayout) convertView;
        }

        RelativeLayout  typeColor = (RelativeLayout) itemView.findViewById(R.id.type_color);
        ImageView       selectedItem = (ImageView) itemView.findViewById(R.id.selected_item);
        TextView        titleView = (TextView) itemView.findViewById(R.id.title_text);
        TextView        dateView = (TextView) itemView.findViewById(R.id.date_text);

        GradientDrawable background = (GradientDrawable)typeColor.getBackground();
        background.setColor(item.getColor().parseColor());

        titleView.setText(item.getTitle());
        dateView.setText(item.getLocalDatetime());

        selectedItem.setVisibility(item.isSelected() ? View.VISIBLE : View.INVISIBLE);
        return itemView;
    }

    public void set(int index, Item item)
    {
        if ((index >= 0) && (index < items.size()))
        {
            items.set(index, item);
            notifyDataSetChanged();
        }
    }

    public Item get(int index)
    {
        return items.get(index);
    }

}
