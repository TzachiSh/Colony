package test1.colony;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


import layout.Login;

public class MainActivity extends AppCompatActivity {
    String user = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user = preferences.getString("Name", "");

        if (user.equals("")) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Login login = new Login();
            fragmentTransaction.replace(R.id.fragment_container, login, "try");
            fragmentTransaction.commit();
        }
        else
        {

            Intent intent = new Intent(this,
                    ChatActivity.class);
            startActivity(intent);
        }

    }
}
