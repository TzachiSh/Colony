package layout;


import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ListView;


import java.util.ArrayList;

import test1.colony.ChatActivity;
import test1.colony.MainActivity;
import test1.colony.User;
import test1.colony.UserAdapter;


public class ChatsListFragment extends ListFragment {


   private ArrayList<User> users;
   UserAdapter  userAdapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        users  = new ArrayList<>();

        userAdapter = new UserAdapter(getActivity(),users);





        setListAdapter(userAdapter);

        //getListView().setDivider(ContextCompat.getDrawable(getActivity(),android.R.color.black));
       // getListView().setDividerHeight(1);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(MainActivity.ACTION_Message_CHANGED));







    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        launchChatActivity(position);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int i;
            int j =0;
            String message = intent.getStringExtra(MainActivity.EXTRA_Chat_Message);
            String title = intent.getStringExtra(MainActivity.EXTRA_Chat_Name);
            for(i= 0 ; i<users.size();i++ )
            {
                if (title == users.get(i).getTitle())
                {
                    users.set(i,new User(title,message));
                    j=1;


                }

            }
            if(j==0) {
                users.add(new User(title, message));
            }


            userAdapter.notifyDataSetChanged();


        }

    };



    private void launchChatActivity(int position)
    {
        User user = (User) getListAdapter().getItem(position);

        Intent intent =  new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(MainActivity.EXTRA_Chat_Name, user.getTitle());
        intent.putExtra(MainActivity.EXTRA_Chat_Message, user.getMessage());
        startActivity(intent);

    }

}
