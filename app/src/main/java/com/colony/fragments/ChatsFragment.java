package com.colony.fragments;


import android.app.Dialog;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.colony.helper.Contract;
import com.colony.model.Chat;
import com.colony.activity.ChatActivity;
import com.colony.helper.MySingleton;
import com.colony.R;
import com.colony.model.ServerIp;

import com.colony.adapter.ChatAdapter;


public class ChatsFragment extends ListFragment {

    ArrayList<Chat> chats;
    String server_url ,chatNumber;
    ChatAdapter chatAdapter;
    FloatingActionButton floatingActionButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_chats_list, container, false);

        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.myFAB);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddNewChat();
            }
        });

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        chats = new ArrayList<>();
        chatAdapter = new ChatAdapter(getActivity(), chats);
        setListAdapter(chatAdapter);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(Contract.ACTION_Message_CHANGED));

    }



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int i;
            int j =0;
            String message = intent.getStringExtra(Contract.EXTRA_Chat_Message);
            String title = intent.getStringExtra(Contract.EXTRA_Chat_Name);
            String number = intent.getStringExtra(Contract.EXTRA_Chat_Number);
            for(i= 0 ; i<= chats.size()-1; i++ )
            {
                if (number.equals(chats.get(i).getNumber()))
                {
                    chats.set(i,new Chat(title,message,chatNumber));
                    j=1;

                }

            }
            if(j==0) {
               chats.add(new Chat(title, message ,chatNumber));
            }

            chatAdapter.notifyDataSetChanged();

        }

    };

    public void DialogAddNewChat ()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Add User");
        dialog.setContentView(R.layout.custom_dialog_add_chat);
        dialog.show();
        final EditText editText_addChat = (EditText)dialog.findViewById(R.id.editText_nameChat);
        Button btn_addChat = (Button)dialog.findViewById(R.id.btn_addChat);


        btn_addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatNumber = editText_addChat.getText().toString();

                server_url = ServerIp.server + "api/Users/" + chatNumber;

                FindUser();

                dialog.cancel();

            }

        });




    }

    public void FindUser()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("Code");
                    String message = jsonObject.getString("Message");
                    if(code.equals("user_success"))
                    {

                        chats.add(new Chat(message, "Send him message",chatNumber));
                        chatAdapter.notifyDataSetChanged();




                    }
                    else if (code.equals("user_error"))
                    {


                    }


                }catch (JSONException e)
                {
                    e.printStackTrace();

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){

        };
        MySingleton.getmInstance(getActivity()).addTorequestque(stringRequest);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        launchChatActivity(position);
    }

    private void launchChatActivity(int position)
    {
        Chat user = (Chat) getListAdapter().getItem(position);

        Intent intent =  new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Contract.EXTRA_Chat_Name, user.getTitle());
        intent.putExtra(Contract.EXTRA_Chat_Message, user.getMessage());
        intent.putExtra(Contract.EXTRA_Chat_Number,user.getNumber());
        startActivity(intent);

    }

}
