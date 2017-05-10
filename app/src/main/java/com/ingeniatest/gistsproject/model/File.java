package com.ingeniatest.gistsproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class File {

    private String id;

    @SerializedName("filename")
    @Expose
    private String filename;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("raw_url")
    @Expose
    private String rawUrl;

    @SerializedName("size")
    @Expose
    private Integer size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
