package com.example.foodspoontacular;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    private ListView listView;
    //TODO define RecipeAdapter below
    private RecipeAdapter recipeAdapter;
    OkHttpHandler okHttpHandler = new OkHttpHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent i = getIntent();
        String query = i.getStringExtra("query");

        recipeAdapter = new RecipeAdapter(ListActivity.this, R.layout.list_item, recipes);
        listView = findViewById(R.id.nice_listview);
        listView.setAdapter(recipeAdapter);

        recipes = new ArrayList<Recipe>();
        listView.setAdapter(recipeAdapter);
        okHttpHandler.execute(query);

    }

    private class RecipeAdapter extends ArrayAdapter<Recipe> {

        private ArrayList<Recipe> recipes;

        public RecipeAdapter(Context context, int textViewResourceId, ArrayList<Recipe> recipes) {
            super(context, textViewResourceId, recipes);
            this.recipes = recipes;
        }

        //This method is called once for every item in the ArrayList as the list is loaded.
        //It returns a View -- a list item in the ListView -- for each item in the ArrayList
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            Recipe o = recipes.get(position);
            if (o != null) {
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
                if (tt != null) {
                    tt.setText(o.getTitle());
                }
                if (bt != null) {
                    bt.setText("Ready in: " + o.getreadyInMinutes() + "minutes!");
                }
//                if (imageView != null){
//                    imageView.setImage...
//                }
            }
            return v;
        }
    }


    class Recipe {
        private int id;
        private String title, readyInMinutes, image;

        public Recipe(Integer id, String title, String readyInMinutes, String image) {
            this.id = id;
            this.title = title;
            this.readyInMinutes = readyInMinutes;
            this.image = image;
        }

        public Integer getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getreadyInMinutes() {
            return readyInMinutes;
        }

        public String getImage() {
            return image;
        }
    }


    public class OkHttpHandler extends AsyncTask {
        OkHttpClient client = new OkHttpClient();
        Response response;

        @Override
        protected String doInBackground(Object[] params) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(params[0].toString())
                    .header("X-Mashape-Key", "HJhThtEW8nmshKXO1WtYtwgsjYHPp1WaJb7jsnLexFfLulxSTd")
                    .addHeader("Accept", "application/json")
                    .build();


            try {
                response = client.newCall(request).execute();
                Log.d("brandon", "doinback: " + response.body().string());
                return response.body().string();
            } catch (IOException e) {
                Log.e("brandon", "IOException in doInBackground(): " + e.getMessage());
            }

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



