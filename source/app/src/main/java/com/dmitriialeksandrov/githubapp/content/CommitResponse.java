package com.dmitriialeksandrov.githubapp.content;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CommitResponse extends RealmObject {

    @SerializedName("commit")
    private Commit commit;

    @SerializedName("author")
    private CommitResponseAuthor author;

    @SerializedName("sha")
    private String sha;

    @NonNull
    public Commit getCommit() {
        return commit;
    }

    @Nullable
    public CommitResponseAuthor getAuthor() {
        return author;
    }

    public String getSha() {
        return sha;
    }
}