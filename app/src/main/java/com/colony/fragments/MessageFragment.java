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
import android.util.Log;
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
import com.colony.adapter.FirebaseAdapter;
import com.colony.helper.Contract;
import com.colony.adapter.MessageAdapter;
import com.colony.helper.MySingleton;
import com.colony.model.Message;
import com.colony.model.ServerIp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MessageFragment extends Fragment {
    //RecyclerView objects
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //ArrayList of messages to store the thread messages
    private ArrayList<Message> messages;

    //the id of the log_user
    String stringUserNumber ,userNumberApp;
    String get_message, sender_name, receiverNumber, snd_message, date_time,stringGroup;
    boolean isGroup;
    int pos ;
    SharedPreferences preferences;
    EditText Snd_Message;
    Button Snd_btn;
    DatabaseReference databaseReference;


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get Data...
        Intent intent = getActivity().getIntent();
        receiverNumber = intent.getStringExtra(Contract.EXTRA_Chat_Number);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        //Initializing recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //get the number of the user
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userNumberApp = preferences.getString(Contract.Shared_User_Number, "");

        //load database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReferenceFromUrl("https://colonly-1325.firebaseio.com/Users/" +
                userNumberApp + "/Messages/"+ receiverNumber);
        loadFromFirebase();

        ////////
        isGroup = intent.getBooleanExtra(Contract.EXTRA_Chat_IsGroup,false);


        //Initializing message arrayList
        messages = new ArrayList<>();
        adapter = new MessageAdapter(getActivity(), messages, userNumberApp);
        recyclerView.setAdapter(adapter);

        //Initializing send message
        Snd_Message = (EditText) view.findViewById(R.id.editTextMessage);
        Snd_btn = (Button) view.findViewById(R.id.btn_send);



        // on send message...
        Snd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                snd_message = Snd_Message.getText().toString();
                sendMessage(snd_message);
                addMessage(userNumberApp, snd_message, date_time, "You");
                saveToFirebase();

            }
        });




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
            if (isGroup)
            {
                stringUserNumber =intent.getStringExtra(Contract.EXTRA_Chat_SenderNumber);
                sender_name = stringUserNumber;
            }
            else
            {
            stringUserNumber = intent.getStringExtra(Contract.EXTRA_Chat_Number);
            }

            addMessage(stringUserNumber, get_message, date_time, sender_name);

        }

    };

    private void addMessage(String userNumber, String message, String date, String senderName) {

        messages.add(new Message(userNumber, message, date, senderName));
        scrollToBottom();


    }

    public void sendMessage(final String message) {
        //Get date time
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        date_time = df.format(Calendar.getInstance().getTime());

        //set ip server
        String server_url = ServerIp.server + "api/Messages/"+isGroup;

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
                params.put("SenderName", userNumberApp);
                params.put("ReceiverNumber", receiverNumber);
                params.put("body", message);
                return params;

            }
        };
        MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);

    }

    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
    }

    private void saveToFirebase()
    {
        databaseReference.setValue(messages);
    }

    private void loadFromFirebase()
    {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Message>> t = new GenericTypeIndicator<ArrayList<Message>>() {};
                ArrayList<Message> yourStringArray = snapshot.getValue(t);
                messages.clear();
                if(yourStringArray!= null) {
                    messages.addAll(yourStringArray);
                    scrollToBottom();
                }

                //Toast.makeText(getActivity(),yourStringArray.get(0), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        };
        databaseReference.addValueEventListener(postListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveToFirebase();
    }
}