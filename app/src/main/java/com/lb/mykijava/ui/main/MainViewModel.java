package com.lb.mykijava.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.lb.mykijava.data.dao.PasswordsDAO;
import com.lb.mykijava.data.models.PasswordEntry;

import java.util.List;

import io.realm.Realm;

public class MainViewModel extends ViewModel {

    private final PasswordsDAO passwordsDAO;
    private Realm realm;

    public MainViewModel() {
        super();
        realm = Realm.getDefaultInstance();
        passwordsDAO = new PasswordsDAO(realm);

    }

    @Override
    protected void onCleared() {
        realm.close();
        super.onCleared();
    }


    public LiveData<List<PasswordEntry>> getItems() {
        return passwordsDAO.getItems();
    }

    public void delete(PasswordEntry entry) {
        passwordsDAO.deleteItem(entry.getId());
    }

    public void add(PasswordEntry entry) {
        passwordsDAO.addItem(entry);

    }

}