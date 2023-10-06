package com.example.myphoneproject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MyContacts> list;
    private LayoutInflater inflater;

    public MyAdapter(Context context, ArrayList<MyContacts> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemHolder handler = null;
        if(view == null){
            view = inflater.inflate(R.layout.list_layout, null);
            handler = new ItemHolder();
            handler.name = view.findViewById(R.id.tvNameList);
            handler.phone = view.findViewById(R.id.tvPhoneList);
            handler.email = view.findViewById(R.id.tvEmailList);
            view.setTag(handler);
        }else handler = (ItemHolder) view.getTag();

        handler.name.setText(list.get(i).getName());
        handler.phone.setText(list.get(i).getPhone());
        handler.email.setText(list.get(i).getEmail());

        return view;
    }

    static class ItemHolder{
        TextView name, phone, email;
    }
}
