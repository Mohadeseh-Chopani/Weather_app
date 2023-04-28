package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather.Adapter.WeaderAdapter;
import com.example.weather.Models.DataForecast;
import com.google.android.gms.location.LocationListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout_home;
    ProgressBar progressBar;
    TextView city_name, text_temp, text_condition, text_header,country,time;
    ImageView icon_search, icon_current, background;
    RecyclerView rv_forecast;
    public static EditText editText;
    ArrayList<DataForecast> dataForecasts;
    WeaderAdapter adapter;
    LocationManager locationManager;
    int PERMISION_COOE = 1;
    public static String cityname;
    Button btn_prayer;
    public static int is_day;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        country=findViewById(R.id.country);
        time=findViewById(R.id.time);
        btn_prayer=findViewById(R.id.btn_prayer_time);
        layout_home=findViewById(R.id.layout_home);
        progressBar=findViewById(R.id.loading);
        city_name=findViewById(R.id.name_city);
        text_temp=findViewById(R.id.text_temp);
        text_condition=findViewById(R.id.text_condition);
        icon_search=findViewById(R.id.btn_search);
        icon_current=findViewById(R.id.icon_current);
        rv_forecast=findViewById(R.id.rv_forecast);
        editText=findViewById(R.id.edit_search);
        background=findViewById(R.id.background);
        text_header=findViewById(R.id.text_header);

        dataForecasts=new ArrayList<>();
        adapter=new WeaderAdapter(this,dataForecasts);
        rv_forecast.setAdapter(adapter);

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},PERMISION_COOE);
        }


        Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location!=null){
            cityname=getcityname(location.getLatitude(),location.getLongitude());
        }


        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cManager.getActiveNetworkInfo();
        if(ninfo!=null && ninfo.isConnected())

        {
            Toast.makeText(this, "Available",Toast.LENGTH_LONG).show();
        }

        else
        {
            Toast.makeText(this, "Not Available",Toast.LENGTH_LONG).show();
        }


        getWeaderinfo(cityname);

        icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city=editText.getText().toString().trim();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity.this,"name city is empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    city_name.setText(cityname);
                    getWeaderinfo(city);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISION_COOE){
            if(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"permission granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,"please accept the permission",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private  String getcityname(double latitude, double longitude){
        String cityname="";
        Geocoder geocoder=new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address>addresses=geocoder.getFromLocation(latitude,longitude,10);
            for (Address address : addresses){
                if(address!=null){
                    String city=address.getCountryName();
                    if(city!=null && !city.equals("")){
                        cityname=city;
                    }
                    else {
                        Log.d("TAG","city not found");
                        Toast.makeText(this,"city not found",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return cityname;
    }


    private void getWeaderinfo(String cityname){
        String url="https://api.weatherapi.com/v1/forecast.json?key=2e549cf3abfc4551acb200656232301&q="+cityname+"&days=1&aqi=yes&alerts=yes";

        city_name.setText(cityname);

        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                layout_home.setVisibility(View.VISIBLE);
                dataForecasts.clear();
                try {
                    String temperature=response.getJSONObject("current").getString("temp_c");
                    text_temp.setText(temperature+"C°");
                    String condition=response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String condotion_icon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    is_day=response.getJSONObject("current").getInt("is_day");
                    Picasso.get().load("https:".concat(condotion_icon)).placeholder(R.drawable.icon_cloud).into(icon_current);
                    text_condition.setText(condition);
                    String time_current=response.getJSONObject("location").getString("localtime");
                    time.setText(time_current.substring(11,time_current.length()));
                    String countryname = response.getJSONObject("location").getString("country");
                    String city = response.getJSONObject("location").getString("name");
                    country.setText(countryname+"/"+city);


                    if(is_day==1){
                        Picasso.get().load("https://www.uplooder.net/img/image/65/89a0aadcd294dc92e902c918ddfeb621/Screenshot-2023-03-14-231227.png").into(background);
                        text_header.setTextColor(Color.BLACK);
                        text_condition.setTextColor(Color.BLACK);
                        city_name.setTextColor(Color.BLACK);
                        text_temp.setTextColor(Color.BLACK);
                        time.setTextColor(Color.BLACK);
                        country.setTextColor(Color.BLACK);
                    }
                    else {
                        Picasso.get().load("https://www.uplooder.net/img/image/2/bb736957f4c8da553cc43e177538e817/Screenshot-2023-03-14-231524.png").into(background);
                        text_header.setTextColor(Color.WHITE);
                        text_condition.setTextColor(Color.WHITE);
                        city_name.setTextColor(Color.WHITE);
                        text_temp.setTextColor(Color.WHITE);
                        time.setTextColor(Color.WHITE);
                        country.setTextColor(Color.WHITE);
                    }

                    JSONObject forecastobj=response.getJSONObject("forecast");
                    JSONObject forecast=forecastobj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourarray=forecast.getJSONArray("hour");

                    for(int i=0;i<hourarray.length();i++){
                        JSONObject hourobject=hourarray.getJSONObject(i);
                        String time=hourobject.getString("time");
                        String temp=hourobject.getString("temp_c");
                        String img=hourobject.getJSONObject("condition").getString("icon");
                        String wind=hourobject.getString("wind_kph");
                        dataForecasts.add(new DataForecast(time,temp,img,wind));
                    }

                    adapter.notifyDataSetChanged();

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"please enter valid city name ...", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

        btn_prayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                if(editText.getText().equals(" ")) {
                    bundle.putString("city_name", editText.getText().toString().trim());
                    Prayer_time fragment1 = new Prayer_time();
                    Objects.requireNonNull(fragment1).setArguments(bundle);
                }
                Fragment fragment=new Prayer_time();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_page,fragment).commit();

            }
        });
    }
}