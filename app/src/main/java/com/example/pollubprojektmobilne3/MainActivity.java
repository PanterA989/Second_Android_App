package com.example.pollubprojektmobilne3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listOfSmartphones);
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

    private void startLoader(){
        LoaderManager.getInstance(MainActivity.this).restartLoader(0, null, this);
        String[] mapFrom = new String[]{DBHelper.BRAND, DBHelper.MODEL};
        int[] mapTo = new int[]{R.id.brand_TextView, R.id.model_TextView};
        simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list_item, cursor, mapFrom, mapTo); //TODO: do something with not initialized cursor?
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {DBHelper.ID, DBHelper.BRAND, DBHelper.MODEL};
        return new CursorLoader(this, MyContentProvider.URI_CONTENT, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }
}