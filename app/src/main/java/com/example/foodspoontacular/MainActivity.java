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

//MUST DO
//SHOULD DO
//TODO fix dropdown theme
//TODO make more search fields
//ONE DAY
//TODO sign up for key page

public class MainActivity extends AppCompatActivity {
    private String queryUrl, jokeUrl, theme, key;
    private TextView tvJoke;
    private EditText etQuery;
    private Button btnSearch, btnDb;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queryUrl = getString(R.string.query_url);
        jokeUrl = getString(R.string.joke_url);

        sharedPreferences = getSharedPreferences("general", 0);
        key = sharedPreferences.getString("key", getString(R.string.mashape_key));
        theme = sharedPreferences.getString("theme", "garden");

        switch(theme)
        {
            case "garden":
                setTheme(R.style.GardenTheme);
                break;
            case "submarine":
                setTheme(R.style.SubmarineTheme);
                break;
            case "monochrome":
                setTheme(R.style.MonochromeTheme);
                break;
        }
        setContentView(R.layout.activity_main);

        tvJoke = findViewById(R.id.tvJoke);
        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearch);
        btnDb = findViewById(R.id.btnDb);

        switch(theme)
        {
            case "garden":
                tvJoke.setBackgroundResource(R.drawable.rounded_corner_red);
                break;
            case "submarine":
                tvJoke.setBackgroundResource(R.drawable.rounded_corner_aqua);
                break;
            case "monochrome":
                tvJoke.setBackgroundResource(R.drawable.rounded_corner_light_grey);
                break;
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryUrl = getString(R.string.query_url);
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

        //CREDIT to Allison for her OkHttp demo, used throughout
        OkHttpHandler okHttpHandler= new OkHttpHandler();

//        uncomment for joke
        okHttpHandler.execute(jokeUrl);

    }//oncreate



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            theme = data.getStringExtra("theme");
            this.recreate();
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
                    .header(getString(R.string.header_name), key)
                    .addHeader("Accept", "application/json")
                    .build();

            try {
                response = client.newCall(request).execute();
                return response.body().string();
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
            tvJoke.setText(json.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}