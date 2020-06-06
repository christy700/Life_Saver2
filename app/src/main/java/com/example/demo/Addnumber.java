package com.example.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Addnumber extends AppCompatActivity {
    EditText ed1,ed2,ed3;
    Button b1;
    String n1,n2,n3,x;
    String url="http://192.168.43.135/myproject/addnumber.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnumber);
        ed1=(EditText)findViewById(R.id.num1);
        ed2=(EditText)findViewById(R.id.num2);
        ed3=(EditText)findViewById(R.id.num3);
        b1=(Button)findViewById(R.id.dd1);
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
                else {
                    n1 = ed1.getText().toString();
                    n2 = ed2.getText().toString();
                    n3 = ed3.getText().toString();
                }


                SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
                x=preferences.getString("userid",null);

                sendToSerever();

            }
        });



    }

    private void sendToSerever() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();
                        Log.d("qry",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String res=jsonObject.getString("status");
                            if(res.equals("success"))
                            {
                                Toast.makeText(getApplication(),"number registered successfully",Toast.LENGTH_SHORT).show();

                                ed1.setText("");
                                ed2.setText("");
                                ed3.setText("");


                            }
                            else
                            {
                                Toast.makeText(getApplication(),"number registration failed",Toast.LENGTH_SHORT).show();
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

                params.put("no1",n1);
                params.put("no2",n2);
                params.put("no3",n3);
                params.put("id",x);

                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);




    }
}
