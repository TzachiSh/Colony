package com.colony.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.colony.R;
import com.colony.adapter.FirebaseAdapter;
import com.colony.fragments.ContactsFragment;
import com.colony.fragments.MessageFragment;
import com.colony.helper.Contract;
import com.colony.model.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    public static String receiver_name,receiverNumber;
    Boolean isGruop ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        receiver_name = intent.getStringExtra(Contract.EXTRA_Chat_Name);
        receiverNumber = intent.getStringExtra(Contract.EXTRA_Chat_Number);
        isGruop = intent.getBooleanExtra(Contract.EXTRA_Chat_IsGroup,false);
        setTitle(receiver_name);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isGruop) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, CheckListActivity.class);
        intent.putExtra(Contract.EXTRA_Chat_Number, receiverNumber);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
