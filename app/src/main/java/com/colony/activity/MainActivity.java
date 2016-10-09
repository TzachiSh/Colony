package com.colony.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.colony.fragments.ChatsFragment;

import com.colony.R;
import com.colony.helper.Contract;
import com.colony.helper.MySingleton;
import com.colony.model.ServerIp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.thrivecom.ringcaptcha.RingcaptchaApplication;
import com.thrivecom.ringcaptcha.RingcaptchaApplicationHandler;
import com.thrivecom.ringcaptcha.RingcaptchaVerification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
//////////////////
    String token,server_url,phoneNumber,log_Number;
    int countryCode;
    long nationalNumber;
/////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        log_Number = preferences.getString(Contract.Shared_User_Number,"");
        ///////////////////

        //check if user login
        if(log_Number.equals(""))
        {
            validNumber();
        }
        else
        {
            loadChatsFragments();
        }

    }

    private void validNumber()
    {
        RingcaptchaApplication.onboard(getApplicationContext(), "u3ynunuqysuza1a2i4oh", "ysojanu2y9ocunifabi3", new RingcaptchaApplicationHandler() {
            @Override
            public void onSuccess(RingcaptchaVerification ringcaptchaVerification) {
                phoneNumber = ringcaptchaVerification.getPhoneNumber();

                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                try {
                    // phone must begin with '+'
                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, "");
                    // get the number of phone in long
                    nationalNumber = numberProto.getNationalNumber();
                    //get the Country code in int
                    countryCode = numberProto.getCountryCode();
                    // save the full number in string
                    log_Number = "+"+Integer.toString(countryCode)+" "+Long.toString(nationalNumber);

                    logUser();

                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }
            }

            @Override
            public void onCancel() {
                Log.i("Ringcaptcha" ,"Cancel");

            }
        });

    }

    private void logUser()
    {
        token = FirebaseInstanceId.getInstance().getToken();
        server_url =  ServerIp.server + "api/Users";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("Code");
                    String message = jsonObject.getString("Message");
                    displayAlert(code);
                }catch (JSONException e)
                {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                displayAlert("");

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String  ,String> params = new HashMap<String, String>();

                params.put("Number", log_Number);
                params.put("Token",token);
                return params;
            }
        };
        MySingleton.getmInstance(getApplicationContext()).addTorequestque(stringRequest);
    }

    private void displayAlert(final String code) {
                if(code.equals("val_error"))
                {
                    //what happen if was error in the validation?

                }
                else if (code.equals("log_success")||code.equals("reg_success"))
                {
                    //when user login..
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Contract.Shared_User_Number  ,log_Number);
                    editor.apply();

                    // when Created  new user
                    if(code.equals("reg_success"))
                    {



                    }

                    loadChatsFragments();


                }
            }

    private void loadChatsFragments()
    {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChatsFragment  chatsFragment = new ChatsFragment();
        fragmentTransaction.replace(R.id.fragment_container, chatsFragment, Contract.Fragment_Main_Replaced);
        fragmentTransaction.commit();
    }

}
