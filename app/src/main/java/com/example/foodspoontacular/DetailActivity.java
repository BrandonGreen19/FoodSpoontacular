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
//[{"name":"",
//  "steps":
//      [{"number":1,
//         "step":"Preheat the oven to 200 degrees F.","ingredients":[],"equipment":[{"id":404784,"name":"oven","image":"https://spoonacular.com/cdn/equipment_100x100/oven.jpg","temperature":{"number":200.0,"unit":"Fahrenheit"}}]},{"number":2,"step":"Whisk together the flour, pecans, granulated sugar, light brown sugar, baking powder, baking soda, and salt in a medium bowl. Whisk together the eggs, buttermilk, butter and vanilla extract and vanilla bean in a small bowl. Add the egg mixture to the dry mixture and gently mix to combine. Do not overmix. Let the batter sit at room temperature for at least 15 minutes and up to 30 minutes before using.","ingredients":[{"id":19334,"name":"light brown sugar","image":"https://spoonacular.com/cdn/ingredients_100x100/brown-sugar-light.jpg"},{"id":19335,"name":"granulated sugar","image":"https://spoonacular.com/cdn/ingredients_100x100/sugar-cubes.jpg"},{"id":2050,"name":"vanilla extract","image":"https://spoonacular.com/cdn/ingredients_100x100/vanilla-extract.jpg"},{"id":18371,"name":"baking powder","image":"https://spoonacular.com/cdn/ingredients_100x100/white-powder.jpg"},{"id":93622,"name":"vanilla bean","image":"https://spoonacular.com/cdn/ingredients_100x100/vanilla.jpg"},{"id":18372,"name":"baking soda","image":"https://spoonacular.com/cdn/ingredients_100x100/white-powder.jpg"},{"id":1230,"name":"buttermilk","image":"https://spoonacular.com/cdn/ingredients_100x100/buttermilk.jpg"},{"id":12142,"name":"pecans","image":"https://spoonacular.com/cdn/ingredients_100x100/pecans.jpg"},{"id":20081,"name":"all purpose flour","image":"https://spoonacular.com/cdn/ingredients_100x100/flour.png"},{"id":1123,"name":"egg","image":"https://spoonacular.com/cdn/ingredients_100x100/egg.jpg"},{"id":2047,"name":"salt","image":"https://spoonacular.com/cdn/ingredients_100x100/salt.jpg"}],"equipment":[{"id":404661,"name":"whisk","image":"https://spoonacular.com/cdn/equipment_100x100/whisk.png"},{"id":404783,"name":"bowl","image":"https://spoonacular.com/cdn/equipment_100x100/bowl.jpg"}],"length":{"number":15,"unit":"minutes"}},{"number":3,"step":"Heat a cast iron or nonstick griddle pan over medium heat and brush with melted butter. Once the butter begins to sizzle, use 2 tablespoons of the batter for each pancake and cook until the bubbles appear on the surface and the bottom is golden brown, about 2 minutes, flip over and cook until the bottom is golden brown, 1 to 2 minutes longer. Transfer the pancakes to a platter and keep warm in a 200 degree F oven.","ingredients":[],"equipment":[{"id":404779,"name":"griddle","image":"https://spoonacular.com/cdn/equipment_100x100/griddle.jpg"},{"id":404784,"name":"oven","image":"https://spoonacular.com/cdn/equipment_100x100/oven.jpg","temperature":{"number":200.0,"unit":"Fahrenheit"}},{"id":404645,"name":"frying pan","image":"https://spoonacular.com/cdn/equipment_100x100/pan.png"}],"length":{"number":3,"unit":"minutes"}},{"number":4,"step":"Serve 6 pancakes per person, top each with some of the bourbon butter. Drizzle with warm maple syrup and dust with confectioners' sugar. Garnish with fresh mint sprigs and more toasted pecans, if desired.","ingredients":[{"id":19336,"name":"powdered sugar","image":"https://spoonacular.com/cdn/ingredients_100x100/powdered-sugar.jpg"},{"id":19911,"name":"maple syrup","image":"https://spoonacular.com/cdn/ingredients_100x100/maple-syrup-or-agave-nectar.jpg"},{"id":10014037,"name":"bourbon","image":"https://spoonacular.com/cdn/ingredients_100x100/bourbon.jpg"},{"id":12142,"name":"pecans","image":"https://spoonacular.com/cdn/ingredients_100x100/pecans.jpg"}],"equipment":[]}]},{"name":"Bourbon Molasses Butter","steps":[{"number":1,"step":"Combine the bourbon and sugar in a small saucepan and cook over high heat until reduced to 3 tablespoons, remove and let cool.","ingredients":[{"id":10014037,"name":"bourbon","image":"https://spoonacular.com/cdn/ingredients_100x100/bourbon.jpg"},{"id":19335,"name":"sugar","image":"https://spoonacular.com/cdn/ingredients_100x100/

