package layout;



import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import test1.colony.ChatActivity;
import test1.colony.MySingleton;
import test1.colony.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    Button log_btn;
    TextView RegText;
    EditText Log_name, Log_password;
    String name , password , token ;
    String server_utl = "http://46.116.43.207/api/users";
    AlertDialog.Builder builder;
    Register register ;


    public Login() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        register =  new Register();
        log_btn = (Button)view.findViewById(R.id.log_btn);
        Log_name = (EditText)view.findViewById(R.id.userName);
        Log_password = (EditText)view.findViewById(R.id.password);
        RegText = (TextView)view.findViewById(R.id.regText);
        builder = new AlertDialog.Builder(this.getActivity());


        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Log_name.getText().toString();
                password = Log_password.getText().toString();
                token = FirebaseInstanceId.getInstance().getToken();
                if(name.equals("")||password.equals(""))
                {
                    builder.setTitle("Something went wrong....");
                    builder.setMessage("Please fill all the fields...");
                    displayAlert("input_error");
                }
                else
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_utl, new Response.Listener<String>() {
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
                            params.put("UserName",name);
                            params.put("Password",password);
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
                    editor.putString("Name",name);
                    editor.apply();

                    Intent intent = new Intent(getActivity(),
                            ChatActivity.class);
                    startActivity(intent);
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
        fragmentTransaction.replace(R.id.fragment_container,register ,"try");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
