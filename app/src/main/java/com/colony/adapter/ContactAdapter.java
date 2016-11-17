package com.colony.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.colony.R;
import com.colony.model.Contact;

import java.util.ArrayList;


public class ContactAdapter extends ArrayAdapter<Contact> {

    public static class ViewHolder {
        TextView name;
        TextView phone;
        ImageView contactIcon;
    }

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for this position
        Contact contact = getItem(position);
        ViewHolder viewHolder;
        //create new viewHolder
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_contacts_list, parent, false);
            //set our views to our view holder so that we no longer have to go back and use find view
            //by id every time we have a new row
            viewHolder.name = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.contact_phone);
            viewHolder.contactIcon = (ImageView) convertView.findViewById(R.id.contact_pic);

            //use set tag to remember out view holder which is holding our references to our widgets
            convertView.setTag(viewHolder);

        } else {
            //we already have a view just go to our viewholder and grab the widgets from it
            viewHolder = (ContactAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(contact.getName());
        viewHolder.phone.setText(contact.getNumber());
        // set Img of user ...


        return convertView;
    }
}
