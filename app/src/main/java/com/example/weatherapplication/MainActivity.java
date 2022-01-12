package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weatherapplication.databinding.ActivityMainBinding;
import com.example.weatherapplication.model.GetWeatherDetails;
import com.example.weatherapplication.model.Hour;
import com.example.weatherapplication.model.WeatherRVModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    WeatherAdapter weatherAdapter;
    LocationManager locationManager;
    int PERMISSION_CODE = 1;
    String city = "";
    GetWeatherDetails getWeatherDetails;
    List<Hour> hours=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        binding.rvWeather.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        getCityName(location.getLatitude(), location.getLongitude());
        getWeather(city);
        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.enterCityField.getText().toString().matches("")) {
                    Toast.makeText(MainActivity.this, "Enter City Name", Toast.LENGTH_SHORT).show();
                } else {
                    getWeather(binding.enterCityField.getText().toString());
                }
            }
        });

    }

    String getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 10);
            for (Address adr : addresses) {
                if (adr != null) {
                    String c = adr.getLocality();
                    if (c != null && !c.matches("")) {
                        city = c;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }

    void getWeather(String city) {
        (Api.getClient()).getWeatherDetails("61364030a84b43b998b83537221201", city, "1", "no", "no").enqueue(new Callback<GetWeatherDetails>() {
            @Override
            public void onResponse(Call<GetWeatherDetails> call, Response<GetWeatherDetails> response) {
                try {
                    getWeatherDetails = response.body();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.rlHome.setVisibility(View.VISIBLE);
                    binding.tvCityName.setText(getWeatherDetails.getLocation().getName());
                    binding.enterCityField.setText(getWeatherDetails.getLocation().getName());
                    binding.tvTemperature.setText(getWeatherDetails.getCurrent().getTempC() + "°C");
                    Glide.with(MainActivity.this).load("http:" + getWeatherDetails.getCurrent().getCondition().getIcon()).into(binding.ivIcon);
                    binding.tvCondition.setText(getWeatherDetails.getCurrent().getCondition().getText());
                    int isDay = getWeatherDetails.getCurrent().getIsDay();
                    if (isDay == 1) {
                        binding.backgroundImage.setImageResource(R.drawable.day);
                    } else {
                        binding.backgroundImage.setImageResource(R.drawable.milky_way);
                    }
                    Date date=new Date();
                    for (Hour hour:getWeatherDetails.getForecast().getForecastday().get(0).getHour()){
                        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        SimpleDateFormat out = new SimpleDateFormat("HH");
                        try {
                            Date date1 = in.parse(hour.getTime());
                            String cur=in.format(date);
                            Date date2= in.parse(cur);
                            String date3=out.format(date1);
                            String date4=out.format(date2);
                            if (Integer.parseInt(date3)>= Integer.parseInt(date4) ){
                                hours.add(hour);
                            }
                            /*if (date1.equals(date2) || date1.after(date2)){
                                hours.add(hour);
                            }*/
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    weatherAdapter = new WeatherAdapter(MainActivity.this, hours);
                    binding.rvWeather.setAdapter(weatherAdapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetWeatherDetails> call, Throwable t) {

            }
        });
    }

}