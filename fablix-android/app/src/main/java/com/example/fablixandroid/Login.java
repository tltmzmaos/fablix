package com.example.fablixandroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fablixandroid.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends ActionBarActivity {

    private EditText username;
    private EditText password;
    private TextView message;
    private Button loginButton;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // upon creation, inflate and initialize the layout
        setContentView(R.layout.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        message = findViewById(R.id.message);
        loginButton = findViewById(R.id.login);
        /**
         * In Android, localhost is the address of the device or the emulator.
         * To connect to your machine, you need to use the below IP address
         * **/
        //url = "https://10.0.2.2:8443/cs122b_spring20_project2_login_cart_example_war/api/";
        //url = "https://10.0.2.2:8443/fablix_pj_war_exploded/";
        url = "https://ec2-13-56-236-95.us-west-1.compute.amazonaws.com:8443/fablix-pj/";
        //assign a listener to call a function to handle the user request when clicking a button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login() {

        message.setText("Trying to login");
        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //request type is POST
        final StringRequest loginRequest = new StringRequest(Request.Method.POST, url + "login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO should parse the json response to redirect to appropriate functions.

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("fail")){
                        message.setText(jsonObject.getString("message"));
                        Log.d("login.fail", response);
                    }else{
                        message.setText(jsonObject.getString("message"));
                        Log.d("login.success", response);
                        Intent searchPage = new Intent(Login.this, MainActivity.class);
                        startActivity(searchPage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);
    }

    @Override
    public void onResume(){
        super.onResume();
        message = findViewById(R.id.message);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        message.setText("");
        username.setText("");
        password.setText("");
    }
}