public class DetailActivity extends AppCompatActivity {

    private String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
    //690978/information

//    private ArrayList<Step> steps = new ArrayList<Step>();
//    private ListView listView;
//    private StepAdapter stepAdapter;

    private TextView tvIngredients, tvInstructions, tvTitle;
    private Button btnSave;
    OkHttpHandler okHttpHandler = new OkHttpHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        Integer idQuery = i.getIntExtra("id", 0);
        url += idQuery.toString() + "/information";
        String title = i.getStringExtra("title");

        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);
        tvTitle = findViewById(R.id.tvTitle);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(DetailActivity.this, ListActivity.class);
//                intent.putExtra("query", queryUrl);
//                startActivityForResult(intent, 0);

            }
        });

        tvTitle.setText(title);
//        listView = findViewById(R.id.nice_listview);
//        steps = new ArrayList<Step>();

//        stepAdapter = new StepAdapter(DetailActivity.this, R.layout.step_item, steps);
//        listView.setAdapter(stepAdapter);
        okHttpHandler.execute(url);

    }

//    private class StepAdapter extends ArrayAdapter<Step> {
//
//        private ArrayList<Step> steps;
//
//        public StepAdapter(Context context, int textViewResourceId, ArrayList<Step> steps) {
//            super(context, textViewResourceId, steps);
//            this.steps = steps;
//        }
//
//        //This method is called once for every item in the ArrayList as the list is loaded.
//        //It returns a View -- a list item in the ListView -- for each item in the ArrayList
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View v = convertView;
//            if (v == null) {
//                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                v = vi.inflate(R.layout.step_item, null);
//            }
//            Step o = steps.get(position);
//            if (o != null) {
//                TextView tt = (TextView) v.findViewById(R.id.toptext);
//                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
//                ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
//                if (tt != null) {
//                    tt.setText(o.getTitle());
//                }
//                if (bt != null) {
//                    bt.setText("Ready in: " + o.getreadyInMinutes() + "minutes!");
//                }
////                if (imageView != null){
////                    imageView.setImage...
////                }
//            }
//            return v;
//        }
//    }


//    class Step {
//        private int id;
//        private String title, readyInMinutes, image;
//
//        public Step(Integer id, String title, String readyInMinutes, String image) {
//            this.id = id;
//            this.title = title;
//            this.readyInMinutes = readyInMinutes;
//            this.image = image;
//        }
//
//        public Integer getId() {
//            return id;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getreadyInMinutes() {
//            return readyInMinutes;
//        }
//
//        public String getImage() {
//            return image;
//        }
//    }


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

//            listView = findViewById(R.id.nice_listview);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent(ListActivity.this, DetailActivity.class);
//                    intent.putExtra("id", recipeAdapter.getItem(position).getId());
////                    intent.putExtra("title", recipeAdapter.getItem(position).title);
////                    intent.putExtra("readyInMinutes", recipeAdapter.getItem(position).getreadyInMinutes());
////                    intent.putExtra("image", recipeAdapter.getItem(position).getImage());
//                    startActivityForResult(intent, 0);
//                }
//            });

            parseResponse(o.toString());
//            listView.setAdapter(stepAdapter);

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
//            steps = new ArrayList<Step>();
            tvInstructions.setText(instructions.replace("         ", "\n"));
            for (int i=0;i<ingredients.length();i++)
            {
                JSONObject c = ingredients.getJSONObject(i);
                String amount =  c.getString("amount");
                String unit =  c.getString("unit");
                String name =  c.getString("name");

                ingredientString += amount + " " + unit + " " + name + "\n";

//                Step recipe = new Step (Integer.parseInt(id), title, readyInMinutes, image);
//
//                steps.add(recipe);
//
//                stepAdapter = new StepAdapter(DetailActivity.this, R.layout.step_item, steps);
//                listView.setAdapter(stepAdapter);
            }
            tvIngredients.setText(ingredientString);
        } catch (JSONException e) {
            Log.d("brandon", e.getMessage());
        }
    }

}



