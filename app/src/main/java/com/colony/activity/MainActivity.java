package com.colony.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.colony.R;
import com.colony.adapter.ViewPagerAdapter;
import com.colony.fragments.ChatsFragment;
import com.colony.fragments.ContactsFragment;
import com.colony.helper.Contract;
import com.colony.helper.LoginNumber;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String log_Number;
    /////////////////
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    /////////////////////////
    public static boolean cancelValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// Check if user login ////
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        log_Number = preferences.getString(Contract.Shared_User_Number, "");
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_CONTACTS}, 1);
        ///if user not login validation number by Sms
        if (log_Number.equals("")) {
            new LoginNumber(getApplication(), this);

        } else {
            setTheme(R.style.TabTheme);
            setContentView(R.layout.activity_main);
            // create tool bar
            toolbar = (Toolbar) findViewById(R.id.toolBar);
            setSupportActionBar(toolbar);
            // create tab layout fragment
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
            viewPagerAdapter.addFaragmets(new ChatsFragment(), "Chats");
            viewPagerAdapter.addFaragmets(new ContactsFragment(), "Contacts");
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);

        }


    }


}


