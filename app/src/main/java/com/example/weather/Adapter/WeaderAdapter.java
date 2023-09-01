package com.example.weather.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.Models.DataForecast;
import com.example.weather.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeaderAdapter extends RecyclerView.Adapter<WeaderAdapter.WeatherViewHolder>{
    Context context;
    List<DataForecast>list;

    public WeaderAdapter(Context context, List<DataForecast> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DataForecast data=list.get(position);

        Picasso.get().load("https:".concat(data.getIcon())).into(holder.condition_forecast);
        holder.temp_forecast.setText(data.getTemperature()+"C°");
        holder.text_speed.setText(data.getWind_speed()+"Km/h");
        holder.text_time.setText(data.getTime().substring(11,data.getTime().length()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder{
        TextView text_time,temp_forecast,text_speed;
        ImageView condition_forecast;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            text_time=itemView.findViewById(R.id.text_time);
            temp_forecast=itemView.findViewById(R.id.temp_forecast);
            text_speed=itemView.findViewById(R.id.text_speed);
            condition_forecast=itemView.findViewById(R.id.condition_forecast);
        }
    }
}
