package com.example.foodspoontacular;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// HJhThtEW8nmshKXO1WtYtwgsjYHPp1WaJb7jsnLexFfLulxSTd

public class MainActivity extends AppCompatActivity {
    private final String TEST_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/jokes/random";
    private TextView tvJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvJoke = findViewById(R.id.tvJoke);

//        OkHttpHandler okHttpHandler= new OkHttpHandler();
//        okHttpHandler.execute(TEST_URL);
    }

    public class OkHttpHandler extends AsyncTask {
//        OkHttpClient client = new OkHttpClient();
        Response response;

        @Override
        protected String doInBackground(Object[] params) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(params[0].toString())
                    .header("X-Mashape-Key", "HJhThtEW8nmshKXO1WtYtwgsjYHPp1WaJb7jsnLexFfLulxSTd")
                    .addHeader("Accept", "application/json")
                    .build();


//            try {
//                response = client.newCall(request).execute();
//                return response.body().string();
//                //Log.d("brandon", "doinback: " + response.body().string());
//            } catch (IOException e) {
//                Log.e("brandon", "IOException in doInBackground(): " + e.getMessage());
//            }

            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try {
                Log.d("brandon", "Parse responcse : " + response.body().string());

//                parseResponse(o.toString());
            } catch (IOException e) {
                Log.e("brandon", "IOException in doInBackground(): " + e.getMessage());
            }

        }
    }

    private void parseResponse(String response) {
        try{
            JSONObject json = new JSONObject(response);
            Log.d("brandon", "answer: " + json.getString("text"));
//            tvJoke.setText(json.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}