package com.example.foodspoontacular;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_list);

        db = AppDatabase.getDatabaseInstance(this);

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
                startActivity(data);
            }
        });

        recipeAdapter = new RecipeAdapter(getApplicationContext(),R.layout.list_item, dbRecipes);

        FetchAllDbRecipesTask fetchAllDbRecipesTask = new FetchAllDbRecipesTask();
        fetchAllDbRecipesTask.execute();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.newUser:
//                startActivity(new Intent(getApplicationContext(),newUserActivity.class));
//                break;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

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
                v = vi.inflate(R.layout.list_item, null, true);
                vh = new ViewHolder(v);
                v.setTag(vh);
            }else {
                vh = (ViewHolder)convertView.getTag();
            }
            DbRecipe o = dbRecipes.get(position);
            if (o != null) {
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
                if (tt != null) {
                    tt.setText(o.getTitle());
                }
                if (bt != null) {
                    bt.setText("Ready in: " + o.getReadyInMinutes() + "minutes! - " + o.getDbRecipeId());
                }
                if (imageView != null){
                    // Loads the image via url into the image view
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
            dbRecipes.addAll(db.dbRecipeDao().getAll());
//            list = findViewById(R.id.list);
//            list.setAdapter(recipeAdapter);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            list.setAdapter(recipeAdapter);
        }
    }
}