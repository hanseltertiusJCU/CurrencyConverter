package com.example.hansel.currencyconverter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ToggleButton;

public class SecondaryActivity extends AppCompatActivity {
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        SharedPreferences sharedPrefs = getSharedPreferences("value", MODE_PRIVATE);
        toggleButton.setChecked(sharedPrefs.getBoolean("metric", true));
    }

    public void main_menu(View v)
    {
        Intent intent = new Intent(this,MainActivity.class);
        if (toggleButton.isChecked())
            //to change the value of the metric when toggle button is pressed(default: "Imperial")
            //when the text is "Imperial"
        {
            SharedPreferences.Editor editor = getSharedPreferences("value", MODE_PRIVATE).edit();
            editor.putBoolean("metric", true);
            editor.apply();
        }
        else
            //when the text is "Metric"
        {
            SharedPreferences.Editor editor = getSharedPreferences("value", MODE_PRIVATE).edit();
            editor.putBoolean("metric", false);
            editor.apply();
        }
        startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("on start called");

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("on stop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}