package com.example.pollubprojektmobilne3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listOfSmartphones);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("JEDEN");
        arrayList.add("DWA");
        arrayList.add("TRZY");
        arrayList.add("CZTERY");
        arrayList.add("PIĘĆ");
        arrayList.add("SZEŚĆ");
        arrayList.add("SIEDEM");
        arrayList.add("OSIEM");
        arrayList.add("DZIEWIĘĆ");
        arrayList.add("DZIESIĘĆ");
        arrayList.add("JEDENAŚCIE");
        arrayList.add("DWANAŚCIE");
        arrayList.add("JEDEN");
        arrayList.add("DWA");
        arrayList.add("TRZY");
        arrayList.add("CZTERY");
        arrayList.add("PIĘĆ");
        arrayList.add("SZEŚĆ");
        arrayList.add("SIEDEM");
        arrayList.add("OSIEM");
        arrayList.add("DZIEWIĘĆ");
        arrayList.add("DZIESIĘĆ");
        arrayList.add("JEDENAŚCIE");
        arrayList.add("DWANAŚCIE");
        if(arrayList.isEmpty()) {
            findViewById(R.id.noDataTextView).setVisibility(View.VISIBLE);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);
    }
}