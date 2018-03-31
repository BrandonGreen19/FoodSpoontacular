package com.example.foodspoontacular;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/324694/analyzedInstructions?stepBreakdown=false

public class DetailActivity extends AppCompatActivity {

    private String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
    //690978/information

    private AppDatabase db;
    private TextView tvIngredients, tvInstructions, tvTitle;
    private Button btnSave;
    OkHttpHandler okHttpHandler = new OkHttpHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = AppDatabase.getDatabaseInstance(this);

        Intent i = getIntent();
        Integer idQuery = i.getIntExtra("id", 0);
        final String readyInMinutes = i.getStringExtra("readyInMinutes");
        final String image = i.getStringExtra("image");
        url += idQuery.toString() + "/information";
        final String title = i.getStringExtra("title");

        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);
        tvTitle = findViewById(R.id.tvTitle);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DbRecipe newRecipe = new DbRecipe(tvIngredients.getText().toString(),
                                                tvInstructions.getText().toString(),
                                                readyInMinutes,
                                                title,
                                                image);

                CreateNewDbRecipeTask newDbRecipeTask = new CreateNewDbRecipeTask(newRecipe);
                newDbRecipeTask.execute();
            }
        });

        tvTitle.setText(title);

        okHttpHandler.execute(url);

    }


    public class OkHttpHandler extends AsyncTask {
        OkHttpClient client = new OkHttpClient();
        Response response;

        @Override
        protected String doInBackground(Object[] params) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
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
            parseResponse(o.toString());
        }
    }

    private void parseResponse(String response) {
        try{
            JSONObject json = new JSONObject(response);
            String instructions = new String(json.getString("instructions"));
            instructions = instructions.replace("<p>", " ");
            instructions = instructions.replace("</p>", " ");
            JSONArray ingredients = json.getJSONArray("extendedIngredients");
            String ingredientString = "";

            Log.d("brandon", "instructions: " + instructions);
            tvInstructions.setText(instructions.replace("         ", "\n"));
            for (int i=0;i<ingredients.length();i++)
            {
                JSONObject c = ingredients.getJSONObject(i);
                String amount =  c.getString("amount");
                String unit =  c.getString("unit");
                String name =  c.getString("name");

                ingredientString += amount + " " + unit + " " + name + "\n";
            }

            tvIngredients.setText(ingredientString);

        } catch (JSONException e) {
            Log.d("brandon", e.getMessage());
        }
    }

    private class CreateNewDbRecipeTask extends AsyncTask<Void,Void,Void>
    {
        private DbRecipe dbRecipe;

        public CreateNewDbRecipeTask(DbRecipe dbRecipe)
        {
            this.dbRecipe  = dbRecipe;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.dbRecipeDao().insertAll(dbRecipe);
            startActivity(new Intent(getApplicationContext(),DbListActivity.class));
            finish();
            return null;
        }
    }
}



