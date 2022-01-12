package com.example.weatherapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapplication.databinding.WeatherRecyclerviewItemBinding;
import com.example.weatherapplication.model.Hour;
import com.example.weatherapplication.model.WeatherRVModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder1> {

    List<Hour> weatherRVModelList;
    Context context;

    public WeatherAdapter(Context context, List<Hour> weatherRVModelList) {
        this.context = context;
        this.weatherRVModelList = weatherRVModelList;
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_recyclerview_item, parent, false);

        return new MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position) {
        Log.e("Hello","Hello");
        Hour hour = weatherRVModelList.get(position);
        Picasso.get().load("http:" + hour.getCondition().getIcon()).into(holder.imageView);
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat out = new SimpleDateFormat("hh:mm aa");
        try {
            Date date = in.parse(hour.getTime());
            holder.time.setText(out.format(date));
            holder.temp.setText(String.valueOf(hour.getTempC()) + "°C");
            holder.wind.setText(String.valueOf(hour.getWindKph()) + " Km/h");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return weatherRVModelList.size();
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {

        TextView time, temp, wind;
        ImageView imageView;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_time);
            temp = itemView.findViewById(R.id.tv_temperature);
            wind = itemView.findViewById(R.id.tv_wind_speed);
            imageView = itemView.findViewById(R.id.iv_condition);
        }
    }
}
