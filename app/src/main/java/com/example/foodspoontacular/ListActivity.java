package com.example.foodspoontacular;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
    private RecipeAdapter recipeAdapter;
    OkHttpHandler okHttpHandler = new OkHttpHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent i = getIntent();
        String query = i.getStringExtra("query");

        listView = findViewById(R.id.nice_listview);
        recipes = new ArrayList<Recipe>();
        recipeAdapter = new RecipeAdapter(ListActivity.this, R.layout.list_item, recipes);
        listView.setAdapter(recipeAdapter);
        okHttpHandler.execute(query);

    }

    private class RecipeAdapter extends ArrayAdapter<Recipe> {

        private ArrayList<Recipe> recipes;

        public RecipeAdapter(Context context, int textViewResourceId, ArrayList<Recipe> recipes) {
            super(context, textViewResourceId, recipes);
            this.recipes = recipes;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null, true);
                vh = new ViewHolder(v);
                v.setTag(vh);
            }else {
                vh = (ViewHolder)convertView.getTag();
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
                    bt.setText("Ready in: " + o.getReadyInMinutes() + "minutes! - " + o.getId());
                }
                if (imageView != null){
                    // Loads the image via url into the image view
                    //Picasso.get().load(o.getImage()).into(vh.imageView);
                    Picasso.get().load(o.getImage()).fit().into(vh.imageView);
                }
            }
            return v;
        }


        class ViewHolder {
            TextView topText;
            TextView bottomText;
            ImageView imageView;

            ViewHolder (View v){
                this.topText = v.findViewById(R.id.toptext);
                this.bottomText = v.findViewById(R.id.bottomtext);
                this.imageView = v.findViewById(R.id.imageView);

            }
        }

    }


    class Recipe {
        private int id;
        private String title, readyInMinutes, image, category;

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

        public String getReadyInMinutes() {
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
                    ////mashape key
                    //.header("X-Mashape-Key", "HJhThtEW8nmshKXO1WtYtwgsjYHPp1WaJb7jsnLexFfLulxSTd")
                    ////rapidAPI key
                    .header("X-Mashape-Key", "GRqwZUoWJemshcH1NJ5pslMz5MmLp1Hw3HwjsnIigeMCVeJOML")
                    .addHeader("Accept", "application/json")
                    .build();


            try {
                response = client.newCall(request).execute();
                //Log.d("brandon", "doinback: " + response.body().string());
                return response.body().string();
            } catch (IOException e) {
                Log.e("brandon", "IOException in doInBackground(): " + e.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            listView = findViewById(R.id.nice_listview);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                    intent.putExtra("id", recipeAdapter.getItem(position).getId());
                    intent.putExtra("title", recipeAdapter.getItem(position).getTitle());
                    intent.putExtra("readyInMinutes", recipeAdapter.getItem(position).getReadyInMinutes());
                    intent.putExtra("image", recipeAdapter.getItem(position).getImage());
                    startActivityForResult(intent, 0);
                }
            });

            parseResponse(o.toString());
            listView.setAdapter(recipeAdapter);

        }
    }

    private void parseResponse(String response) {
        try{
            JSONObject json = new JSONObject(response);
            JSONArray jsonResults = json.getJSONArray("results");
//            Log.d("brandon", "answer: " + jsonResults.get);
            recipes = new ArrayList<Recipe>();

            for (int i=0;i<jsonResults.length();i++)
            {
                JSONObject c = jsonResults.getJSONObject(i);
                Log.d("brandon", "c: " + c.toString());
                String id =  c.getString("id");
                String title =  c.getString("title");
                String readyInMinutes =  c.getString("readyInMinutes");
                String image =  "https://spoonacular.com/recipeImages/" + c.getString("image");

                Recipe recipe = new Recipe (Integer.parseInt(id), title, readyInMinutes, image);

                recipes.add(recipe);

                recipeAdapter = new RecipeAdapter(ListActivity.this, R.layout.list_item, recipes);
                listView.setAdapter(recipeAdapter);
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}



