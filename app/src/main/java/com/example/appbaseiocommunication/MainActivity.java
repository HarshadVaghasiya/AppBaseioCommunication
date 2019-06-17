package com.example.appbaseiocommunication;

import android.app.DownloadManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbaseiocommunication.R;

import org.json.JSONObject;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText urlEdittext, method, credentials, bodyEdittext;
    Button submit;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        credentials = findViewById(R.id.credentials);
        urlEdittext = findViewById(R.id.urlEditext);
        method = findViewById(R.id.methodEdittext);
        bodyEdittext = findViewById(R.id.bodyEditext);
        resultText = findViewById(R.id.resultText);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String base64 = credentials.getText().toString();
                String url = urlEdittext.getText().toString();
                String met = method.getText().toString().toUpperCase();
                final String body = bodyEdittext.getText().toString();

                if (url.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter URL", Toast.LENGTH_LONG).show();
                } else if (met.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Method", Toast.LENGTH_LONG).show();
                } else if (base64.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Provide Credentials", Toast.LENGTH_LONG).show();
                } else {

                    try {
                        byte[] data = base64.getBytes("UTF-8");
                        final String credentials_64 = Base64.encodeToString(data, Base64.DEFAULT);
                        Toast.makeText(getApplicationContext(), credentials_64, Toast.LENGTH_LONG).show();

                        int hii = -1;
                        if (met.equals("GET"))
                            hii = Request.Method.GET;
                        else if (met.equals("POST"))
                            hii = Request.Method.POST;
                        else if (met.equals("PUT"))
                            hii = Request.Method.PUT;
                        else if (met.equals("DELETE"))
                            hii = Request.Method.DELETE;

                        if (hii >= 0) {
                            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                            StringRequest stringRequest = new StringRequest(hii,
                                    url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            resultText.setText(response);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getApplicationContext(), "Error ):", Toast.LENGTH_LONG).show();
                                            resultText.setText("Error in request");
                                        }
                                    }
                            ) {
                                @Override
                                public Map getHeaders() throws AuthFailureError {
                                    HashMap headers = new HashMap();
                                    headers.put("Authorization", "Basic " + credentials_64);
                                    headers.put("Content-Type", "application/json");
                                    return headers;
                                }

                                @Override
                                public byte[] getBody() throws AuthFailureError {
                                    try {

                                        return body.equals("") ? null : body.getBytes("utf-8");
                                    } catch (Exception uee) {
                                        uee.printStackTrace();
                                        return null;
                                    }
                                }
                            };
                            queue.add(stringRequest);

                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter Method From GET POST PUT DELETE", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }
}
