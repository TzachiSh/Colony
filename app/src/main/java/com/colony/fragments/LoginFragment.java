package com.colony.fragments;



import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.colony.helper.Contract;
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.colony.activity.MainActivity;
import com.colony.model.ServerIp;
import com.colony.helper.MySingleton;
import com.colony.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    Button log_btn;
    TextView RegText;
    EditText Log_name, Log_password;
    String log_name , log_password , token,server_url ;
    AlertDialog.Builder builder;
    RegisterFragment register ;


    public LoginFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        server_url =  ServerIp.server + "api/Users";

        register =  new RegisterFragment();
        log_btn = (Button)view.findViewById(R.id.log_btn);
        Log_name = (EditText)view.findViewById(R.id.userName);
        Log_password = (EditText)view.findViewById(R.id.password);
        RegText = (TextView)view.findViewById(R.id.regText);
        builder = new AlertDialog.Builder(this.getActivity());


        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_name = Log_name.getText().toString();
                log_password = Log_password.getText().toString();
                token = FirebaseInstanceId.getInstance().getToken();
                if(log_name.equals("")||log_password.equals(""))
                {
                    builder.setTitle("Something went wrong....");
                    builder.setMessage("Please fill all the fields...");
                    displayAlert("input_error");
                }
                else
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String code = jsonObject.getString("Code");
                                String message = jsonObject.getString("Message");
                                builder.setTitle("Server Response...");
                                builder.setMessage(message);
                                displayAlert(code);
                            }catch (JSONException e)
                            {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            builder.setTitle("Server Response...");
                            builder.setMessage("Cant connect to sever...");
                            displayAlert("");

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String ,String> params = new HashMap<String, String>();
                            params.put("UserName", log_name);
                            params.put("Password",log_password);
                            params.put("Token",token);
                            return params;
                        }
                    };
                    MySingleton.getmInstance(getActivity()).addTorequestque(stringRequest);
                }

            }

        });


        RegText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToRegFrag();

            }
        });


        return view;
    }

     private void displayAlert(final String code) {
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(code.equals("input_error"))
                            {
                                Log_password.setText("");



                            }
                            else if (code.equals("log_success"))
                            {
                                //when user login..

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(Contract.Shared_User_Login , log_name);
                                editor.apply();


                                FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ChatsFragment chatsFragment = new ChatsFragment();
                    fragmentTransaction.replace(R.id.fragment_container, chatsFragment, Contract.Fragment_Main_Replaced);
                    fragmentTransaction.commit();


                }
                else if (code.equals("log_error"))
                {
                    Log_password.setText("");
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void goToRegFrag()
    {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,register , Contract.Fragment_Main_Replaced);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
