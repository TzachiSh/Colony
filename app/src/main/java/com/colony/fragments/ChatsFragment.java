package com.colony.fragments;


import android.app.Dialog;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.colony.adapter.FirebaseAdapter;
import com.colony.helper.Contract;
import com.colony.model.Chat;
import com.colony.activity.ChatActivity;
import com.colony.helper.MySingleton;
import com.colony.R;
import com.colony.model.Contact;
import com.colony.model.ServerIp;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ChatsFragment extends ListFragment {


    Chat chat;
    String server_url, chatName, chatMessage, chatNumberId ,userNumberApp;
    String groupId,listViewSize;
    FloatingActionButton floatingActionButton;
    Boolean isGroup;
    FirebaseAdapter LoadDatabase;
    static FirebaseListAdapter<Chat> ChatFbListAdapter;
    ArrayList<String> phoneList;
    Intent intent;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats_list, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userNumberApp = preferences.getString(Contract.Shared_User_Number, "");

        LoadDatabase = new FirebaseAdapter(getActivity());

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.myFAB);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddNewChat();
            }
        });

        //Load database
        ChatFbListAdapter = LoadDatabase.ChatAdapter(getActivity());
        setListAdapter(ChatFbListAdapter);
        listViewSize =String.valueOf(ChatFbListAdapter.getCount());
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(Contract.ACTION_Message_CHANGED));

        return view;

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int i;
            int j = 0;
            chatMessage = intent.getStringExtra(Contract.EXTRA_Chat_Message);
            chatName = intent.getStringExtra(Contract.EXTRA_Chat_Name);
            chatNumberId = intent.getStringExtra(Contract.EXTRA_Chat_Number);
            isGroup = Boolean.valueOf(intent.getStringExtra(Contract.EXTRA_Chat_IsGroup));

            chat = new Chat(chatName, chatMessage, chatNumberId, isGroup);


            if(ChatFbListAdapter.getCount() > 0) {
                for (i = 0; i < ChatFbListAdapter.getCount(); i++) {
                    if (chatNumberId.equals(ChatFbListAdapter.getItem(i).getNumber())) {

                        ChatFbListAdapter.getRef(i).setValue(chat);

                        j = 1;

                    }

                }
                if (j == 0) {
                    LoadDatabase.getChatRef().child(listViewSize+1).setValue(chat);

                }
            }
            else {LoadDatabase.getChatRef().child(listViewSize).setValue(chat);}
            ChatFbListAdapter.notifyDataSetChanged();
        }
    };




    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        launchChatActivity(position);
    }

    private void launchChatActivity(int position) {

        Chat user = ChatFbListAdapter.getItem(position) ;

        intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Contract.EXTRA_Chat_Name, user.getTitle());
        intent.putExtra(Contract.EXTRA_Chat_Message, user.getMessage());
        intent.putExtra(Contract.EXTRA_Chat_Number,user.getNumber());
        intent.putExtra(Contract.EXTRA_Chat_IsGroup,user.getGroup());
        startActivity(intent);

    }

    private void DialogAddNewChat() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Add Group");
        dialog.setContentView(R.layout.custom_dialog_add_group);
        dialog.show();
        final EditText editText_addGroup = (EditText) dialog.findViewById(R.id.editText_nameGroup);
        Button btn_addChat = (Button) dialog.findViewById(R.id.btn_addGroup);
        ListView listView =(ListView)dialog.findViewById(R.id.contactDialogList);
        listView.setAdapter(LoadDatabase.ContactAdapter());
        phoneList = new ArrayList<String>();
        phoneList.add(userNumberApp);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.contact_phone);
                String stringPhoneView = textView.getText().toString();


                if (view.getBackground() != null && !view.getBackground().equals(parent.getBackground()))
                {
                    view.setBackground(parent.getBackground());

                    int i;
                    for (i = 0; i < phoneList.size() - 1; i++) ;
                    {
                        if (phoneList.get(i).equals(stringPhoneView)) {
                            phoneList.remove(i);
                        }
                    }
                }
                else
                {
                    view.setBackgroundColor(0xFF00FF00);
                    phoneList.add(stringPhoneView);
                }

            }
        });



        btn_addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatName = editText_addGroup.getText().toString();

                server_url = ServerIp.server + "api/Groups";
                isGroup = true;
                CreateGroup();



                dialog.cancel();

            }

        });


    }

    private void CreateGroup() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                groupId = response.toString();
                AddUserToGroup();
                if(ChatFbListAdapter.getCount() == 0)
                {
                    LoadDatabase.getChatRef().child(listViewSize).setValue(new Chat(chatName, chatMessage, groupId, isGroup));
                }
                else {
                    LoadDatabase.getChatRef().child(listViewSize+1).setValue(new Chat(chatName, chatMessage, groupId, isGroup));
                }


                ChatFbListAdapter.notifyDataSetChanged();

                //chats.add(new Chat(groupName,"last message", groupName, "",true));
                //chatAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("GroupName",chatName);
                return params;
            }


        };
        MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);

    }

    private void AddUserToGroup()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url+"/"+groupId+"/AddUser/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               Log.i("responseAddUser",response.toString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>(){}.getType();
                String json = gson.toJson(phoneList, type);

                    params.put("",json);


                return params;
            }


        };
        MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
