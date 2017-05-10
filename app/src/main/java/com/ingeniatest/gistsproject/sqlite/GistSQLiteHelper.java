package com.ingeniatest.gistsproject.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GistSQLiteHelper extends SQLiteOpenHelper {

    private final String CREATE_TABLE_GIST
            = "CREATE TABLE Gist (id TEXT PRIMARY KEY NOT NULL, createdAt TEXT, description TEXT)";
    private final String CREATE_TABLE_OWNER
            = "CREATE TABLE Owner (id TEXT PRIMARY KEY NOT NULL, login TEXT, avatarUrl TEXT)";
    private final String CREATE_TABLE_FILE
            = "CREATE TABLE File (id TEXT PRIMARY KEY NOT NULL, filename TEXT, rawUrl TEXT)";

    public GistSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GIST);
        db.execSQL(CREATE_TABLE_OWNER);
        db.execSQL(CREATE_TABLE_FILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Gist");
        db.execSQL("DROP TABLE IF EXISTS Owner");
        db.execSQL("DROP TABLE IF EXISTS File");

        onCreate(db);
    }
}
