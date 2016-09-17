package com.colony.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import com.colony.helper.MySingleton;
import test1.colony.R;
import com.colony.model.ServerIp;

public class ChatActivity extends AppCompatActivity {

    EditText SendMessage ;
    TextView GetMessage;
    Button Send_btn;
    String server_url , receiver_name, user ,get_message, send_message ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user = preferences.getString(MainActivity.Shared_User_Login, "");

        server_url = ServerIp.server +"api/Messages";

        SendMessage = (EditText)findViewById(R.id.post_text);
        GetMessage = (TextView)findViewById(R.id.get_text);
        Send_btn = (Button)findViewById(R.id.send_btn);


        get_message = intent.getStringExtra(MainActivity.EXTRA_Chat_Message);
        receiver_name = intent.getStringExtra(MainActivity.EXTRA_Chat_Name);
        GetMessage.setText(get_message);
        setTitle("Chat with "+receiver_name);


        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(MainActivity.ACTION_Message_CHANGED));

        Send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_message = SendMessage.getText().toString();

                sendMessage(send_message, receiver_name);
            }
        });





    }

    public void sendMessage(final String message , final String receiverName)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("SenderName", user);
                params.put("ReceiverName", receiverName);
                params.put("body",message);
                return params;

            }
        };
        MySingleton.getmInstance(ChatActivity.this).addTorequestque(stringRequest);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            get_message = intent.getStringExtra(MainActivity.EXTRA_Chat_Message);
            GetMessage.setText(get_message);
        }
    };



}
