package com.example.hansel.currencyconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText editText_convert;
    private Spinner spinner_from;
    private Spinner spinner_to;
    private Button button_convert;
    private TextView textView_from;
    private TextView textView_to;
    private String currency_url = "https://rate-exchange-1.appspot.com/currency?";
    private AQuery aq;
    private Boolean metric;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_convert = (EditText) findViewById(R.id.editText_convert);
        spinner_from = (Spinner) findViewById(R.id.spinner_from);
        spinner_to = (Spinner) findViewById(R.id.spinner_to);
        button_convert = (Button) findViewById(R.id.button_convert);
        textView_from = (TextView) findViewById(R.id.textView_from);
        textView_to = (TextView) findViewById(R.id.textView_to);
        aq = new AQuery(this);
        //instantiate the query, which makes android-query useful
        sharedPrefs = getSharedPreferences("value", MODE_PRIVATE);
        metric = sharedPrefs.getBoolean("metric",true);
        // assigning the boolean value, which is useful for inverse conversion


        ArrayAdapter<CharSequence> adapter =
                // to be able to dropdown the spinner item based on the currency.xml
                ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);
        // to set the currency.xml values in spinner

        if (metric) {
            // if the boolean metric is true (which is before you setup anything)
            button_convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (editText_convert.getText().toString().length() < 1) {
                        // when the input is empty, show the error message
                        Toast.makeText(MainActivity.this, "you have to write a value", Toast.LENGTH_LONG).show();
                    } else {
                        final Double currency_from_value = Double.valueOf(editText_convert.getText().toString());
                        String from_currency = String.valueOf(spinner_from.getSelectedItem());
                        String to_currency = String.valueOf(spinner_to.getSelectedItem());

                        String url = currency_url + "from=" + from_currency + "&to=" + to_currency;// perform the search from the URL

                        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

                            @Override
                            public void callback(String url, JSONObject json, AjaxStatus status) {

                                if (json != null) {
                                        //successful ajax call
                                    try {
                                        Double rate = json.getDouble("rate");// get the rate of the conversion

                                        double the_result = currency_from_value * rate;// get the result based on the rate and the value that you entered

                                        textView_from.setText(editText_convert.getText().toString() + "" + spinner_from.getSelectedItem().toString() + "=");
                                        textView_to.setText(String.valueOf(the_result) + " " + spinner_to.getSelectedItem().toString());
                                        // get the results that is displayed
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    //ajax error
                                    Toast.makeText(aq.getContext(), "Error" + status.getCode(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }


                }
            });


        } else {
            // when the boolean metric is false, which is the inversed conversion
            button_convert.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    if(editText_convert.getText().toString().length() < 1){
                        Toast.makeText(MainActivity.this,"you have to write a value",Toast.LENGTH_LONG).show();
                    }else {
                        final Double currency_from_value = Double.valueOf(editText_convert.getText().toString());
                        String from_currency = String.valueOf(spinner_from.getSelectedItem());
                        String to_currency = String.valueOf(spinner_to.getSelectedItem());

                        String url = currency_url + "from=" + to_currency + "&to=" + from_currency;

                        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

                            @Override
                            public void callback(String url, JSONObject json, AjaxStatus status) {

                                if (json != null) {

                                    try{
                                        Double rate = json.getDouble("rate");

                                        double the_result = currency_from_value * rate;

                                        textView_from.setText(editText_convert.getText().toString() + "" + spinner_to.getSelectedItem().toString() + "=");
                                        textView_to.setText(String.valueOf(the_result) + " " + spinner_from.getSelectedItem().toString());
                                    } catch (JSONException e){
                                        e.printStackTrace();
                                    }


                                } else {

                                    Toast.makeText(aq.getContext(), "Error" + status.getCode(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }


                }
            });


        }
        }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            // to make the setting button in top right hand corner appear vertically
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            // to make the setting button in top right hand corner appear horizontally
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SecondaryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

