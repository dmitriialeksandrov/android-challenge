package com.dmitriialeksandrov.githubapp.content;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Commit extends RealmObject {

    private String repoName;

    @SerializedName("author")
    private Author author;

    @SerializedName("message")
    private String message;

    @NonNull
    public String getRepoName() {
        return repoName;
    }

    @NonNull
    public Author getAuthor() {
        return author;
    }

    @NonNull
    public String getMessage() {
        return message;
    }
}
