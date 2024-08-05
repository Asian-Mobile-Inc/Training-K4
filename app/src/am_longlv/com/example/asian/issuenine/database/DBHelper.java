package com.example.asian.issuenine.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asian.issuenine.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "User";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_AGE = "age";
    private static final String CREATE_TABLE =
            String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
                    TABLE_NAME, KEY_ID, KEY_NAME, KEY_AGE);
    private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    private static final String SELECT_ALL = String.format("SELECT * FROM %s", TABLE_NAME);
    private static final String SQL_INSERT = "INSERT INTO User(user_name, age) VALUES(?, ?)";
    private static final String SQL_DELETE = "DELETE FROM User WHERE user_id = ?";
    private static final String SQL_DELETE_ALL = "DELETE FROM User";

    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void addUser(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_INSERT, new String[]{userInfo.getUsername(), userInfo.getAge()});
    }

    public void deleteUser(UserInfo userInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE, new String[]{String.valueOf(userInfo.getUserId())});
    }

    public void deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ALL);
    }

    public List<UserInfo> getAllUser() {
        List<UserInfo> userInfoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);
        if (cursor.moveToFirst()) {
            do {
                UserInfo userInfo = new UserInfo(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
                userInfoList.add(userInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userInfoList;
    }
}
