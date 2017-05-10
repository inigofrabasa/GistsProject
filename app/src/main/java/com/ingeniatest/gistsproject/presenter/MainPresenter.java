package com.ingeniatest.gistsproject.presenter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.ingeniatest.gistsproject.MainActivity;
import com.ingeniatest.gistsproject.model.File;
import com.ingeniatest.gistsproject.model.Files;
import com.ingeniatest.gistsproject.model.Gist;
import com.ingeniatest.gistsproject.model.Owner;
import com.ingeniatest.gistsproject.request.GistsRequest;
import com.ingeniatest.gistsproject.sqlite.GistSQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainPresenter {

    private MainActivity mainActivity;

    private GistsRequest gistsRequest;
    private ArrayList<Gist> serviceGists;
    private ArrayList<Gist> dataBaseGists;

    //Handler for autoRefresh
    Handler handler;
    final Integer MILISECONDS_UPDATE = 900000; // every 15 minutes

    //SQLite
    private SQLiteDatabase db;
    private GistSQLiteHelper gistHelper;

    public MainPresenter(MainActivity mainActivity ) {
        this.mainActivity = mainActivity;

        //Init dataBase
        initDB();

        //remove all
        //removeAllGists();

        //Obtain Gist from DateBase if exists
        dataBaseGists = getAllGists();

        //Obtain Gists from Service
        gistsRequest = new GistsRequest();
        serviceGists = gistsRequest.getGist();
        if (serviceGists != null){

            //prepare Gits from service to match ids
            prepareGits(serviceGists);

            //Save data on DB
            saveAllGists(serviceGists);

            //Get all Data
            dataBaseGists = getAllGists();
            if (dataBaseGists != null)
            {
                mainActivity.bindAdapterData(dataBaseGists);
            }
        }

        setAutorefresh();
    }

    private void prepareGits(ArrayList<Gist> inGists){
        if (inGists != null){
            //Assign ids

            for(Gist gist : inGists){
                if (gist.getOwner() != null) {
                    gist.getOwner().setId(gist.getId());
                }
                if (gist.getFiles().getFiles() != null){
                    if (!gist.getFiles().getFiles().isEmpty()) {
                        for (File file :gist.getFiles().getFiles()) {
                            file.setId(gist.getId());
                        }
                    }
                }
            }
        }
    }

    private void manageDB(){
        //Init DB
        initDB();

        //remove all
        removeAllGists();

        //delete database
        deleteDataBase();

        //Save data on DB
        saveAllGists(serviceGists);

        //Get all Data
        ArrayList<Gist> data = getAllGists();
        if (data != null){}
    }

    private void initDB(){
        //Open Data Base in readable and writable mode
        gistHelper = new GistSQLiteHelper(mainActivity, "GistsDB", null, 1);
        db = gistHelper.getWritableDatabase();
    }

    private ArrayList<Gist> getAllGists(){
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

    private void saveAllGists(ArrayList<Gist> inGists){
        if (inGists != null){
            for (Gist gist : inGists){
                createGistRegistry(gist);
            }
        }
    }

    private void createGistRegistry(Gist gist){
        if ( db != null) {

            //Check if Gist already Exist
            String gistQueryFile = "select * from Gist where id = " + "'" + gist.getId().toString() + "'";
            Cursor cursor = db.rawQuery(gistQueryFile, null);
            if (cursor != null && cursor.moveToFirst()) {}
            else{
                //new Gist Registry to Insert
                ContentValues gistRegistry = new ContentValues();

                gistRegistry.put("id", gist.getId());
                gistRegistry.put("createdAt", fixDateFormat(gist.getCreatedAt()));
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
        }
    }

    private void removeAllGists(){
        try {
            db.delete("File","",null);
        }
        catch(SQLException ex){}

        try{
            db.delete("Owner","",null);
        } catch(SQLException ex){}

        try {
            db.delete("Gist", "", null);
        }catch (SQLException ex){}
    }

    private void deleteDataBase(){
        mainActivity.deleteDatabase("GistsDB");
    }

    private String fixDateFormat(String dateformat){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date;
        try {
            date = format.parse(dateformat);
        } catch (ParseException e) { return "";}

        SimpleDateFormat stringFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatted = stringFormat.format(date);
        return formatted;
    }

    private void setAutorefresh(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshItems(true);
            }
        }, MILISECONDS_UPDATE );
    }

    public void refreshItems(Boolean IsAutomatic) {

        gistsRequest = new GistsRequest();
        ArrayList<Gist> inGists = gistsRequest.getGist();
        if (inGists != null){

            //prepare Gits from service to match ids
            prepareGits(inGists);

            //Save data on DB
            saveAllGists(inGists);

            //Get all Data
            dataBaseGists = getAllGists();
            if (dataBaseGists != null)
            {
                mainActivity.bindUpdateAdapterData(dataBaseGists);
            }
        }

        //Set New Autorefresh
        if (IsAutomatic){
            setAutorefresh();
        }
    }

    public void onDestroy(){
        db.close();
    }

    public interface View {
        void bindAdapterData(ArrayList<Gist> gist);

        void bindUpdateAdapterData(ArrayList<Gist> gist);
    }
}
