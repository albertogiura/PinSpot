package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText et,at;
    TextView tv,descrizione,descrizione2;
    //String url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}";
    String url = "api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}";
    String apikey = "4f6ec18ab9eb724adb869edca9cbbf63";
    LocationManager manager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = findViewById(R.id.et);
        at = findViewById(R.id.at);
        tv = findViewById(R.id.tv);
        descrizione = findViewById(R.id.descrizione);
        descrizione2 = findViewById(R.id.descrizione2);

    }

    public void getweather(View v){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherapi myapi=retrofit.create(weatherapi.class);
        Call<Example> examplecall = myapi.getweather(Double.parseDouble(String.valueOf(et.getText())),Double.parseDouble(String.valueOf(at.getText())), apikey);
        examplecall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if(response.code()==404){
                    Toast.makeText(MainActivity.this,"Please Enter a valid City",Toast.LENGTH_LONG).show();
                }
                else if(!(response.isSuccessful())){
                    Toast.makeText(MainActivity.this,response.code()+" ",Toast.LENGTH_LONG).show();
                    return;
                }
                Example mydata=response.body();

                Main main=mydata.getMain();
                Wind wind=mydata.getWind();
                Weather[] weather = mydata.getWeather();


                Double temp=main.getTemp();
                Double temp1=wind.getSpeed();

                String description = weather[0].getDescription();
                String description2 = weather[0].getMain();

                Integer temperature=(int)(temp-273.15);
                tv.setText(String.valueOf(temperature)+" °C");
                descrizione.setText(description);
                descrizione2.setText(description2);

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }
}
