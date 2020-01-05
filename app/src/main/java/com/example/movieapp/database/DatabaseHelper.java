package com.example.movieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movie_db";
    private static final String USER_TABLE_NAME = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE =
                "CREATE TABLE " + USER_TABLE_NAME + "("
                        + COLUMN_USERNAME + " TEXT PRIMARY KEY , "
                        + COLUMN_EMAIL + " TEXT, "
                        + COLUMN_PASSWORD + " TEXT"
                        + ")";

        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);

    }

    public boolean insertUser(String username, String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        long ins = db.insert(USER_TABLE_NAME, null, values);
        if (ins == -1) return false;
        else return true;
    }

    //checking if email exists;
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT* FROM " + USER_TABLE_NAME + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    public ArrayList<String> getDatas(String email) {
        ArrayList<String> datas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT* FROM " + USER_TABLE_NAME + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        if (result.moveToFirst()) {
            datas.add(result.getString(result.getColumnIndex(COLUMN_USERNAME)));
            datas.add(result.getString(result.getColumnIndex(COLUMN_EMAIL)));
            datas.add(result.getString(result.getColumnIndex(COLUMN_PASSWORD)));
            return datas;
        }
        return null;
    }

    //checking the email and the password(Login Fragment)
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (email != null && password != null) {
            cursor = db.rawQuery("select * from " + USER_TABLE_NAME + " where " + COLUMN_USERNAME +
                    "=?" + " and " + COLUMN_PASSWORD + " =?", new String[]{email, password});
        }

        return cursor != null && cursor.getCount() > 0;
    }

}
