package layout;



import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


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

import test1.colony.MySingleton;
import test1.colony.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends Fragment {
    Button Reg_btn;
    EditText Reg_name, Reg_password, Reg_conPassword;
    String name, password, conPass , token;
    String server_utl = "http://46.116.43.207/api/users";
    AlertDialog.Builder builder;

    public Register() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Reg_name = (EditText)view.findViewById(R.id.userName);
        Reg_password = (EditText)view.findViewById(R.id.password) ;
        Reg_conPassword = (EditText)view.findViewById(R.id.conPassword);
        Reg_btn = (Button)view.findViewById(R.id.reg_btn);
        token = FirebaseInstanceId.getInstance().getToken();


        builder = new AlertDialog.Builder(this.getActivity());

        Reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Reg_name.getText().toString();
                password = Reg_password.getText().toString();
                conPass = Reg_conPassword.getText().toString();
                if (name.equals("") || password.equals("")||conPass.equals("")){

                    builder.setTitle("Something went wrong....");
                    builder.setMessage("Please fill all the fields...");
                    displayAlert("input_error");

                }
                else
                {
                    if(!(password.equals(conPass)))
                    {
                        builder.setTitle("Something went wrong....");
                        builder.setMessage("Your password are not matching..");
                        displayAlert("input_error");
                    }
                    else
                    {
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, server_utl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String code = jsonObject.getString("Code");
                                    String message = jsonObject.getString("Message");
                                    builder.setTitle("Server Response...");
                                    builder.setMessage(message);
                                    displayAlert(code);
                                } catch (JSONException e) {
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
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("UserName", name);
                                params.put("Password", password);
                                params.put("Token",token);
                                return params;

                            }
                        };
                        MySingleton.getmInstance(getActivity()).addTorequestque(stringRequest);

                    }
                }
            }


        });

        return view ;
    }
    public void displayAlert(final String code)
    {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code.equals("input_error"))
                {
                    Reg_password.setText("");
                    Reg_conPassword.setText("");


                }
                else if (code.equals("reg_success"))
                {
                    Login login = new Login();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,login ,"try");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if (code.equals("reg_error"))
                {
                    Reg_password.setText("");
                    Reg_conPassword.setText("");
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

}
