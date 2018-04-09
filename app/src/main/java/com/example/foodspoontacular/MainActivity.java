package com.example.foodspoontacular;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
// mashape key
// HJhThtEW8nmshKXO1WtYtwgsjYHPp1WaJb7jsnLexFfLulxSTd
// rapidAPI key
// GRqwZUoWJemshcH1NJ5pslMz5MmLp1Hw3HwjsnIigeMCVeJOML
// private final String SEARCH_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?instructionsRequired=false&limitLicense=false&number=10&offset=0&query=burger";

public class MainActivity extends AppCompatActivity {
    private final String JOKE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/jokes/random";
    private String queryUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?instructionsRequired=false&limitLicense=false&number=10&offset=0&query=";
    private String theme;
    private TextView tvJoke;
    private EditText etQuery;
    private Button btnSearch, btnDb;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("general", 0);
        String theme = sharedPreferences.getString("theme", "garden");

        switch(theme)
        {
            case "garden":
                setTheme(R.style.GardenTheme);
                break;
            case "submarine":
                setTheme(R.style.SubmarineTheme);
        }
        setContentView(R.layout.activity_main);

        tvJoke = findViewById(R.id.tvJoke);
        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearch);
        btnDb = findViewById(R.id.btnDb);
        tvJoke.setText(theme);

        switch(theme)
        {
            case "garden":
                tvJoke.setBackgroundResource(R.drawable.rounded_corner_red);
                break;
            case "submarine":
                tvJoke.setBackgroundResource(R.drawable.rounded_corner_aqua);
                break;
        }

        tvJoke = findViewById(R.id.tvJoke);
        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearch);
        btnDb = findViewById(R.id.btnDb);



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?instructionsRequired=false&limitLicense=false&number=10&offset=0&query=";
                queryUrl += etQuery.getText();
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("query", queryUrl);
                startActivity(intent);
            }
        });

        btnDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DbListActivity.class);
                startActivity(intent);
            }
        });

        OkHttpHandler okHttpHandler= new OkHttpHandler();

//        uncomment for joke
//        okHttpHandler.execute(JOKE_URL);

    }//oncreate



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            theme = data.getStringExtra("theme");
            finish();
            startActivity(getIntent());
        } else {
            Toast.makeText(MainActivity.this, "I lost your data...",Toast.LENGTH_LONG);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnuSettings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class OkHttpHandler extends AsyncTask {
        Response response;

        @Override
        protected String doInBackground(Object[] params) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(params[0].toString())
                    //mashape key
                    .header("X-Mashape-Key", "HJhThtEW8nmshKXO1WtYtwgsjYHPp1WaJb7jsnLexFfLulxSTd")
                    ////rapidAPI key
//                    .header("X-Mashape-Key", "GRqwZUoWJemshcH1NJ5pslMz5MmLp1Hw3HwjsnIigeMCVeJOML")
                    .addHeader("Accept", "application/json")
                    .build();


            try {

                response = client.newCall(request).execute();
                return response.body().string();
                //Log.d("brandon", "doinback: " + response.body().string());
            } catch (IOException e) {
                Log.e("brandon", "IOException in doInBackground(): " + e.getMessage());

            }

            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
                parseResponse(o.toString());
        }
    }

    private void parseResponse(String response) {
        try{
            JSONObject json = new JSONObject(response);
            //Log.d("brandon", "answer: " + json.getString("text"));
            tvJoke.setText(json.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}