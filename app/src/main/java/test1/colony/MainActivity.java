package test1.colony;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


import layout.ChatsListFragment;
import layout.Login;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_Chat_ID ="com.test1.colony.identifier"; // the id of the chat
    public static final String EXTRA_Chat_Name ="com.test1.colony.name";     // the name of the sender
    public static final String EXTRA_Chat_Message = "com.test1.colony.message"; //the message
    public static final String ACTION_Message_CHANGED = "com.test1.colony.changed"; // check if receive message


    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user = preferences.getString("Name", "");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (user.equals("")) {


            Login login = new Login();
            fragmentTransaction.replace(R.id.fragment_container, login, "try");
            fragmentTransaction.commit();
        }
        else
        {
            ChatsListFragment chatsListFragment = new ChatsListFragment();
            fragmentTransaction.replace(R.id.fragment_container, chatsListFragment, "try");
            fragmentTransaction.commit();

        }
       /* Intent intent = new Intent(this,
                ChatActivity.class);
        startActivity(intent);*/

    }
}
