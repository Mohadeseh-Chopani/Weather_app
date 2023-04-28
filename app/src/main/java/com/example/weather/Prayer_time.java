package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Prayer_time extends Fragment {
    ImageView btn_back;
    TextView data_ad, month_ad, day_ad, data_lunar, month_lunar, day_lunar, text_fajr, text_sunrise, text_dhuhr, text_asr,
            text_sunset, text_maghrib, text_isha, text_midnight, text_holiday,name,data_solar,month_solar,day_solar;
    String city;
    String url;
    LinearLayout layout;
    ProgressBar progressBar;
    ImageView background_prayer;
    boolean status = false;

    @SuppressLint({"UseRequireInsteadOfGet", "MissingInflatedId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prayer_time, container, false);
        status=true;

        background_prayer=view.findViewById(R.id.background_prayer);
        layout =view.findViewById(R.id.layout);
        progressBar =view.findViewById(R.id.load);
        btn_back =view.findViewById(R.id.btn_back);
        data_ad =view.findViewById(R.id.data_Ad);
        month_ad =view.findViewById(R.id.month_Ad);
        day_ad =view.findViewById(R.id.day_Ad);
        data_lunar =view.findViewById(R.id.data_Lunar);
        month_lunar =view.findViewById(R.id.month_Lunar);
        day_lunar = view.findViewById(R.id.day_Lunar);
        text_fajr = view.findViewById(R.id.time_fajr);
        text_sunrise = view.findViewById(R.id.time_sunrise);
        text_dhuhr =view.findViewById(R.id.time_dhuhr);
        text_asr =view.findViewById(R.id.time_asr);
        text_sunset = view.findViewById(R.id.time_sunset);
        text_maghrib =view.findViewById(R.id.time_maghrib);
        text_isha =view.findViewById(R.id.time_isha);
        text_midnight =view.findViewById(R.id.time_midnight);
        text_holiday =view.findViewById(R.id.holiday);
        name =view.findViewById(R.id.city);

        city=(MainActivity.editText.getText().toString().trim());
        name.setText(city);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String current = simpleDateFormat.format(Calendar.getInstance().getTime());


        url = "https://api.aladhan.com/v1/timingsByAddress/" + current + "?address=" + city + "%2C&method=8";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                status = true;
                progressBar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                try {
                    String dataad = response.getJSONObject("data").getJSONObject("date").getJSONObject("gregorian").getString("date");
                    data_ad.setText(dataad);
                    String monthad = response.getJSONObject("data").getJSONObject("date").getJSONObject("gregorian").getJSONObject("month").getString("en");
                    month_ad.setText(monthad);
                    String dayad = response.getJSONObject("data").getJSONObject("date").getJSONObject("gregorian").getJSONObject("weekday").getString("en");
                    day_ad.setText(dayad);
                    String datahijri = response.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getString("date");
                    data_lunar.setText(datahijri);
                    String monthhijri = response.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getJSONObject("month").getString("ar");
                    month_lunar.setText(monthhijri);
                    String dayhijri = response.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getJSONObject("weekday").getString("ar");
                    day_lunar.setText(dayhijri);
                    String fajr = response.getJSONObject("data").getJSONObject("timings").getString("Fajr");
                    text_fajr.setText(fajr);
                    String sunrise = response.getJSONObject("data").getJSONObject("timings").getString("Sunrise");
                    text_sunrise.setText(sunrise);
                    String dhuhr = response.getJSONObject("data").getJSONObject("timings").getString("Dhuhr");
                    text_dhuhr.setText(dhuhr);
                    String asr = response.getJSONObject("data").getJSONObject("timings").getString("Asr");
                    text_asr.setText(asr);
                    String sunset = response.getJSONObject("data").getJSONObject("timings").getString("Sunset");
                    text_sunset.setText(sunset);
                    String maghrib = response.getJSONObject("data").getJSONObject("timings").getString("Maghrib");
                    text_maghrib.setText(maghrib);
                    String isha = response.getJSONObject("data").getJSONObject("timings").getString("Isha");
                    text_isha.setText(isha);
                    String midnight = response.getJSONObject("data").getJSONObject("timings").getString("Midnight");
                    text_midnight.setText(midnight);
                    String holiday = response.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getString("holidays");
                    text_holiday.setText(holiday);

                    if(MainActivity.is_day==1){
                        Picasso.get().load("https://www.uplooder.net/img/image/65/89a0aadcd294dc92e902c918ddfeb621/Screenshot-2023-03-14-231227.png").into(background_prayer);
                    }else {
                        Picasso.get().load("https://www.uplooder.net/img/image/2/bb736957f4c8da553cc43e177538e817/Screenshot-2023-03-14-231524.png").into(background_prayer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "please enter valid city name ...", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(objectRequest);
        return view;
    }
}