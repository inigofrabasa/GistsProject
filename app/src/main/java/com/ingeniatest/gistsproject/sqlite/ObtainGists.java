package com.ingeniatest.gistsproject.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.ingeniatest.gistsproject.model.File;
import com.ingeniatest.gistsproject.model.Files;
import com.ingeniatest.gistsproject.model.Gist;
import com.ingeniatest.gistsproject.model.Owner;

import java.util.ArrayList;
import java.util.List;

public class ObtainGists extends AsyncTask<Void, Void, ArrayList<Gist>> {

    private SQLiteDatabase db;

    public ObtainGists(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    protected ArrayList<Gist> doInBackground( Void... voids ) {
        if (db != null){
            String gistQueryOwner ="select * from Gist order by datetime(createdAt) desc";
            Cursor cursor = db.rawQuery(gistQueryOwner, null);
            ArrayList<Gist> inGists = new ArrayList<>();

            if (cursor.moveToFirst()){
                while(cursor.isAfterLast() == false){

                    Gist gist = new Gist();
                    gist.setId(cursor.getString(cursor.getColumnIndex("id")));
                    gist.setCreatedAt(cursor.getString(cursor.getColumnIndex("createdAt")));
                    gist.setDescription(cursor.getString(cursor.getColumnIndex("description")));

                    //Get Owner
                    String selectQueryOwner = "select * from Owner where id = " + "'" + gist.getId().toString() + "'";
                    Cursor ownerCursor = db.rawQuery(selectQueryOwner, null);
                    if (ownerCursor != null && ownerCursor.moveToFirst()) {
                        Owner owner = new Owner();
                        owner.setId(ownerCursor.getString((ownerCursor.getColumnIndex("id"))));
                        owner.setLogin(ownerCursor.getString((ownerCursor.getColumnIndex("login"))));
                        owner.setAvatarUrl(ownerCursor.getString((ownerCursor.getColumnIndex("avatarUrl"))));

                        gist.setOwner(owner);
                    }

                    //Get File
                    String selectQueryFile = "select * from File where id = " + "'" + gist.getId().toString() + "'";
                    Cursor fileCursor = db.rawQuery(selectQueryFile, null);
                    if (fileCursor != null && fileCursor.moveToFirst()) {
                        File file = new File();
                        file.setId(fileCursor.getString((fileCursor.getColumnIndex("id"))));
                        file.setFilename(fileCursor.getString((fileCursor.getColumnIndex("filename"))));
                        file.setRawUrl(fileCursor.getString((fileCursor.getColumnIndex("rawUrl"))));

                        List<File> files = new ArrayList<>();
                        files.add(file);
                        Files mainFiles = new Files(files);
                        gist.setFiles(mainFiles);
                    }

                    inGists.add(gist);
                    cursor.moveToNext();
                }
            }

            return inGists;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Gist> gists) {
        super.onPostExecute(gists);
    }
}
