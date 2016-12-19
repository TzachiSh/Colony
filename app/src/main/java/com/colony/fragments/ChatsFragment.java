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
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.colony.helper.Contract;
import com.colony.model.Chat;
import com.colony.activity.ChatActivity;
import com.colony.helper.MySingleton;
import com.colony.R;
import com.colony.model.Contact;
import com.colony.model.ServerIp;

import com.colony.adapter.ChatAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ChatsFragment extends ListFragment {


    ArrayList<Chat> chats = null;
    String server_url,userNumberApp ,chatName,chatMessage,chatNumberId;
    Long groupId;
    ChatAdapter chatAdapter;
    FloatingActionButton floatingActionButton;
    Boolean isGroup;
    SharedPreferences preferences;
    DatabaseReference myRefChats;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats_list, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.myFAB);

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
        //get the number of the user
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userNumberApp = preferences.getString(Contract.Shared_User_Number, "");
        chats = new ArrayList<>();
        chatAdapter = new ChatAdapter(getActivity(), chats);

        //Load database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRefChats = database.getReferenceFromUrl("https://colonly-1325.firebaseio.com/Users/"+ Settings.Secure.ANDROID_ID + "/"+ userNumberApp + "/Chats");
        if(chats == null)
        {
        reloadDatabase ();
        }

        setListAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(Contract.ACTION_Message_CHANGED));




    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int i;
            int j = 0;
            chatMessage = intent.getStringExtra(Contract.EXTRA_Chat_Message);
            chatName = intent.getStringExtra(Contract.EXTRA_Chat_Name);
            chatNumberId = intent.getStringExtra(Contract.EXTRA_Chat_Number);
            isGroup = intent.getBooleanExtra(Contract.EXTRA_Chat_IsGroup, false);

            if (chats != null) {
                for (i = 0; i <= chats.size() - 1; i++) {
                    if (chatNumberId.equals(chats.get(i).getNumber())) {
                        chats.set(i, new Chat(chatName, chatMessage, chatNumberId, isGroup));
                        j = 1;

                    }

                }
                if (j == 0) {
                    chats.add(new Chat(chatName, chatMessage, chatNumberId, isGroup));

                }
                saveToDatabase();
                chatAdapter.notifyDataSetChanged();

            }
            else {

                chats.add(new Chat(chatName, chatMessage, chatNumberId, isGroup));

            }
            chatAdapter.notifyDataSetChanged();

        }

    };

    public void DialogAddNewChat() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Add Group");
        dialog.setContentView(R.layout.custom_dialog_add_group);
        dialog.show();
        final EditText editText_addGroup = (EditText) dialog.findViewById(R.id.editText_nameGroup);
        Button btn_addChat = (Button) dialog.findViewById(R.id.btn_addGroup);


        btn_addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatName = editText_addGroup.getText().toString();

                server_url = ServerIp.server + "api/Groups";

                CreateGroup();

                dialog.cancel();

            }

        });


    }

    public void CreateGroup() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                groupId = Long.parseLong( response.toString());

                //chats.add(new Chat(groupName,"last message", groupName, "",true));
                chatAdapter.notifyDataSetChanged();

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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        launchChatActivity(position);
    }

    private void launchChatActivity(int position) {
        Chat user = (Chat) getListAdapter().getItem(position);

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Contract.EXTRA_Chat_Name, user.getTitle());
        intent.putExtra(Contract.EXTRA_Chat_Message, user.getMessage());
        intent.putExtra(Contract.EXTRA_Chat_Number, user.getNumber());
        intent.putExtra(Contract.Extra_Chat_Position,position);
        startActivity(intent);

    }

    private void saveToDatabase ()
    {
        myRefChats.setValue(chats);
    }

    private void reloadDatabase ()
    {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Chat>> t = new GenericTypeIndicator<ArrayList<Chat>>() {};
                ArrayList<Chat> yourStringArray = snapshot.getValue(t);
                chats.clear();
                if(yourStringArray != null) {
                    chats.addAll(yourStringArray);
                }
                chatAdapter.notifyDataSetChanged();

                //Toast.makeText(getActivity(),yourStringArray.get(0).getNumber(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        };
        myRefChats.addValueEventListener(postListener);


    }

}
