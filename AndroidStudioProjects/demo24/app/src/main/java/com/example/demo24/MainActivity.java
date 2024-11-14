package com.example.demo24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addData = findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.demo23.provider/book");
                ContentValues values = new ContentValues();
                values.put("name", "A Clash of Kings");
                values.put("author", "George Martin");
                values.put("pages", 1040);
                values.put("price", 22.85);
                Uri newUri = getContentResolver().insert(uri, values);
                if (newUri != null) {
                    newId = newUri.getPathSegments().get(1);
                } else {
                    Log.e("MainActivity", "Failed to insert data");
                }
            }
        });

        Button queryData = findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.demo23.provider/book");
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity", "Book Name: " + name);
                        Log.d("MainActivity", "Author: " + author);
                        Log.d("MainActivity", "Pages: " + pages);
                        Log.d("MainActivity", "Price: " + price);
                    }
                    cursor.close();
                }
            }
        });

        Button updateData = findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newId != null) {
                    Uri uri = Uri.parse("content://com.example.demo23.provider/book/" + newId);
                    ContentValues values = new ContentValues();
                    values.put("name", "A Clash of Kings (Updated)");
                    getContentResolver().update(uri, values, null, null);
                }
            }
        });

        Button deleteData = findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newId != null) {
                    Uri uri = Uri.parse("content://com.example.demo23.provider/book/" + newId);
                    getContentResolver().delete(uri, null, null);
                }
            }
        });
    }
}