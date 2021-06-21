package com.lb.mykijava.data.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PasswordEntry extends RealmObject {
    @PrimaryKey
    private long id;
    private String username;
    private String password;
    private String url;

    public PasswordEntry() {
    }

    public PasswordEntry(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
