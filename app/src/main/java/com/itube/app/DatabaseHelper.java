package com.itube.app;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "USER_DATA.db";
    private static final String TABLE_NAME = "user";
    private static final String TABLE_NAME_PLAYLIST = "userPlaylist";
    public static final String COL_ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_PASSWORD = "PASSWORD";
    public static final String COL_CPASSWORD = "CPASSWORD";
    public static final String COL_USERNAME = "USERNAME";
    public static final String COL_YOUTUBE_URL = "YOUTUBE_URL";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT, USERNAME TEXT, PASSWORD TEXT, CPASSWORD TEXT)");
        db.execSQL("CREATE TABLE userPlaylist (ID INTEGER PRIMARY KEY AUTOINCREMENT , USERNAME TEXT, YOUTUBE_URL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_PLAYLIST);
        onCreate(db);
    }

    public boolean InsertUserData(ModelClass modelClass) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_NAME, modelClass.getFullName());
        contentValues.put(COL_USERNAME, modelClass.getUserName());
        contentValues.put(COL_PASSWORD, modelClass.getPassword());
        contentValues.put(COL_CPASSWORD, modelClass.getCpassword());

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean CheckUserName(String USERNAME) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + " = ?", new String[]{USERNAME});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean CheckLogIn(String USERNAME, String PASSWORD) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + "= ? AND " + COL_PASSWORD + " = ? ", new String[]{USERNAME, PASSWORD});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean InsertPlayListData(String USERNAME, String YOUTUBE_URL) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_USERNAME, USERNAME);
        contentValues.put(COL_YOUTUBE_URL, YOUTUBE_URL);

        long result = sqLiteDatabase.insert(TABLE_NAME_PLAYLIST, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean CheckPlayList(String USERNAME, String YOUTUBE_URL) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_PLAYLIST + " WHERE " + COL_USERNAME + "= ? AND " + COL_YOUTUBE_URL + " = ? ", new String[]{USERNAME, YOUTUBE_URL});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public ArrayList<String> getAllPlayListData(String USERNAME) {
        ArrayList<String> dataholder = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_PLAYLIST+ " WHERE " + COL_USERNAME + "= ? ", new String[]{USERNAME});

        if (cursor.moveToNext()) {
            do {
                @SuppressLint("Range") String url =  cursor.getString(cursor.getColumnIndex(COL_YOUTUBE_URL));
                dataholder.add(url);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return dataholder;
    }
}
