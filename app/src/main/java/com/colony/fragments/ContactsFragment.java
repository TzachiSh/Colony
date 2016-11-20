package com.colony.fragments;


import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.colony.helper.FixPhoneNumber;
import com.colony.model.ServerIp;
import com.google.gson.Gson;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.colony.R;
import com.colony.adapter.ContactAdapter;
import com.colony.helper.MySingleton;
import com.colony.model.Contact;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ContactsFragment extends ListFragment {
    ArrayList<Contact> arrayList_Android_Contacts;
    ContactAdapter contactAdapter;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        fp_get_android_Contacts();

        return view;

    }

    public void fp_get_android_Contacts() {
//----------------< fp_get_Android_Contacts() >----------------
        ArrayList<Contact> arrayList_Android_Contacts = new ArrayList<>();

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

                // Add the contact to the ArrayList
                arrayList_Android_Contacts.add(android_contact);
            }
            //----</ @Loop: all Contacts >----

            SendContactsToServer(arrayList_Android_Contacts);

            //< show results >
            ContactAdapter adapter = new ContactAdapter(getActivity(), arrayList_Android_Contacts);
            setListAdapter(adapter);

            adapter.notifyDataSetChanged();
            //</ show results >


        }
        //----</ Check: hasContacts >----

    // ----------------</ fp_get_Android_Contacts() >----------------
    }


    private void SendContactsToServer(final ArrayList<Contact> arrayList_Android_Contacts) {
        String jsonString =new Gson().toJson(arrayList_Android_Contacts);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, ServerIp.server +"api/user/contact", (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                String jsonString =new Gson().toJson(arrayList_Android_Contacts);

                params.put("Contacts",jsonString);


                return params;

            }


        };
        MySingleton.getmInstance(getActivity()).addTorequestque(jsonArrayRequest);



    }



}