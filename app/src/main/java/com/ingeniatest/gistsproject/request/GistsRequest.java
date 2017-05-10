package com.ingeniatest.gistsproject.request;

import com.ingeniatest.gistsproject.model.Gist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class GistsRequest {

    public ArrayList<Gist> getGist(){
        try {
            String myUrl
                    = "https://api.github.com/gists/public";

            HttpGetRequest getRequest = new HttpGetRequest();

            ArrayList<Gist> gists = getRequest.execute(myUrl).get();

            return gists;
        }
        catch(ExecutionException e){
            return null;
        }
        catch(InterruptedException f){
            return null;
        }
    }
}
