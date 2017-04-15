package com.example.hansel.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private EditText editText_currency;
    private Spinner spinner_from;
    private Spinner spinner_to;
    private Button button_convert;
    private TextView textView_from;
    private TextView textView_to;
    private String base_url = "https://rate-exchange-1.appspot.com/currency?";
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_currency = (EditText) findViewById(R.id.editText_currency);
        spinner_from = (Spinner) findViewById(R.id.spinner_from);
        spinner_to = (Spinner) findViewById(R.id.spinner_to);
        button_convert = (Button) findViewById(R.id.button_convert);
        textView_from = (TextView) findViewById(R.id.textView_from);
        textView_to = (TextView) findViewById(R.id.textView_to);
        aq = new AQuery(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        button_convert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(editText_currency.getText().toString().length() < 1){
                    Toast.makeText(MainActivity.this,"you have to write a value",Toast.LENGTH_LONG).show();
                }else {
                    final Double currency_from_value = Double.valueOf(editText_currency.getText().toString());
                    String from_currency = String.valueOf(spinner_from.getSelectedItem());
                    String to_currency = String.valueOf(spinner_to.getSelectedItem());

                    String url = base_url + "from=" + from_currency + "&to=" + to_currency;

                    aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {

                            if (json != null) {

                                try{
                                    Double rate = json.getDouble("rate");

                                    double the_result = currency_from_value * rate;

                                    textView_from.setText(editText_currency.getText().toString() + "" + spinner_from.getSelectedItem().toString() + "=");
                                    textView_to.setText(String.valueOf(the_result) + " " + spinner_to.getSelectedItem().toString());
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
