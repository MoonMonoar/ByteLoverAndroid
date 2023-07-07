package com.immo2n.bytelover.LocalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBinit extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bl-local.db";
    public DBinit(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DBinit(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Table creation
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY, name TEXT, image TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On upgrade due to version change
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}