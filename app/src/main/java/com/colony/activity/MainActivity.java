package com.colony.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


import com.colony.fragments.ChatsFragment;
import com.colony.fragments.LoginFragment;

import test1.colony.R;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_Chat_ID ="com.colony.identifier"; // key id of the chat
    public static final String EXTRA_Chat_Name ="com.colony.name";     // key name of the sender
    public static final String EXTRA_Chat_Message = "com.colony.message"; //key message
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String ACTION_Message_CHANGED = "com.colony.changed"; //key check if receive message
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String Shared_User_Login ="com.colony.login"; // key check if the user login
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String Fragment_Main_Replaced = "com.colony.replaced"; //key for replaced fragment in the main activity


//////////////////
    String log_user;
/////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        log_user = preferences.getString(Shared_User_Login, "");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if (log_user.equals("")) {


            LoginFragment loginFragment = new LoginFragment();
            fragmentTransaction.replace(R.id.fragment_container, loginFragment, Fragment_Main_Replaced);
            fragmentTransaction.commit();



        }
        else
        {
            ChatsFragment chatsFragment = new ChatsFragment();
            fragmentTransaction.replace(R.id.fragment_container, chatsFragment, Fragment_Main_Replaced);
            fragmentTransaction.commit();


        }

    }


}
