package com.example.currencyapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView currencyListView;
    private EditText currencySearchEditText;
    private Button fetchDataButton;
    private ArrayList<String> currencyRates;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currencyListView = findViewById(R.id.currencyListView);
        currencySearchEditText = findViewById(R.id.currencySearchEditText);
        fetchDataButton = findViewById(R.id.fetchDataButton);

        currencyRates = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencyRates);
        currencyListView.setAdapter(adapter);

        fetchDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCurrencyRates("floatrates"); // Change this parameter to "exchangerates" if needed
            }
        });

        currencySearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadCurrencyRates(String source) {
        DataLoader dataLoader = new DataLoader(new DataLoader.DataLoaderListener() {
            @Override
            public void onDataLoaded(ArrayList<String> rates) {
                if (rates != null && !rates.isEmpty()) {
                    currencyRates.clear();
                    currencyRates.addAll(rates);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "No rates found or data is empty.");
                }
            }
        });

        if ("floatrates".equals(source)) {
            dataLoader.execute("http://www.floatrates.com/daily/usd.xml");
        } else if ("exchangerates".equals(source)) {
            dataLoader.execute("https://api.exchangeratesapi.io/latest");
        } else {
            Log.e("MainActivity", "Invalid source specified.");
        }
    }
}