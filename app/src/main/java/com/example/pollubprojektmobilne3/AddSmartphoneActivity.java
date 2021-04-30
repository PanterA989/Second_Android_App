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

public class AddSmartphoneActivity extends AppCompatActivity {

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

        prepareActivity();
    }

    public void prepareActivity() {
        Bundle bundle = getIntent().getExtras();
        requestCode = bundle.getInt("requestCode");
        if (requestCode == MainActivity.REQUEST_CREATE_SMARTPHONE)
            getSupportActionBar().setTitle("Dodanie smartfona");
        if (requestCode == MainActivity.REQUEST_UPDATE_SMARTPHONE) {
            getSupportActionBar().setTitle("Aktualizacja smartfona");
            id = bundle.getLong("id");
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = db.query(true, //distinct
                DBHelper.TABLE_NAME, //table
                new String[]{DBHelper.ID, DBHelper.BRAND, DBHelper.MODEL, DBHelper.VERSION, DBHelper.WWW}, //columns
                DBHelper.ID + " = " + Long.toString(id), //selection - where
                null, //selectionArgs
                null, //group by
                null, //having
                null, //order by
                null); //limit
        startManagingCursor(cursor);

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

    public boolean validation() {
        if (brand.getText().toString().length() >= 2 &&
                model.getText().toString().length() >= 1 &&
                version.getText().toString().length() >= 3 &&
                version.getText().toString().contains(".") &&
                Patterns.WEB_URL.matcher(www.getText().toString()).matches())
            return true;
        else {
            showToast("Wprowadź poprawne dane");
            return false;
        }
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void addSmartphone() {
        ContentValues wartosci = new ContentValues();

        wartosci.put("brand", brand.getText().toString());
        wartosci.put("model", model.getText().toString());
        wartosci.put("www", www.getText().toString());
        wartosci.put("version", version.getText().toString());

        getContentResolver().insert(MyContentProvider.URI_CONTENT, wartosci);
        finish();
    }

    private void updateSmartphone() {
        ContentValues values = new ContentValues();
//        MyContentProvider dbProvider = new MyContentProvider();

        values.put("brand", brand.getText().toString());
        values.put("model", model.getText().toString());
        values.put("www", www.getText().toString());
        values.put("version", version.getText().toString());

//        getContentResolver().update(dbProvider.URI_CONTENT, values, DBHelper.ID + " = " + Long.toString(id), null);
        getContentResolver().update(MyContentProvider.URI_CONTENT, values, DBHelper.ID + " = " + Long.toString(id), null);
        finish();
    }

    public void CancelAddingSmartphone(View view) {
        setResult(RESULT_CANCELED);
        showToast("Anulowano");
        finish();
    }

    public void GoToWWW(View view) {
        String givenURL = www.getText().toString();
        if (Patterns.WEB_URL.matcher(givenURL).matches()) {
            if (!givenURL.startsWith("https://")) givenURL = "https://" + givenURL;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(givenURL));
            startActivity(browserIntent);
        } else {
            showToast("Podaj poprawny adres");
        }
    }

    public void SaveSmartphone(View view) {
        if (validation()) {

            Intent intentOut = new Intent();
            intentOut.putExtra("requestCode", requestCode);
            if (requestCode == MainActivity.REQUEST_CREATE_SMARTPHONE) addSmartphone();
            if (requestCode == MainActivity.REQUEST_UPDATE_SMARTPHONE) updateSmartphone();
            setResult(RESULT_OK, intentOut);
            finish();
        }
    }
}