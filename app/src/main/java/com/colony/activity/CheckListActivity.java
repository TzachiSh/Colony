package com.colony.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.colony.R;
import com.colony.adapter.FirebaseAdapter;
import com.colony.helper.Contract;
import com.colony.model.CheckList;
import com.firebase.ui.database.FirebaseListAdapter;

public class CheckListActivity extends AppCompatActivity {
    String userNumberApp ,groupId ,name;
    int count;
    FirebaseAdapter LoadDatabase;
    FirebaseListAdapter<CheckList> CheckListAdapter;
    EditText editText_addCheck,editText_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userNumberApp = preferences.getString(Contract.Shared_User_Number, "");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddNewCheckList();
            }
        });

        Intent intent = getIntent();
        groupId = intent.getStringExtra(Contract.EXTRA_Chat_Number);

        LoadDatabase = new FirebaseAdapter(this);
        CheckListAdapter = LoadDatabase.CheckListAdapter(groupId);

        ListView listView = (ListView)findViewById(R.id.checkListView);
        listView.setAdapter(CheckListAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DialogDeleteCheckList(position);
                return false;
            }
        });

    }

    private void  DialogAddNewCheckList()
    {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Add Group");
        dialog.setContentView(R.layout.custom_dialog_add_new_check);
        dialog.show();
        editText_addCheck = (EditText)dialog.findViewById(R.id.editText_nameCheck);
        editText_count = (EditText)dialog.findViewById(R.id.editText_countCheck);
        Button button = (Button)dialog.findViewById(R.id.btn_addCheck);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = editText_addCheck.getText().toString();
                if(editText_count.getText().toString().equals(""))
                {
                    count = 0;
                }
                else
                {
                    count = Integer.valueOf(editText_count.getText().toString());

                }


                int listId = CheckListAdapter.getCount();

                CheckList checkList = new CheckList(listId,name,count,"Create by : " +userNumberApp);
                Log.i("name of checklist",checkList.getName());

                LoadDatabase.getCheckListRef().child(groupId).child(""+listId).setValue(checkList);

                dialog.cancel();
            }
        });



    }

    private void DialogDeleteCheckList(final int position)
    {

        AlertDialog alertDialog = new AlertDialog.Builder(CheckListActivity.this).create();
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("You sure you want delete this toDo?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CheckListAdapter.getRef(position).removeValue();

                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();



    }



}


