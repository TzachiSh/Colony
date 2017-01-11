package com.colony.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.colony.R;
import com.colony.helper.Contract;
import com.colony.helper.MySingleton;
import com.colony.model.Chat;
import com.colony.model.CheckList;
import com.colony.model.Contact;
import com.colony.model.ServerIp;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class FirebaseAdapter {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String userNumberApp;
    private Activity activity;

    private DatabaseReference ChatRef;
    private DatabaseReference ContactRef;
    private DatabaseReference MessageRef;
    private DatabaseReference CheckListRef;

    public FirebaseAdapter(Activity activity) {
        this.activity = activity;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        userNumberApp = preferences.getString(Contract.Shared_User_Number, "");

        ChatRef = database.getReferenceFromUrl("https://colonly-1325.firebaseio.com/Users/" + userNumberApp + "/Chats");
        ContactRef = database.getReferenceFromUrl("https://colonly-1325.firebaseio.com/Users/" + userNumberApp + "/Contacts");
        MessageRef = database.getReferenceFromUrl("https://colonly-1325.firebaseio.com/Users/" + userNumberApp + "/Messages/");
        CheckListRef = database.getReferenceFromUrl("https://colonly-1325.firebaseio.com/List/");
    }

    public DatabaseReference getChatRef() {
        return ChatRef;
    }

    public DatabaseReference getContactRef() {
        return ContactRef;
    }

    public DatabaseReference getMessageRef() {
        return MessageRef;
    }
    public DatabaseReference getCheckListRef() {
        return CheckListRef;
    }

    public FirebaseListAdapter<Chat> ChatAdapter(Activity activity) {
        FirebaseListAdapter<Chat> ChatFbListAdapter = new FirebaseListAdapter<Chat>(activity, Chat.class, R.layout.custom_chat_list_row, ChatRef) {
            @Override
            protected void populateView(View view, Chat myObj, int position) {
                //Set the value for the views
                ((TextView) view.findViewById(R.id.listItemDialogueTitle)).setText(myObj.getTitle());
                ((TextView) view.findViewById(R.id.listItemDialogueBody)).setText(myObj.getMessage());
                //...
            }
        };
        return ChatFbListAdapter;


    }

    public FirebaseListAdapter<Contact> ContactAdapter() {

        FirebaseListAdapter<Contact> ContactFbListAdapter = new FirebaseListAdapter<Contact>(activity, Contact.class, R.layout.custom_contacts_list, ContactRef) {
            @Override
            protected void populateView(View view, Contact myObj, int position) {
                //Set the value for the views
                ((TextView) view.findViewById(R.id.contact_name)).setText(myObj.getName());
                ((TextView) view.findViewById(R.id.contact_phone)).setText(myObj.getNumber());
                //...
            }
        };

        return ContactFbListAdapter;
    }

    public FirebaseListAdapter<CheckList> CheckListAdapter(final String groupId) {
        FirebaseListAdapter<CheckList> CheckListFbListAdapter = new FirebaseListAdapter<CheckList>(activity, CheckList.class, R.layout.custom_check_list, CheckListRef.child(groupId)) {
            @Override
            protected void populateView(final View v, final CheckList model, final int position) {
                ((TextView) v.findViewById(R.id.checkName)).setText(model.getName());
                TextView checkUser = ((TextView) v.findViewById(R.id.checkUser));
                checkUser.setText(model.getUserUpdate());

                CheckBox checkBox = ((CheckBox) v.findViewById(R.id.checkBox));
                checkBox.setChecked(model.isChecked());
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(buttonView.isPressed())
                        {
                            if (isChecked) {
                                CheckListRef.child(groupId).child("" + position).child("checked").setValue(true);
                                CheckListRef.child(groupId).child("" + position).child("userUpdate").setValue("Done by: " + userNumberApp);
                                sendMessage("i Done :" + model.getName(), groupId);


                            } else {
                                CheckListRef.child(groupId).child("" + position).child("checked").setValue(false);
                                CheckListRef.child(groupId).child("" + position).child("userUpdate").setValue("Disable by: " + userNumberApp);
                                sendMessage("i unCheck :" + model.getName(), groupId);
                            }
                        }
                    }
                });

                if(model.getCount() == 0)
                {
                    v.findViewById(R.id.checkCount).setVisibility(View.GONE);
                }
                else {
                    ((TextView) v.findViewById(R.id.checkCount)).setText("X "+String.valueOf(model.getCount()));
                }


            }
        };

        return CheckListFbListAdapter;
    }
    private void sendMessage(final String message , final String groupId) {

        //set ip server
        String server_url = ServerIp.server + "api/Messages/"+true;

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
                params.put("ReceiverNumber", groupId);
                params.put("body", message);
                return params;

            }
        };
        MySingleton.getInstance(activity).addToRequestque(stringRequest);

    }
}

