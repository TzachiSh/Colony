package com.colony.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


import com.colony.fragments.ChatsFragment;
import com.colony.fragments.LoginFragment;

import com.colony.R;
import com.colony.helper.Contract;

public class MainActivity extends AppCompatActivity {



//////////////////
    String log_user;
/////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        log_user = preferences.getString(Contract.Shared_User_Login, "");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if (log_user.equals("")) {


            LoginFragment loginFragment = new LoginFragment();
            fragmentTransaction.replace(R.id.fragment_container, loginFragment, Contract.Fragment_Main_Replaced);
            fragmentTransaction.commit();



        }
        else
        {
            ChatsFragment chatsFragment = new ChatsFragment();
            fragmentTransaction.replace(R.id.fragment_container, chatsFragment, Contract.Fragment_Main_Replaced);
            fragmentTransaction.commit();


        }

    }


}
