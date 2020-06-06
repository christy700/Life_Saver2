package com.example.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class welcome extends AppCompatActivity implements SensorEventListener,LocationListener{
    Button b0,b1,b2,b3,b4;
    TextView tt1,tt2,tt3;
    float x,y,z;
    boolean status=false;
    Sensor mysensor;
    SensorManager mysensormanager;
    TextView tv;
    MediaPlayer mp;

    int numberOfHolesAndBumbs = 0;
    float accel;
    float accelCurrent;
    float accelLast;
    int shakeReset = 2500;
    long timeStamp;
    String url="http://192.168.43.135/myproject/accelerometer.php";

     LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;



    AlertDialog ad;
    String address;

    MyCountDownTimer countDownTimer;
    private static final String FORMAT = "%02d:%02d:%02d";
    CountDownTimer c;
    static int timeval;
    private final long startTime = 10 * 1000;

    private final long interval = 1 * 1000;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        countDownTimer = new MyCountDownTimer(startTime, interval);


        try{
         locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,this);

     }
     catch(SecurityException e)
     {

         e.printStackTrace();
     }
        b0=(Button)findViewById(R.id.d0);
        b1=(Button)findViewById(R.id.d1);
        b2=(Button)findViewById(R.id.d2);
        b3=(Button)findViewById(R.id.d3);
        b4=(Button)findViewById(R.id.d4);
        tt1=(TextView)findViewById(R.id.t1);
        tt2=(TextView)findViewById(R.id.t2);
        tt3=(TextView)findViewById(R.id.t3);
        tv=(TextView)findViewById(R.id.setName);


        accel = 0.00f;
        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;



        mysensormanager=(SensorManager) getSystemService(SENSOR_SERVICE);
        mysensor=mysensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mysensormanager.registerListener(this,mysensor,SensorManager.SENSOR_DELAY_UI);


        SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        String getUserId= sharedPreferences.getString("userid",null);

        String getUsername=sharedPreferences.getString("username",null);
        tv.setText("Hello "+ getUsername + ",");



        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed=getSharedPreferences("login",MODE_PRIVATE).edit();
                ed.clear();
                ed.apply();
                Intent f=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(f);


            }
        });

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b=new Intent(getApplicationContext(),Addnumber.class);
                startActivity(b);

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=false;
                Toast.makeText(getApplication(),"stopped",Toast.LENGTH_LONG).show();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=false;
                Toast.makeText(getApplication(),"stopped",Toast.LENGTH_LONG).show();



                }




        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(status)
                {
                    Toast.makeText(getApplication(),"Already running",Toast.LENGTH_LONG).show();

                }
                else{

                    status=true;
                    Toast.makeText(getApplication(),"Started",Toast.LENGTH_LONG).show();


                }



            }

        });





    }


    @Override
    public void onLocationChanged(Location location) {

        SharedPreferences.Editor editor=getSharedPreferences("latlong",MODE_PRIVATE).edit();

        Double lat=location.getLatitude();
        Double longi=location.getLongitude();

        editor.putString("lat",lat.toString());
        editor.putString("long",longi.toString());
        editor.apply();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(status==true)
        {
            x=event.values[0];
            y=event.values[1];
            z=event.values[2];

            tt1.setText(String.valueOf(x));
            tt2.setText(String.valueOf(y));
            tt3.setText(String.valueOf(z));











            // ACCELEROMETER LAST READ EQUAL TO THE CURRENT ONE
            accelLast = accelCurrent;
            // QUICK MAFS TO CALCULATE THE ACCELERATION
            accelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            // DELTA BETWEEN THE CURRENT AND THE LAST READ OF THE ACCELEROMETER
            float delta = accelCurrent - accelLast;
            // QUICK MAFS TO CALCULATE THE ACCEL THAT WILL DECLARE IF IT SHAKED OR NOT
            accel = accel * 0.9f + delta;
            // DID IT SHAKE??
            if (accel > 5) {
                if(status) {
                    final long timenow = System.currentTimeMillis();
                    if (timeStamp + shakeReset > timenow) {
                        return;
                    }
                    timeStamp = timenow;
                    numberOfHolesAndBumbs++;

                    Toast.makeText(getApplicationContext(), String.valueOf(numberOfHolesAndBumbs), Toast.LENGTH_LONG).show();

                    SharedPreferences preferences = getSharedPreferences("latlong", MODE_PRIVATE);
                    String x = preferences.getString("lat", null);

                    String y = preferences.getString("long", null);

                    Toast.makeText(getApplicationContext(), "lAT" + x, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Long" + y, Toast.LENGTH_LONG).show();


                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    mp = MediaPlayer.create(getApplicationContext(), notification);
                    mp.start();


                    showSettingsAlert();  // Call Dialog


                }


            }


        }

    }

    private void sendToServer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();

                       // Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();

                        Log.d("qry",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String res=jsonObject.getString("status");
                            if(res.equals("success"))
                            {
                              //  Toast.makeText(getApplication(),"registered successfully",Toast.LENGTH_SHORT).show();



                            }
                            else
                            {
                              //  Toast.makeText(getApplication(),"registration failed",Toast.LENGTH_SHORT).show();
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

                SharedPreferences preferences1=getSharedPreferences("latlong",MODE_PRIVATE);
                String xx=preferences1.getString("lat",null);

                String yy=preferences1.getString("long",null);


                SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
               String  x=preferences.getString("userid",null);

                params.put("id",x);
                params.put("k",xx);
                params.put("l",yy);


                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);





    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    // Functions Start

    public void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(welcome.this);
        countDownTimer.start();

        // Setting Dialog Title
        alertDialog.setTitle("Safety alert!");

        // Setting Dialog Message
        alertDialog
                .setMessage(" Do you want to report?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(),"Ok Clicked and Alert will send soon",Toast.LENGTH_LONG).show();
// Call API Here

                        sendToServer();

                        countDownTimer.cancel();
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // dialogInterface=dialog;
                        countDownTimer.cancel();
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        // alertDialog.show();
        ad = alertDialog.create();
        ad.show();
    }



    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            Toast.makeText(getApplicationContext(),"Timer ended and Alert will send soon",Toast.LENGTH_LONG).show();
// Call API Here

            sendToServer();
            ad.dismiss();




        }


        @Override

        public void onTick(long millisUntilFinished) {

            //millisUntilFinished / 1000;
        }

    }


}
