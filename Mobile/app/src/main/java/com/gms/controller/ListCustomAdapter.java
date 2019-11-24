package com.gms.controller;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListCustomAdapter extends BaseAdapter {

    ArrayList<String> dataList = new ArrayList<>();
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<String> dataListDir = new ArrayList<>();
    ArrayList<String> dataListDate = new ArrayList<>();

    public ListCustomAdapter(ArrayList<String> anyDataList,
                             ArrayList<String> anyImageList,
                             ArrayList<String> anyDataListDir,
                             ArrayList<String> anyDataListDate){
        this.dataList = anyDataList;
        this.imageList = anyImageList;
        this.dataListDir = anyDataListDir;
        this.dataListDate = anyDataListDate;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater myInflater = LayoutInflater.from
                    (parent.getContext());
            convertView = myInflater.inflate(R.layout.action_item, parent, false);
        }

        ImageView itemImageView = (ImageView)convertView.findViewById(R.id.imageViewItem);
        if(imageList.get(position).toString().equals("Sprinkler"))
        {
            if(dataList.get(position).toString().equals(("Start Watering")))
            {
                Picasso.with(convertView.getContext()).
                        load(imageList.get(position).toString()).fit().centerCrop().
                        placeholder(R.drawable.img_sprinkler_active).into(itemImageView);
            }
            else
            {
                Picasso.with(convertView.getContext()).
                        load(imageList.get(position).toString()).fit().centerCrop().
                        placeholder(R.drawable.img_sprinkler).into(itemImageView);
            }
        }

        if(imageList.get(position).toString().equals("Message"))
        {
            Picasso.with(convertView.getContext()).
                    load(imageList.get(position).toString()).fit().centerCrop().
                    placeholder(R.drawable.img_lcd_active).into(itemImageView);
        }
        if(imageList.get(position).toString().equals("Alarm"))
        {
            Picasso.with(convertView.getContext()).
                    load(imageList.get(position).toString()).fit().centerCrop().
                    placeholder(R.drawable.img_alarm_active).into(itemImageView);
        }

        if(imageList.get(position).toString().equals("default"))
        {
            Picasso.with(convertView.getContext()).
                    load(imageList.get(position).toString()).fit().centerCrop().
                    placeholder(R.drawable.img_console_active).into(itemImageView);
        }
//        itemImageView.setImageResource(imageList.get(position));


        TextView itemTextView = (TextView)convertView.findViewById(R.id.textViewItem);
        itemTextView.setText(dataList.get(position));

        TextView itemTextViewDir = (TextView) convertView.findViewById(R.id.textViewItemDir);
        itemTextViewDir.setText(dataListDir.get(position));

        if(dataList.get(position).equalsIgnoreCase("Dismiss Alarm")) {
            itemTextViewDir.setTextColor(Color.parseColor("#eeeeee"));
        }

        TextView itemTextViewDate = (TextView)convertView.findViewById(R.id.textViewDate);
        itemTextViewDate.setText(dataListDate.get(position));

        return convertView;
    }
}
