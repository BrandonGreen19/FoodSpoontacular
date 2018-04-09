package com.example.foodspoontacular;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SharedPreferences sharedPreferences;
    String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner spTheme = findViewById(R.id.spTheme);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.themes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTheme.setAdapter(adapter);
        spTheme.setOnItemSelectedListener(this);

        sharedPreferences = getSharedPreferences("general", 0);
        theme = sharedPreferences.getString("theme", "garden");
        int themeInt = 0;
        switch(theme){
            case "garden":
                themeInt = 0;
                break;
            case "submarine":
                themeInt = 1;
                break;
        }

        spTheme.setSelection(themeInt);
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Toast.makeText(this, parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
        editor.putString("theme", parent.getItemAtPosition(pos).toString());
        editor.commit();

        Intent dataIntent = new Intent();
        dataIntent.putExtra("theme", theme);
        setResult(Activity.RESULT_OK, dataIntent);

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}
