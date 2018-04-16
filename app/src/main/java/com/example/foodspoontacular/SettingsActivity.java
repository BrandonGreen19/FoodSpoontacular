package com.example.foodspoontacular;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private CheckBox cbRapid;
    String theme, key;

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
        setContentView(R.layout.activity_settings);

        Spinner spTheme = findViewById(R.id.spTheme);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.themes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTheme.setAdapter(adapter);
        spTheme.setOnItemSelectedListener(this);

        cbRapid = findViewById(R.id.cbRapid);

        if (key.equals(getString(R.string.rapid_key)))
        {
            cbRapid.setChecked(true);
        }

        cbRapid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("key", getString(R.string.rapid_key));
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("key", getString(R.string.mashape_key));
                    editor.commit();
                }
            }
        });

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
            case "monochrome":
                themeInt = 2;
                break;
        }

        spTheme.setSelection(0);
    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        if (!parent.getItemAtPosition(pos).toString().equals("Select Theme:")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Toast.makeText(this, parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
            editor.putString("theme", parent.getItemAtPosition(pos).toString());
            editor.commit();

            Intent dataIntent = new Intent(SettingsActivity.this, MainActivity.class);
            //thanks Jean:
            //this.recreate();
            //need to fix auto select of spinner
            dataIntent.putExtra("theme", theme);
            setResult(Activity.RESULT_OK, dataIntent);
//            recreate();
//            onBackPressed();

            dataIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(dataIntent);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}
