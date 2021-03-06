package com.example.pollubprojektmobilne3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Hashtable;

public class AddSmartphoneActivity extends AppCompatActivity implements ConfirmDialog.ConfirmDialogListener {

    private EditText brand;
    private EditText version;
    private EditText model;
    private EditText www;

    private int requestCode;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_smartphone);

        brand = findViewById(R.id.Brand_EditText);
        version = findViewById(R.id.Version_EditText);
        model = findViewById(R.id.Model_EditText);
        www = findViewById(R.id.WWW_EditText);

        //setting up Activity
        prepareActivity();
    }

    public void prepareActivity() {
        Bundle bundle = getIntent().getExtras();
        requestCode = bundle.getInt("requestCode");
        if (requestCode == MainActivity.REQUEST_CREATE_SMARTPHONE)
            getSupportActionBar().setTitle("Dodanie wpisu");
        if (requestCode == MainActivity.REQUEST_UPDATE_SMARTPHONE) {
            getSupportActionBar().setTitle("Aktualizacja wpisu");
            //getting smartphone id from bundle
            id = bundle.getLong("id");
            //filling TextEdit fields with smartphone data
            setTextEditValues(id);
            //showing delete button
            findViewById(R.id.Delete_Button).setVisibility(View.VISIBLE);
        }
    }

    void setTextEditValues(long id){
        Cursor cursor;
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = db.query(true, //distinct
                DBHelper.TABLE_NAME, //table
                new String[]{DBHelper.ID, DBHelper.BRAND, DBHelper.MODEL, DBHelper.VERSION, DBHelper.WWW}, //columns
                DBHelper.ID + "=?", //selection - where
                new String[] {Long.toString(id)}, //selectionArgs
                null, //group by
                null, //having
                null, //order by
                null); //limit

        Hashtable<String, String> smartphoneData = new Hashtable<>();
        //while loop for all selected elements.
        while (cursor.moveToNext()){
            //puting elements to Hashtable
            smartphoneData.put(DBHelper.BRAND, cursor.getString(cursor.getColumnIndex(DBHelper.BRAND)));
            smartphoneData.put(DBHelper.MODEL, cursor.getString(cursor.getColumnIndex(DBHelper.MODEL)));
            smartphoneData.put(DBHelper.VERSION, cursor.getString(cursor.getColumnIndex(DBHelper.VERSION)));
            smartphoneData.put(DBHelper.WWW, cursor.getString(cursor.getColumnIndex(DBHelper.WWW)));
        }

        cursor.close();

        //setting TextEdit texts with acquired data
        brand.setText(smartphoneData.get(DBHelper.BRAND));
        model.setText(smartphoneData.get(DBHelper.MODEL));
        version.setText(smartphoneData.get(DBHelper.VERSION));
        www.setText(smartphoneData.get(DBHelper.WWW));
    }

    public boolean validation() {
        //main validation
        if (!brand.getText().toString().isEmpty() &&
                !model.getText().toString().isEmpty() &&
                !version.getText().toString().isEmpty() &&
                Patterns.WEB_URL.matcher(www.getText().toString()).matches())
            return true;
        else {
            //adding information which fields are incorrect
            String errors = "B????dne dane: ";
            if(brand.getText().toString().isEmpty()) errors += "marka ";
            if(model.getText().toString().isEmpty()) errors += "model ";
            if(version.getText().toString().isEmpty()) errors += "wersja ";
            if(!Patterns.WEB_URL.matcher(www.getText().toString()).matches()) errors += "www ";
            showToast(errors);
            return false;
        }
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }


//region SAVE BUTTON METHODS

    private void addSmartphone() {
        ContentValues wartosci = new ContentValues();

        wartosci.put("brand", brand.getText().toString());
        wartosci.put("model", model.getText().toString());
        wartosci.put("www", www.getText().toString());

        String ver = version.getText().toString();
        //adding .0 to the end of version if integer given
        if(!ver.contains(".")) ver += ".0";
        if(ver.endsWith(".")) ver += "0";
        wartosci.put("version", ver);

        //inserting smartphone to database
        getContentResolver().insert(MyContentProvider.URI_CONTENT, wartosci);
        finish();
    }

    private void updateSmartphone() {
        ContentValues values = new ContentValues();

        values.put("brand", brand.getText().toString());
        values.put("model", model.getText().toString());
        values.put("www", www.getText().toString());
        values.put("version", version.getText().toString());

        getContentResolver().update(MyContentProvider.URI_CONTENT, values, DBHelper.ID + " = " + id, null);
        finish();
    }

//endregion



//region BUTTON METHODS

    public void CancelAddingSmartphone(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void GoToWWW(View view) {
        String givenURL = www.getText().toString();
        //checking if given text is URL
        if (Patterns.WEB_URL.matcher(givenURL).matches()) {
            //adding https if necessary
            if (!givenURL.startsWith("https://")) givenURL = "https://" + givenURL;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            //seting URL for browser
            browserIntent.setData(Uri.parse(givenURL));
            //starting browser
            startActivity(browserIntent);
        } else {
            showToast("Podaj poprawny adres");
        }
    }

    public void SaveSmartphone(View view) {
        if (validation()) {
            Intent intentOut = new Intent();
            intentOut.putExtra("requestCode", requestCode);
            //calling the appropriate method depending on the type of activity
            if (requestCode == MainActivity.REQUEST_CREATE_SMARTPHONE) addSmartphone();
            if (requestCode == MainActivity.REQUEST_UPDATE_SMARTPHONE) updateSmartphone();
            setResult(RESULT_OK, intentOut);
            finish();
        }
    }

    public void DeleteSmartphone(View view) {
        ConfirmDialog dialog = new ConfirmDialog();
        //showing confirmation dialog - after pressing YES, method onYesClicked() will be called
        dialog.show(getSupportFragmentManager(), "confirmationDialog");
    }

    //implementation of interface from ConfirmDialog
    @Override
    public void onYesClicked() {
        //deleting element from DB depending on its id
        getContentResolver().delete(MyContentProvider.URI_CONTENT, DBHelper.ID + " = " + id, null);
        Toast.makeText(getBaseContext(), "Usuni??to smartphone.", Toast.LENGTH_SHORT).show();
        setResult(RESULT_CANCELED);
        finish();
    }

//endregion
}