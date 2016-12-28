package com.colony.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
    String userNumberApp;
    /////////////////
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    /////////////////////////
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// Check if user login ////
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userNumberApp = preferences.getString(Contract.Shared_User_Number, "");

        ///if user not login validation number by Sms
        if (userNumberApp.equals("")) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.WRITE_CONTACTS,
                    android.Manifest.permission.READ_CONTACTS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS},
                    Contract.PERMISSIONS_REQUEST_R_W_SMS);
        }
        else
        {

        setTheme(R.style.TabTheme);
        setContentView(R.layout.activity_main);
        // create tool bar
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFaragmets(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFaragmets(new ContactsFragment(), "Contacts");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Contract.PERMISSIONS_REQUEST_R_W_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new LoginNumber(getApplication(), this);
                    

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}


