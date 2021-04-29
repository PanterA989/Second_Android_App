package com.example.pollubprojektmobilne3;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


//import androidx.loader.app.LoaderManager;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.widget.SimpleCursorAdapter;
//import android.app.LoaderManager.LoaderCallbacks;



public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private SimpleCursorAdapter simpleCursorAdapter;


    private static final int REQUEST_ADD_SMARTPHONE = 378;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listOfSmartphones);

        //DBHelper db = new DBHelper(this);
        //SQLiteDatabase sqlDB = db.getWritableDatabase();
        //db.close();

        setupMenu();
        startLoader();

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
                Intent intent = new Intent(this, AddSmartphoneActivity.class);
                intent.putExtra("operationType", "insert");
                startActivityForResult(intent, REQUEST_ADD_SMARTPHONE);

                return true;
            case R.id.remove_smartphone:
                Toast.makeText(getBaseContext(), "Usuwanie", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
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

    private void startLoader(){
        LoaderManager.getInstance(this).restartLoader(0, null, this);
        String[] mapFrom = new String[]{DBHelper.BRAND, DBHelper.MODEL};
        int[] mapTo = new int[]{R.id.list_brand_TextView, R.id.list_model_TextView};
        //simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list_item, cursor, mapFrom, mapTo); //TODO: do something with not initialized cursor?
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, null, mapFrom, mapTo, 0);
        listView.setAdapter(simpleCursorAdapter);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {DBHelper.ID, DBHelper.BRAND, DBHelper.MODEL};
        CursorLoader cursorLoader = new CursorLoader(this, MyContentProvider.URI_CONTENT, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }
}