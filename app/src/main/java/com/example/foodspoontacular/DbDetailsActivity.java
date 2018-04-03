package com.example.foodspoontacular;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/324694/analyzedInstructions?stepBreakdown=false

public class DbDetailsActivity extends AppCompatActivity {

//    private String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
//    //690978/information
//    String category = "";
    private AppDatabase db;
    private TextView tvIngredients, tvInstructions, tvTitle;
    private Button btnDelete;
//    OkHttpHandler okHttpHandler = new OkHttpHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_details);

        db = AppDatabase.getDatabaseInstance(this);

        Intent i = getIntent();
        final Integer id = i.getIntExtra("id", 0);
        final String readyInMinutes = i.getStringExtra("readyInMinutes");
        final String image = i.getStringExtra("image");
        final String instructions = i.getStringExtra("instructions");
        final String ingredients = i.getStringExtra("ingredients");
//        url += idQuery.toString() + "/information";

        final String category = i.getStringExtra("category");
        final String title = i.getStringExtra("title") + " - " + category;

        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);
        tvTitle = findViewById(R.id.tvTitle);

        tvInstructions.setText(instructions.replace("         ", "\n"));
        tvTitle.setText(title);
        tvIngredients.setText(ingredients);
        btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

//                Category newCategory = new Category(category);
//
                DbRecipe recipeToDelete =  new DbRecipe(tvIngredients.getText().toString(),
                        tvInstructions.getText().toString(),
                        readyInMinutes,
                        title,
                        image);

                recipeToDelete.setDbRecipeId(id);

                DeleteDbRecipeTask newDeleteDbRecipeTask = new DeleteDbRecipeTask(recipeToDelete);
                newDeleteDbRecipeTask.execute();
//
//                    newCategory = db.categoryDao().findCategoryByName(category);
//
//                if (db.categoryDao().findCategoryByName(category) == null)
//                {
//                    CreateNewCategoryTask newCategoryTask = new CreateNewCategoryTask(newCategory);
//                    newCategoryTask.execute();
//                    Category dbCat = db.categoryDao().findCategoryByName(category);
//                }
//                else
//                {
//                    newRecipe.setCategoryId(dbCat.getCategoryId());
//                }
//
//
//                CreateNewDbRecipeTask newDbRecipeTask = new CreateNewDbRecipeTask(newRecipe);
//                newDbRecipeTask.execute();
//
//                Intent intent = new Intent(DetailActivity.this, DbListActivity.class);
//                startActivityForResult(intent, 0);
            }
        });




//        okHttpHandler.execute(url);

    }


//    public class OkHttpHandler extends AsyncTask {
//        OkHttpClient client = new OkHttpClient();
//        Response response;
//
//        @Override
//        protected String doInBackground(Object[] params) {
//
//            OkHttpClient client = new OkHttpClient();
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    ////mashape key
//                    //.header("X-Mashape-Key", "HJhThtEW8nmshKXO1WtYtwgsjYHPp1WaJb7jsnLexFfLulxSTd")
//                    ////rapidAPI key
//                    .header("X-Mashape-Key", "GRqwZUoWJemshcH1NJ5pslMz5MmLp1Hw3HwjsnIigeMCVeJOML")
//                    .addHeader("Accept", "application/json")
//                    .build();
//
//
//            try {
//                response = client.newCall(request).execute();
//                //Log.d("brandon", "doinback: " + response.body().string());
//                return response.body().string();
//            } catch (IOException e) {
//                Log.e("brandon", "IOException in doInBackground(): " + e.getMessage());
//            }
//
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//            parseResponse(o.toString());
//        }
//    }





//    private void parseResponse(String response) {
//        try{
//            JSONObject json = new JSONObject(response);
//            String instructions = new String(json.getString("instructions"));
//            JSONArray dishTypes = json.getJSONArray("dishTypes");
//            instructions = instructions.replace("<p>", " ");
//            instructions = instructions.replace("</p>", " ");
//            JSONArray ingredients = json.getJSONArray("extendedIngredients");
//            String ingredientString = "";
//
//
//            Log.d("brandon", "dishTypes: " + dishTypes.isNull(0));
//            if (dishTypes.isNull(0))
//            {
//                category = "unknown";
//            }
//            else
//            {
//                category = dishTypes.getString(0);
//            }
//
//            Log.d("brandon", "dishtype: " + category);
//            tvInstructions.setText(instructions.replace("         ", "\n"));
//            for (int i=0;i<ingredients.length();i++)
//            {
//                JSONObject c = ingredients.getJSONObject(i);
//                String amount =  c.getString("amount");
//                String unit =  c.getString("unit");
//                String name =  c.getString("name");
//
//                ingredientString += amount + " " + unit + " " + name + "\n";
//            }
//
//            tvIngredients.setText(ingredientString);
//
//        } catch (JSONException e) {
//            Log.d("brandon", e.getMessage());
//        }
//    }
//
//    private class CreateNewDbRecipeTask extends AsyncTask<Void,Void,Void>
//    {
//        private DbRecipe dbRecipe;
//
//        public CreateNewDbRecipeTask(DbRecipe dbRecipe)
//        {
//            this.dbRecipe  = dbRecipe;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            Category newCategory = db.categoryDao().findCategoryByName(category);
//
//            if (newCategory == null)
//            {
//                newCategory = new Category(category);
//                CreateNewCategoryTask newCategoryTask = new CreateNewCategoryTask(newCategory);
//                newCategoryTask.execute();
////                Category dbCat = db.categoryDao().findCategoryByName(category);
//            }
//
//            newCategory = db.categoryDao().findCategoryByName(category);
//
//            dbRecipe.setCategoryId(newCategory.getCategoryId());
//            db.dbRecipeDao().insertAll(dbRecipe);
//            //startActivity(new Intent(getApplicationContext(),DbListActivity.class));
//            finish();
//            return null;
//        }
//    }
//
    private class DeleteDbRecipeTask extends AsyncTask<Void,Void,Void>
    {
        private DbRecipe dbRecipe;

        public DeleteDbRecipeTask(DbRecipe dbRecipe)
        {
            this.dbRecipe  = dbRecipe;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            db.dbRecipeDao().delete(dbRecipe);
            finish();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(DbDetailsActivity.this, DbListActivity.class);
            startActivityForResult(intent, 0);
        }
    }
}



