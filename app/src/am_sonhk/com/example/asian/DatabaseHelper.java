package com.example.asian;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "User";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_AGE = "age";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_AGE + " INTEGER" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public long addUser(String name, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_AGE, age);

        long id = db.insert(TABLE_USER, null, values);
        db.close();
        return id;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_USER, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_USER_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
                int ageIndex = cursor.getColumnIndex(COLUMN_USER_AGE);

                do {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    int age = cursor.getInt(ageIndex);
                    users.add(new User(id, name, age));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return users;
    }

    public void deleteUserById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
    }
}
