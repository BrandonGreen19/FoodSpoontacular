package com.example.foodspoontacular;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {

    private String url = getString(R.string.detail_url);
    String category = "";
    String theme, key;
    private AppDatabase db;
    private TextView tvIngredients, tvInstructions, tvTitle;
    private Button btnSave;
    OkHttpHandler okHttpHandler = new OkHttpHandler();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("general", 0);
        theme = sharedPreferences.getString("theme", "garden");
        key = sharedPreferences.getString("key", getString(R.string.mashape_key));

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

        setContentView(R.layout.activity_detail);

        //CREDIT to John for ROOM demo
        db = AppDatabase.getDatabaseInstance(this);

        Intent i = getIntent();
        Integer idQuery = i.getIntExtra("id", 0);
        final String readyInMinutes = i.getStringExtra("readyInMinutes");
        final String image = i.getStringExtra("image");
        url += idQuery.toString() + "/information";
        final String title = i.getStringExtra("title");

        this.setTitle(title);

        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);
        tvTitle = findViewById(R.id.tvTitle);
        btnSave = findViewById(R.id.btnSave);

        switch(theme)
        {
            case "garden":
                tvTitle.setBackgroundResource(R.drawable.rounded_corner_red);
                tvIngredients.setBackgroundResource(R.drawable.rounded_corner_light_green);
                tvInstructions.setBackgroundResource(R.drawable.rounded_corner_dark_green);
                break;
            case "submarine":
                tvTitle.setBackgroundResource(R.drawable.rounded_corner_aqua);
                tvIngredients.setBackgroundResource(R.drawable.rounded_corner_light_blue);
                tvInstructions.setBackgroundResource(R.drawable.rounded_corner_bright_blue);
                break;
            case "monochrome":
                tvTitle.setBackgroundResource(R.drawable.rounded_corner_grey);
                tvIngredients.setBackgroundResource(R.drawable.rounded_corner_light_grey);
                tvInstructions.setBackgroundResource(R.drawable.rounded_corner_grey);
                break;
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Category newCategory = new Category(category);

                DbRecipe newRecipe = new DbRecipe(tvIngredients.getText().toString(),
                                                tvInstructions.getText().toString(),
                                                readyInMinutes,
                                                title,
                                                image);

                CreateNewDbRecipeTask newDbRecipeTask = new CreateNewDbRecipeTask(newRecipe);
                newDbRecipeTask.execute();
                Toast.makeText(DetailActivity.this, "Recipe Saved!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DetailActivity.this, DbListActivity.class);
                startActivity(intent);
            }
        });

        okHttpHandler.execute(url);
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
                Intent intent = new Intent(DetailActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                    .header(getString(R.string.header_name), key)
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
            JSONArray dishTypes = json.getJSONArray("dishTypes");
            instructions = instructions.replace("<p>", " ");
            instructions = instructions.replace("</p>", " ");
            JSONArray ingredients = json.getJSONArray("extendedIngredients");
            String ingredientString = "";

            Log.d("brandon", "dishTypes: " + dishTypes.isNull(0));
            if (dishTypes.isNull(0))
            {
                category = "unknown";
            }
            else
            {
                category = dishTypes.getString(0);
            }

            Log.d("brandon", "dishtype: " + category);
            tvInstructions.setText(instructions.replace("         ", "\n"));
            for (int i=0;i<ingredients.length();i++)
            {
                JSONObject c = ingredients.getJSONObject(i);
                String amount =  c.getString("amount");
                amount = amount.replace(".3333333333333333", ".33");
                amount = amount.replace(".0", "");
                String unit =  c.getString("unit");
                String name =  c.getString("name");

                ingredientString += amount + " " + unit + " " + name + "\n";
            }

            tvIngredients.setText(ingredientString);
            tvTitle.setText(category);

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

            Category newCategory = db.categoryDao().findCategoryByName(category);

            if (newCategory == null)
            {
                newCategory = new Category(category);
                CreateNewCategoryTask newCategoryTask = new CreateNewCategoryTask(newCategory, dbRecipe);
                newCategoryTask.execute();
            }
            else
            {
                dbRecipe.setCategoryId(newCategory.getCategoryId());
                db.dbRecipeDao().insertAll(dbRecipe);
            }
            finish();
            return null;
        }
    }

    private class CreateNewCategoryTask extends AsyncTask<Void,Void,Void>
    {
        private Category category;
        private DbRecipe dbRecipe;

        public CreateNewCategoryTask(Category category, DbRecipe dbRecipe)
        {
            this.category  = category;
            this.dbRecipe = dbRecipe;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            db.categoryDao().insertAll(category);
            category = db.categoryDao().findCategoryByName(category.getName());
            dbRecipe.setCategoryId(category.getCategoryId());
            db.dbRecipeDao().insertAll(dbRecipe);
            finish();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(DetailActivity.this, DbListActivity.class);
            startActivity(intent);
        }
    }
}



