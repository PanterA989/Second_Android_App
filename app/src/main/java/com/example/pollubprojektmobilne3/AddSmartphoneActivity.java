package com.example.pollubprojektmobilne3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddSmartphoneActivity extends AppCompatActivity {

    private EditText brand;
    private EditText version;
    private EditText model;
    private EditText www;

    private String operationType;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_smartphone);

        brand = findViewById(R.id.Brand_EditText);
        version = findViewById(R.id.Version_EditText);
        model = findViewById(R.id.Model_EditText);
        www = findViewById(R.id.WWW_EditText);

        handleOperationType();
    }

    //CHANGE
    public void handleOperationType() {
        Bundle bundleIn = getIntent().getExtras();
        operationType = bundleIn.getString("operationType");
        if (operationType.contains("insert"))
            getSupportActionBar().setTitle("Add a smartphone to the DB");
        if (operationType.contains("update")) {
            getSupportActionBar().setTitle("Update DB entry");
            id = bundleIn.getLong("id"); //pobierz ID wpisu w BD przekazane przez poprzednią aktywność

            //Ustaw w text inputach wartości pobrane z BD po indeksie przekazanym z poprzedniej aktywności
            brand.setText(getDBentryColumnValue(id, "brand"));
            model.setText(getDBentryColumnValue(id, "model"));
            version.setText(getDBentryColumnValue(id, "version"));
            www.setText(getDBentryColumnValue(id, "www"));

        }
    }


    //CHANGE
    public String getDBentryColumnValue(long id, String column) {
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            cursor = db.query(true, //distinct
                    DBHelper.TABLE_NAME, //tabela
                    new String[]{DBHelper.ID, DBHelper.BRAND, DBHelper.MODEL, DBHelper.VERSION, DBHelper.WWW}, //kolumny
                    DBHelper.ID + " = " + Long.toString(id), //selection - where
                    null, //selectionArgs
                    null, //group by
                    null, //having
                    null, //order by
                    null); //limit
            startManagingCursor(cursor);
        }
        cursor.moveToFirst();

        String value = "";
        int columnIndex = cursor.getColumnIndexOrThrow(DBHelper.BRAND);

        while (!cursor.isAfterLast()) {
            switch (column) {
                case "brand":
                    columnIndex = cursor.getColumnIndexOrThrow(DBHelper.BRAND);
                    break;
                case "model":
                    columnIndex = cursor.getColumnIndexOrThrow(DBHelper.MODEL);
                    break;
                case "version":
                    columnIndex = cursor.getColumnIndexOrThrow(DBHelper.VERSION);
                    break;
                case "www":
                    columnIndex = cursor.getColumnIndexOrThrow(DBHelper.WWW);
                    break;

            }

            value = cursor.getString(columnIndex);
            cursor.moveToNext();
        }
        return value;

    }

    //CHANGE
    public boolean validate() {
        if (!isDataOK()) {
            showToast("Enter valid data");
            return false;
        } else return true;
    }

    //CHANGE
    public boolean isDataOK() {
        if (brand.getText().toString().length() >= 2 && version.getText().toString().length() >= 3 && version.getText().toString().contains(".") && model.getText().toString().length() >= 1 && Patterns.WEB_URL.matcher( www.getText().toString()).matches()) {
            return true;
        } else return false;
    }

    //CHANGE
    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    //CHANGE
    private void addDBentry() {

        ContentValues wartosci = new ContentValues();
        MyContentProvider dbProvider = new MyContentProvider();
//        EditText brand = (EditText)
//                findViewById(R.id.Brand_EditText);
//        EditText model = (EditText)
//                findViewById(R.id.Model_EditText);
//        EditText www = (EditText)
//                findViewById(R.id.WWW_EditText);
//        EditText version = (EditText)
//                findViewById(R.id.Version_EditText);

        wartosci.put("brand", brand.getText().toString());
        wartosci.put("model", model.getText().toString());
        wartosci.put("www", www.getText().toString());
        wartosci.put("version", version.getText().toString());

        dbProvider.insert(dbProvider.URI_CONTENT, wartosci);
        finish();
    }

    //CHANGE
    private void updateDBentry() {
        ContentValues wartosci = new ContentValues();
        MyContentProvider dbProvider = new MyContentProvider();
//        EditText brand = (EditText)
//                findViewById(R.id.editText_brand);
//        EditText model = (EditText)
//                findViewById(R.id.editText_model);
//        EditText www = (EditText)
//                findViewById(R.id.editText_www);
//        EditText version = (EditText)
//                findViewById(R.id.editText_version);


        wartosci.put("brand", brand.getText().toString());
        wartosci.put("model", model.getText().toString());
        wartosci.put("www", www.getText().toString());
        wartosci.put("version", version.getText().toString());

        getContentResolver().update(dbProvider.URI_CONTENT, wartosci, DBHelper.ID + " = " + Long.toString(id), null);
        finish();
    }



    public void CancelAddingSmartphone(View view) {
    }

    public void GoToWWW(View view) {
    }

    public void SaveSmartphone(View view) {
        if (validate()) {

            Intent intentOut = new Intent();
            intentOut.putExtra("operationType", operationType);
            setResult(RESULT_OK, intentOut);
            if (operationType.startsWith("insert")) addDBentry();
            if (operationType.startsWith("update")) updateDBentry();
            finish();
        }
    }
}