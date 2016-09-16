package test1.colony;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    EditText SendMessage , ReceiverName;
    TextView GetMessage;
    Button Send_btn;
    String server_url ,message , receiverName , user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();

        server_url = ServerIp.server +"api/Messages";

        SendMessage = (EditText)findViewById(R.id.post_text);
        GetMessage = (TextView)findViewById(R.id.get_text);
        Send_btn = (Button)findViewById(R.id.send_btn);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        user = preferences.getString("Name", "");
        receiverName = intent.getStringExtra(MainActivity.EXTRA_Chat_Name);
        GetMessage.setText(intent.getStringExtra(MainActivity.EXTRA_Chat_Message));


        Send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = SendMessage.getText().toString();

                sendMessage(message ,receiverName);
            }
        });





    }

    public void sendMessage(final String message , final String receiverName)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("SenderName", user);
                params.put("ReceiverName", receiverName);
                params.put("body",message);
                return params;

            }
        };
        MySingleton.getmInstance(ChatActivity.this).addTorequestque(stringRequest);

    }


}
