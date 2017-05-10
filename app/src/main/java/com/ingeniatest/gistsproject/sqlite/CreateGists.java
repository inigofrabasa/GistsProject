package com.ingeniatest.gistsproject.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.ingeniatest.gistsproject.model.Gist;
import com.ingeniatest.gistsproject.presenter.MainPresenter;

import java.util.ArrayList;

public class CreateGists extends AsyncTask<Void, Void, Boolean> {

    private ArrayList<Gist> gists;
    private SQLiteDatabase db;

    public CreateGists(ArrayList<Gist> gists, SQLiteDatabase db) {
        this.gists = gists;
        this.db = db;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        if (gists != null){
            for (Gist gist : gists){
                if ( db != null) {

                    //Check if Gist already Exist
                    String gistQueryFile = "select * from Gist where id = " + "'" + gist.getId().toString() + "'";
                    Cursor cursor = db.rawQuery(gistQueryFile, null);
                    if (cursor != null && cursor.moveToFirst()) {}
                    else{
                        //new Gist Registry to Insert
                        ContentValues gistRegistry = new ContentValues();

                        gistRegistry.put("id", gist.getId());
                        gistRegistry.put("createdAt", MainPresenter.fixDateFormat(gist.getCreatedAt()));
                        gistRegistry.put("description", gist.getDescription());

                        db.insert("Gist", null, gistRegistry);

                        //new Owner Registry to Insert
                        if (gist.getOwner() != null) {
                            ContentValues ownerRegistry = new ContentValues();

                            ownerRegistry.put("id", gist.getOwner().getId());
                            ownerRegistry.put("login", gist.getOwner().getLogin());
                            ownerRegistry.put("avatarUrl", gist.getOwner().getAvatarUrl());

                            db.insert("Owner", null, ownerRegistry);
                        }

                        //new File Registry to Insert
                        if (gist.getFiles().getFiles() != null) {
                            if (gist.getFiles().getFiles().size() > 0) {
                                ContentValues fileRegistry = new ContentValues();

                                fileRegistry.put("id", gist.getFiles().getFiles().get(0).getId());
                                fileRegistry.put("filename", gist.getFiles().getFiles().get(0).getFilename());
                                fileRegistry.put("rawUrl", gist.getFiles().getFiles().get(0).getRawUrl());

                                db.insert("File", null, fileRegistry);
                            }
                        }
                    }
                } else { return false; }
            }
        }
        else{ return false; }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
