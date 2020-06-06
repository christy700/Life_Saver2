package com.example.demo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8,ed9,ed10,ed11;
    Button b1;
    String name,veh,name2,address,city,pin,st,country,bl,pass1,cpass2;
    String url="http://192.168.43.135/myproject/registration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ed1 = (EditText) findViewById(R.id.nam1);
        ed2 = (EditText) findViewById(R.id.vn);
        ed3 = (EditText) findViewById(R.id.nam2);
        ed4 = (EditText) findViewById(R.id.add);
        ed5 = (EditText) findViewById(R.id.ct);
        ed6 = (EditText) findViewById(R.id.pc);
        ed7 = (EditText) findViewById(R.id.st);
        ed8 = (EditText) findViewById(R.id.cy);
        ed9 = (EditText) findViewById(R.id.bg);
        ed10 = (EditText) findViewById(R.id.pass);
        ed11 = (EditText) findViewById(R.id.cpass);


        b1 = (Button) findViewById(R.id.reg);





        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ed1.length()==0)
                {
                    ed1.setError("fill the field");
                }
                else if (ed2.length()==0)
                {
                    ed2.setError("fill the field");
                }
                else if (ed3.length()==0)
                {
                    ed3.setError("fill the field");
                }
                else if (ed4.length()==0)
                {
                    ed4.setError("fill the field");
                }
                else if (ed5.length()==0)
                {
                    ed5.setError("fill the field");
                }
                else if (ed6.length()==0)
                {
                    ed6.setError("fill the field");
                }
                else if (ed7.length()==0)
                {
                    ed7.setError("fill the field");
                }
                else if (ed8.length()==0)
                {
                    ed8.setError("fill the field");
                }
                else if (ed9.length()==0)
                {
                    ed9.setError("fill the field");
                }
                else if (ed10.length()==0)
                {
                    ed10.setError("fill the field");
                }
                else if (ed11.length()==0)
                {
                    ed11.setError("fill the field");
                }
                else {


                    name = ed1.getText().toString();
                    veh = ed2.getText().toString();
                    name2 = ed3.getText().toString();
                    address = ed4.getText().toString();
                    city = ed5.getText().toString();
                    pin = ed6.getText().toString();
                    st = ed7.getText().toString();
                    country = ed8.getText().toString();
                    bl = ed9.getText().toString();
                    pass1 = ed10.getText().toString();
                    cpass2 = ed11.getText().toString();


                    //Toast.makeText(getApplication(), name, Toast.LENGTH_LONG).show();

                    if (pass1.contentEquals(cpass2)) {
                        // Toast.makeText(getApplication(), "REGISTERED", Toast.LENGTH_LONG).show();

                        sendToSerever();
                    } else {
                        Toast.makeText(getApplication(), "password must me same", Toast.LENGTH_SHORT).show();
                    }
                }



            }


        });
    }

    private void sendToSerever() {
        //Toast.makeText(getApplication(),"check",Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String res=jsonObject.getString("status");
                            if(res.equals("success"))
                            {
                                Toast.makeText(getApplication(),"successfully registered",Toast.LENGTH_SHORT).show();
                                Intent z=new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(z);
                                Toast.makeText(getApplication(),"successfully registered",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplication(),"registration failed",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> params=new HashMap<>();

                params.put("user",name);
                params.put("vehicle",veh);
                params.put("name",name2);
                params.put("add",address);
                params.put("city",city);
                params.put("pin",pin);
                params.put("state",st);
                params.put("country",country);
                params.put("bloodgroup",bl);
                params.put("pass",pass1);


                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);







    }


    }
