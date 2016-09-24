package com.colony.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.colony.R;
import com.colony.helper.Contract;

public class ChatActivity extends AppCompatActivity {

    EditText SendMessage ;
    TextView GetMessage;
    Button Send_btn;
    String server_url , user ,get_message, send_message ;
    public static String receiver_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        receiver_name = intent.getStringExtra(Contract.EXTRA_Chat_Name);
        setTitle("Chat with "+ receiver_name);






    }


}
