package com.ingeniatest.gistsproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gist{

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("forks_url")
    @Expose
    private String forksUrl;

    @SerializedName("commits_url")
    @Expose
    private String commitsUrl;

    @SerializedName("git_pull_url")
    @Expose
    private String gitPullUrl;

    @SerializedName("git_push_url")
    @Expose
    private String gitPushUrl;

    @SerializedName("html_url")
    @Expose
    private String htmlUrl;

    @SerializedName("files")
    @Expose
    private Files files;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("comments")
    @Expose
    private Integer comments;

    @SerializedName("user")
    @Expose
    private Object user;

    @SerializedName("comments_url")
    @Expose
    private String commentsUrl;

    @SerializedName("owner")
    @Expose
    private Owner owner;

    @SerializedName("truncated")
    @Expose
    private Boolean truncated;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getForksUrl() {
        return forksUrl;
    }

    public void setForksUrl(String forksUrl) {
        this.forksUrl = forksUrl;
    }

    public String getCommitsUrl() {
        return commitsUrl;
    }

    public void setCommitsUrl(String commitsUrl) {
        this.commitsUrl = commitsUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGitPullUrl() {
        return gitPullUrl;
    }

    public void setGitPullUrl(String gitPullUrl) {
        this.gitPullUrl = gitPullUrl;
    }

    public String getGitPushUrl() {
        return gitPushUrl;
    }

    public void setGitPushUrl(String gitPushUrl) {
        this.gitPushUrl = gitPushUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public Files getFiles() {
        return files;
    }

    public void setFiles(Files files) {
        this.files = files;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Boolean getTruncated() {
        return truncated;
    }

    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }
}
