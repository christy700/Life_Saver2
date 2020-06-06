package com.example.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText ed1,ed2;
    Button b1,b2;
    String user,pass;
    String id,name;
    String url="http://192.168.43.135/myproject/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.uname);
        ed2=(EditText)findViewById(R.id.pswd);
        b1=(Button)findViewById(R.id.login);
        b2=(Button)findViewById(R.id.regbutton);



        SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
        String x=preferences.getString("userid",null);

        if(x!=null)
        {
            Intent v=new Intent(getApplicationContext(), welcome.class);
            startActivity(v);

        }

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(j);

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=ed1.getText().toString();
                pass=ed2.getText().toString();



                sendToserver();
            }





        });
    }

    private void sendToserver() {

        Toast.makeText(getApplication(),"Please add emergency number first",Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                       Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String res=jsonObject.getString("status");
                            if(res.equals("success"))
                            {


                                JSONArray jsonArray= jsonObject.getJSONArray("Details");

                                for(int i=0; i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    name=jsonObject1.getString("username");
                                  id=jsonObject1.getString("id");

                                }
                                SharedPreferences.Editor editor=getSharedPreferences("login",MODE_PRIVATE).edit();
                                editor.putString("userid",id);
                                editor.putString("username",name);
                                editor.apply();



                                Intent v=new Intent(getApplicationContext(), welcome.class);
                                startActivity(v);
                            }
                            else
                            {
                                Toast.makeText(getApplication(),"invalid username or password",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplication(),e.toString(),Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplication(),error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> params=new HashMap<>();

                params.put("user",user);
                params.put("password",pass);

                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);







    }
}

