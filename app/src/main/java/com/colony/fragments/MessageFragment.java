package com.colony.fragments;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.colony.R;
import com.colony.helper.Contract;
import com.colony.adapter.MessageAdapter;
import com.colony.helper.MySingleton;
import com.colony.model.Message;
import com.colony.model.ServerIp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    //RecyclerView objects
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //ArrayList of messages to store the thread messages
    private ArrayList<Message> messages;

    //the id of the log_user
    int userNumber = -1;
    String get_message, sender_name, log_number, receiverName, snd_message, date_time;

    EditText Snd_Message;
    Button Snd_btn;


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        //Initializing recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Initializing message arrayList
        messages = new ArrayList<>();
        adapter = new MessageAdapter(getActivity(), messages, userNumber);
        recyclerView.setAdapter(adapter);

        //Initializing send message
        Snd_Message = (EditText) view.findViewById(R.id.editTextMessage);
        Snd_btn = (Button) view.findViewById(R.id.btn_send);

        // load the user name login
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        log_number = preferences.getString(Contract.Shared_User_Number, "");

        // on send message...
        Snd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                snd_message = Snd_Message.getText().toString();
                userNumber = -1;
                sendMessage(snd_message);
                addMessage(userNumber, snd_message, date_time, "You");

            }
        });

        //get Data...
        Intent intent = getActivity().getIntent();
        receiverName = intent.getStringExtra(Contract.EXTRA_Chat_Number);


        // on Receive message...
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(Contract.ACTION_Message_CHANGED));

        return view;
    }


    // on Receive message...
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            get_message = intent.getStringExtra(Contract.EXTRA_Chat_Message);
            sender_name = intent.getStringExtra(Contract.EXTRA_Chat_Name);
            date_time = intent.getStringExtra(Contract.EXTRA_Chat_Date);
            userNumber = intent.getIntExtra(Contract.EXTRA_Chat_Number, -1);

            ///the number need to change!!!!!!!!!!!
            addMessage(userNumber, get_message, date_time, sender_name);


        }

    };

    private void addMessage(int userId, String message, String date, String senderName) {

        messages.add(new Message(userId, message, date, senderName));
        scrollToBottom();


    }

    public void sendMessage(final String message) {
        //Get date time
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        date_time = df.format(Calendar.getInstance().getTime());

        //set ip server
        String server_url = ServerIp.server + "api/Messages";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("SenderName", log_number);
                params.put("ReceiverName", receiverName);
                params.put("body", message);
                return params;

            }
        };
        MySingleton.getmInstance(getActivity()).addTorequestque(stringRequest);

    }

    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
    }


}
