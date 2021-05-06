package com.example.pollubprojektmobilne3;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends AppCompatActivity implements ConfirmDialog.ConfirmDialogListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private SimpleCursorAdapter simpleCursorAdapter;


    public static final int REQUEST_CREATE_SMARTPHONE = 378;
    public static final int REQUEST_UPDATE_SMARTPHONE = 379;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listOfSmartphones);

        //adding text to show if list is empty
        listView.setEmptyView(findViewById(R.id.noDataTextView));

        //setting up upper menu and its actions handling
        setupMenu();

        //setting up LoaderManager which will map elements from DB to ListView
        startLoader();

        //setting up actions to perform after touching element on the list
        setUpOnClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creating upper menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.database_actions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //action for add icon
        if (item.getItemId() == R.id.add_smartphone) {
            Intent intent = new Intent(this, AddSmartphoneActivity.class);
            //adding type of action to perform
            intent.putExtra("requestCode", REQUEST_CREATE_SMARTPHONE);
            startActivityForResult(intent, REQUEST_CREATE_SMARTPHONE);

            return true;
        }
        return false;
    }

    public void setupMenu(){
        //allowing to select multiple elements
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            //creating upper menu while selecting elements
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.database_actions_menu, menu);
                //hiding add icon
                menu.findItem(R.id.add_smartphone).setVisible(false);
                //showing remove icon
                menu.findItem(R.id.remove_smartphone).setVisible(true);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                //action for remove icon
                if (item.getItemId() == R.id.remove_smartphone) {
                    ConfirmDialog dialog = new ConfirmDialog();
                    //showing confirmation dialog - after pressing YES, method onYesClicked() will be called
                    dialog.show(getSupportFragmentManager(), "confirmationDialog");
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    private void startLoader(){
        LoaderManager.getInstance(this).restartLoader(0, null, this);
        //array of strings from which adapter will map elements to list
        String[] mapFrom = new String[]{DBHelper.BRAND, DBHelper.MODEL};
        //array of UI elements to which adapter will map elements to
        int[] mapTo = new int[]{R.id.list_brand_TextView, R.id.list_model_TextView};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, null, mapFrom, mapTo, 0);
        //adding created adapter to ListView
        listView.setAdapter(simpleCursorAdapter);
    }

    public void setUpOnClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddSmartphoneActivity.class);
                //adding type of action to perform
                intent.putExtra("requestCode", REQUEST_UPDATE_SMARTPHONE);
                //adding id of touched element
                intent.putExtra("id", id);
                //starting new activity
                startActivityForResult(intent, REQUEST_UPDATE_SMARTPHONE);
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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }

    //action to perform after clicking YES in dialog for deleting elements
    @Override
    public void onYesClicked() {
        //geting array of selected elements
        long[] checked = listView.getCheckedItemIds();

        //deleting all selected elements
        for (long l : checked) {
            getContentResolver().delete(MyContentProvider.URI_CONTENT, DBHelper.ID + " = " + l, null);
        }
        Toast.makeText(getBaseContext(), "Usunięto wybrane elementy.", Toast.LENGTH_SHORT).show();
    }
}