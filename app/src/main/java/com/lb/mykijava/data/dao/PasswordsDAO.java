package com.lb.mykijava.data.dao;

import androidx.lifecycle.LiveData;

import com.lb.mykijava.LiveRealmResults;
import com.lb.mykijava.data.models.PasswordEntry;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class PasswordsDAO {

    private final Realm realm;

    public PasswordsDAO(Realm realm) {
        this.realm = realm;
    }


    public List<PasswordEntry> findItemsByName(String[] text) {
        RealmQuery<PasswordEntry> query = realm.where(PasswordEntry.class)
                .contains("url", text[0], Case.INSENSITIVE);
        for (int i = 1; i < text.length; i++) {
            query.or().contains("url", text[i], Case.INSENSITIVE);
        }
        return realm.copyFromRealm(query.findAll());
    }

    public LiveData<List<PasswordEntry>> getItems() {
        return new LiveRealmResults<>(realm.where(PasswordEntry.class).findAllAsync());
    }

    public void deleteItem(long id) {
        realm.executeTransactionAsync(realm -> realm.where(PasswordEntry.class).equalTo("id", id).findAll().deleteAllFromRealm());
    }

    public void addItem(PasswordEntry entry) {
        Number maxId = realm.where(PasswordEntry.class).max("id");
        if (maxId != null)
            entry.setId(maxId.intValue() + 1);
        realm.executeTransactionAsync(realm -> realm.insert(entry));
    }
}
