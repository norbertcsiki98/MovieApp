package com.example.movieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.movieapp.Movie;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "movie_db";
    private static final String USER_TABLE_NAME = "users";
    private static final String COLUMN_USERNAME = "USERNAME";
    private static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_IMAGE = "IMAGE";

    public static final String TABLE_FAVORITE = "FAVORITE";
    public static final String COLUMN_MOVIEID = "MOVIEID";
    public static final String COLUMN_USER = "USER";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_USERRATING = "USERRATING";
    public static final String COLUMN_POSTER_PATH = "POSTER_PATH";
    public static final String COLUMN_OVERVIEW = "OVERVIEW";
    public static final String COLUMN_RELEASE_DATE = "RELEASE_DATE";
    public static final String _ID = "ID";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + " (" + COLUMN_USERNAME + " TEXT PRIMARY KEY, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_IMAGE + " BLOB);");


        db.execSQL("CREATE TABLE " + TABLE_FAVORITE + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MOVIEID + " INTEGER, " + COLUMN_USER + " TEXT," + COLUMN_TITLE + " TEXT, " +
                COLUMN_USERRATING + " TEXT, " + COLUMN_POSTER_PATH + " TEXT, " + COLUMN_OVERVIEW + " TEXT, " + COLUMN_RELEASE_DATE + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(db);
    }

    public void addFavorite(Movie movie, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIEID, movie.getId());
        values.put(COLUMN_USER, username);
        values.put(COLUMN_TITLE, movie.getTitle());
        values.put(COLUMN_USERRATING, movie.getVote_average());
        values.put(COLUMN_POSTER_PATH, movie.getPoster_path());
        values.put(COLUMN_OVERVIEW, movie.getOverview());
        db.insert(TABLE_FAVORITE, null, values);
        db.close();
    }


    public void deleteFavourite(int id, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FAVORITE + " where " + COLUMN_MOVIEID + " = " + id + " and " + COLUMN_USER + " ='" + username + "';");
    }

    public List<Movie> getAllFavorite(String name) {
        String[] columns = {
                _ID,
                COLUMN_MOVIEID,
                COLUMN_USER,
                COLUMN_TITLE,
                COLUMN_USERRATING,
                COLUMN_POSTER_PATH,
                COLUMN_OVERVIEW,
                COLUMN_RELEASE_DATE
        };
        String sortOrder = _ID + " ASC";
        List<Movie> favoriteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITE, columns, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(cursor.getColumnIndex(COLUMN_USER));
                if (user.equals(name)) {
                    Movie movie = new Movie();
                    movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIEID))));
                    movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    movie.setPoster_path(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
                    movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
                    favoriteList.add(movie);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favoriteList;
    }

    public boolean isFavorite(int id, String userName) {
        String[] columns = {
                _ID,
                COLUMN_MOVIEID,
                COLUMN_USER,
                COLUMN_TITLE,
                COLUMN_USERRATING,
                COLUMN_POSTER_PATH,
                COLUMN_OVERVIEW,
                COLUMN_RELEASE_DATE
        };
        boolean isFavorite = false;
        String selection = COLUMN_MOVIEID + "=" + id + " AND " + COLUMN_USER + "=\"" + userName + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITE, columns, selection, null, null, null, null);
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            isFavorite = true;
        }
        cursor.close();
        db.close();
        return isFavorite;
    }

    public boolean insertUser(String username, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_IMAGE, 0);
        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
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

    public void addImage(String name, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IMAGE, image);
        db.update(USER_TABLE_NAME, contentValues, COLUMN_USERNAME + "='" + name + "'", null);
    }

    public Bitmap getImage(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + COLUMN_IMAGE + " FROM " + USER_TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = '" + username + "' and " + COLUMN_IMAGE + " != 0", null);
        res.moveToNext();
        if (res.moveToFirst()) {
            byte[] imgByte = res.getBlob(0);
            res.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        return null;
    }

    public void changePassword(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PASSWORD, password);
        db.update(USER_TABLE_NAME, contentValues, COLUMN_USERNAME + "='" + name + "'", null);
    }

}
