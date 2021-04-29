package com.example.pollubprojektmobilne3;


import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;



//import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
//import android.app.LoaderManager.LoaderCallbacks;
import android.widget.SimpleCursorAdapter;

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

        setupMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.database_actions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_smartphone:
                Toast.makeText(getBaseContext(), "Dodawanie", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.remove_smartphone:
                Toast.makeText(getBaseContext(), "Usuwanie", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    public void setUpContextualMenu() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean
            onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }



            @Override
            public void
            onDestroyActionMode(ActionMode mode) {
            }

            @Override
            public boolean
            onCreateActionMode(ActionMode mode, Menu menu) { //przy wywołaniu appBar'a
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.database_actions_menu, menu);
                return true;
            }

            @Override
            public boolean
            onActionItemClicked(ActionMode mode, MenuItem item) { //po wciśnięciu przycisku usuwającego wpisy z BD
                switch (item.getItemId()) {
                    case R.id.remove_smartphone:
                        Toast.makeText(getBaseContext(), "Usuwanie", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }

            @Override
            public void
            onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            }
        });
    }


    private void startLoader(){
//        LoaderManager.getInstance(MainActivity.this).restartLoader(0, null, this);
        getSupportLoaderManager().initLoader(0, null, this);
        String[] mapFrom = new String[]{DBHelper.BRAND, DBHelper.MODEL};
        int[] mapTo = new int[]{R.id.brand_TextView, R.id.model_TextView};
        simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list_item, cursor, mapFrom, mapTo); //TODO: do something with not initialized cursor?

    }

    public void setupMenu(){
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.database_actions_menu, menu);
                menu.findItem(R.id.add_smartphone).setVisible(false);
                menu.findItem(R.id.remove_smartphone).setVisible(true);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.remove_smartphone:
                        Toast.makeText(getBaseContext(), "Usuwanie", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
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