package com.example.asian.ex_sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asian.ex_sqlite.model.User;

import java.util.ArrayList;

public class UserSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_AGE = "age";
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table " + TABLE_USER + "( " + COLUMN_USER_ID + " integer primary key autoincrement, " + COLUMN_USER_NAME + " text not null, " + COLUMN_AGE + " integer not null);";
    private static final String[] ALL_COLUMN = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_AGE};

    public UserSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(sqLiteDatabase);
    }

    public User addUser(String name, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_AGE, age);
        long idInsert = db.insert(TABLE_USER, null, values);
        Cursor cursor = db.query(TABLE_USER, ALL_COLUMN, COLUMN_USER_ID + " = " + idInsert, null, null, null, null);
        cursor.moveToFirst();
        User user = cursorToPerson(cursor);
        db.close();
        return user;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> listUser = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USER, ALL_COLUMN, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToPerson(cursor);
            listUser.add(user);
            cursor.moveToNext();
        }
        return listUser;
    }

    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + " = " + id, null);
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER);
    }

    private User cursorToPerson(Cursor cursor) {
        User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
        return user;
    }
}
