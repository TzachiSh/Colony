package com.colony.fragments;


import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.colony.activity.ChatActivity;
import com.colony.helper.Contract;
import com.colony.helper.FixPhoneNumber;
import com.colony.model.Chat;
import com.colony.model.ServerIp;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.transition.CircularPropagation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.colony.R;
import com.colony.adapter.ContactAdapter;
import com.colony.helper.MySingleton;
import com.colony.model.Contact;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsFragment extends ListFragment {
    ArrayList<Contact> arrayList_Android_Contacts ;
    ContactAdapter contactAdapter;

    public ContactsFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        new loadContactsAsync().execute("my string paramater");

        return view;

    }
    private class loadContactsAsync extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            String myString = params[0];

            // Do something that takes a long time, for example:
            fp_get_android_Contacts();

            // Do things

            // Call this to update your progress

            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }

    private void fp_get_android_Contacts() {
//----------------< fp_get_Android_Contacts() >----------------
        arrayList_Android_Contacts = new ArrayList<>() ;

        //--< get all Contacts >--
        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = getActivity().getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }
        //--</ get all Contacts >--

        //----< Check: hasContacts >----
        if (cursor_Android_Contacts.getCount() > 0) {
        //----< has Contacts >----
        //----< @Loop: all Contacts >----
            while (cursor_Android_Contacts.moveToNext()) {
        //< init >
                Contact android_contact = new Contact();
                String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        //</ init >

        //----< set >----
                android_contact.setName(contact_display_name);
                android_contact.setContactId(Integer.parseInt(contact_id));


        //----< get phone number >----
                int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{contact_id}
                            , null);

                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                        //< set >.
                        android_contact.setNumber(FixPhoneNumber.fixPhoneNumber(getActivity(), phoneNumber));
                        //</ set >
                    }
                    phoneCursor.close();
                }
                //----</ set >----
                //----</ get phone number >----

                arrayList_Android_Contacts.add(android_contact);

            }
            //----</ @Loop: all Contacts >----


            SendContactsToServer(arrayList_Android_Contacts);


            //< show results >

            //</ show results >


        }
        //----</ Check: hasContacts >----

    // ----------------</ fp_get_Android_Contacts() >----------------
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        launchChatActivity(position);
    }

    private void SendContactsToServer(final ArrayList<Contact> arrayList_Android_Contacts) {
                StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, ServerIp.server +"api/user/contact",  new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Contact>>(){}.getType();
                ArrayList<Contact> contactsFromServer = gson.fromJson(response,type);

                ContactAdapter adapter = new ContactAdapter(getActivity(), contactsFromServer);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }

        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Contact>>(){}.getType();
                        String json = gson.toJson(arrayList_Android_Contacts, type);
                        //params.put("Content-Type", "application/json; charset=utf-8");
                        params.put("",json);
                        return params;
                    }


        };
        MySingleton.getInstance(getActivity()).addToRequestque(jsonObjectRequest);

    }

    private void launchChatActivity(int position) {
        Contact contact = (Contact) getListAdapter().getItem(position);

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Contract.EXTRA_Chat_Name, contact.getNumber());
        intent.putExtra(Contract.EXTRA_Chat_Message, "Send a Message!");
        intent.putExtra(Contract.EXTRA_Chat_Number, contact.getNumber());
        startActivity(intent);

    }




}