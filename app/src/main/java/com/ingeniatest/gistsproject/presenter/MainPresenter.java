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
import com.ingeniatest.gistsproject.sqlite.CreateGists;
import com.ingeniatest.gistsproject.sqlite.GistSQLiteHelper;
import com.ingeniatest.gistsproject.sqlite.ObtainGists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainPresenter {

    private MainActivity mainActivity;

    private GistsRequest gistsRequest;
    private ArrayList<Gist> serviceGists;
    private ArrayList<Gist> dataBaseGists;
    private CreateGists createGists;
    private ObtainGists obtainGists;

    //Handler for autoRefresh
    private Handler handler;
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
        try {
            obtainGists = new ObtainGists(db);
            return obtainGists.execute().get();
        } catch (ExecutionException e) {
        } catch (InterruptedException f) {}

        return null;
    }

    private void saveAllGists(ArrayList<Gist> inGists) {
        if (inGists != null) {
            try {
                createGists = new CreateGists(inGists, db);
                Boolean result = createGists.execute().get();
            } catch (ExecutionException e) {
            } catch (InterruptedException f) {}
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

    public static String fixDateFormat(String dateformat){

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
