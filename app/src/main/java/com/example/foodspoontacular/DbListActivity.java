package com.example.foodspoontacular;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DbListActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecipeAdapter recipeAdapter;
    private ListView list;
    private ArrayList<DbRecipe> dbRecipes = new ArrayList();
    private ArrayList<Category> dbCategories = new ArrayList();
    private int categoryId;
    private String categoryName, theme;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabaseInstance(this);

        sharedPreferences = getSharedPreferences("general", 0);
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
        setContentView(R.layout.activity_db_list);
    }

    @Override
    protected void onResume() {

        list = findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DbRecipe dbRecipe = (DbRecipe) adapterView.getItemAtPosition(i);

                Intent data = new Intent(getApplicationContext(),DbDetailsActivity.class);
                data.putExtra("id",dbRecipe.getDbRecipeId());
                data.putExtra("ingredients",dbRecipe.getIngredients());
                data.putExtra("instructions",dbRecipe.getInstructions());
                data.putExtra("readyInMinutes",dbRecipe.getReadyInMinutes());
                data.putExtra("title",dbRecipe.getTitle());
                categoryName = dbCategories.get(dbRecipe.getCategoryId() - 1).getName();
                data.putExtra("category", categoryName);
                startActivity(data);
            }
        });

        FetchAllDbRecipesTask fetchAllDbRecipesTask = new FetchAllDbRecipesTask();
        fetchAllDbRecipesTask.execute();
        this.setTitle("My Recipes");
        super.onResume();
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
                Intent intent = new Intent(DbListActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class RecipeAdapter extends ArrayAdapter<DbRecipe>
    {
        private ArrayList<DbRecipe> dbRecipes;
        private int textViewResourceId;

        public RecipeAdapter(Context context,int textViewResourceId, ArrayList<DbRecipe> dbRecipes)
        {
            super(context,textViewResourceId,dbRecipes);
            this.textViewResourceId = textViewResourceId;
            this.dbRecipes = dbRecipes;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                switch(theme)
                {
                    case "garden":
                        v = vi.inflate(R.layout.garden_list_item, null, true);
                        break;
                    case "submarine":
                        v = vi.inflate(R.layout.submarine_list_item, null, true);
                        break;
                    case "monochrome":
                        v = vi.inflate(R.layout.monochrome_list_item, null, true);
                        break;
                }
                vh = new ViewHolder(v);
                v.setTag(vh);
            }else {
                vh = (ViewHolder)convertView.getTag();
            }
            DbRecipe o = dbRecipes.get(position);
            Category c = dbCategories.get(o.getCategoryId() - 1);
            if (o != null) {
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
                categoryId = o.getCategoryId();

                if (tt != null) {
                    tt.setText(o.getTitle());
                }
                if (bt != null) {
                    bt.setText("Ready in: " + o.getReadyInMinutes() + "minutes! - " + c.getName());
                }
                if (imageView != null){
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

    private class FetchAllDbRecipesTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            dbRecipes.clear();
            dbRecipes.addAll(db.dbRecipeDao().getAll());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recipeAdapter = new RecipeAdapter(getApplicationContext(),R.layout.garden_list_item, dbRecipes);
            FindCategoryByIdTask findCategoryByIdTask = new FindCategoryByIdTask();
            findCategoryByIdTask.execute();
        }
    }

    private class FindCategoryByIdTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            dbCategories.addAll(db.categoryDao().getAll());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recipeAdapter = new RecipeAdapter(DbListActivity.this, R.layout.garden_list_item, dbRecipes);
            list.setAdapter(recipeAdapter);
        }
    }
}