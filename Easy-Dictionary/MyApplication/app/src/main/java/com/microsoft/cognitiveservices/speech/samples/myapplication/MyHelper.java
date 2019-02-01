package com.microsoft.cognitiveservices.speech.samples.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

    private static final String dbname = "Dictionary";

    private static final int version =1;

    public MyHelper(Context context){
        super (context, dbname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE
        String sql = "CREATE TABLE DICTIONARY (_id INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT, MEANING TEXT)";
        db.execSQL(sql);
        //Sample data inserted.

        insertData("diligent","someone who works hard.", db);
    }

    private void insertData(String word, String meaning, SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put("WORD",word);
        values.put("MEANING",meaning);
        database.insert("DICTIONARY", null, values);


    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
