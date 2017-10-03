package com.dmitriialeksandrov.githubapp.content;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CommitResponseAuthor extends RealmObject {

    @SerializedName("name")
    private String authorName;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @NonNull
    public String getAuthorName() {
        return authorName;
    }

    @Nullable
    public String getAvatarUrl() {
        return avatarUrl;
    }
}
