package com.example.foodspoontacular;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/324694/analyzedInstructions?stepBreakdown=false

public class DbDetailsActivity extends AppCompatActivity {

//    private String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
//    //690978/information
    private AppDatabase db;
    private TextView tvIngredients, tvInstructions, tvTitle;
    private Button btnDelete;
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

        setContentView(R.layout.activity_db_details);



        db = AppDatabase.getDatabaseInstance(this);

        Intent i = getIntent();
        final Integer id = i.getIntExtra("id", 0);
        final String readyInMinutes = i.getStringExtra("readyInMinutes");
        final String image = i.getStringExtra("image");
        final String instructions = i.getStringExtra("instructions");
        final String ingredients = i.getStringExtra("ingredients");

        final String category = i.getStringExtra("category");
        final String title = i.getStringExtra("title");

        this.setTitle(title);

        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);
        tvTitle = findViewById(R.id.tvTitle);

        tvInstructions.setText(instructions.replace("         ", "\n"));
        tvTitle.setText(category);
        tvIngredients.setText(ingredients);
        btnDelete = findViewById(R.id.btnDelete);

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
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

        DbRecipe recipeToDelete =  new DbRecipe(tvIngredients.getText().toString(),
                tvInstructions.getText().toString(),
                readyInMinutes,
                title,
                image);

        recipeToDelete.setDbRecipeId(id);

        DeleteDbRecipeTask newDeleteDbRecipeTask = new DeleteDbRecipeTask(recipeToDelete);
        newDeleteDbRecipeTask.execute();

            }
        });
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
                Intent intent = new Intent(DbDetailsActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


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
            finish();
//            Intent intent = new Intent(DbDetailsActivity.this, DbListActivity.class);
//            startActivity(intent);

//            DbDetailsActivity.this.onBackPressed();
        }
    }
}



