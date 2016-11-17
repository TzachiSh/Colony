package com.colony.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.colony.model.Chat;

import java.util.ArrayList;

import com.colony.R;

/**
 * Created by zahi on 11/09/2016.
 */
public class ChatAdapter extends ArrayAdapter<Chat> {
    public static class ViewHolder {
        TextView title;
        TextView message;
        ImageView userIcon;

    }

    public ChatAdapter(Context context, ArrayList<Chat> chats) {
        super(context, 0, chats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for this position
        Chat chat = getItem(position);

        //create a new viewholder
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_chat_list_row, parent, false);

            //set our views to our view holder so that we no longer have to go back and use find view
            //by id every time we have a new row
            viewHolder.title = (TextView) convertView.findViewById(R.id.listItemDialogueTitle);
            viewHolder.message = (TextView) convertView.findViewById(R.id.listItemDialogueBody);
            viewHolder.userIcon = (ImageView) convertView.findViewById(R.id.listItemDialogueImg);

            //use set tag to remember out view holder which is holding our references to our widgets
            convertView.setTag(viewHolder);

        } else {
            //we already have a view just go to our viewholder and grab the widgets from it
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.title.setText(chat.getTitle());
        viewHolder.message.setText(chat.getMessage());
        // set Img of user ...

        return convertView;


    }


}
