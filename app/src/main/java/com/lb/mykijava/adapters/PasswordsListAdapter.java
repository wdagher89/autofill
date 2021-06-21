package com.lb.mykijava.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.lb.mykijava.data.models.PasswordEntry;
import com.lb.mykijava.databinding.PasswordsListItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;


public class PasswordsListAdapter
        extends RecyclerView.Adapter<PasswordsListAdapter.PasswordEntryViewHolder> {

    private final List<PasswordEntry> data;
    public MutableLiveData<PasswordEntry> itemLongClicked = new MutableLiveData<>();

    public PasswordsListAdapter(List<PasswordEntry> data) {
        super();

        this.data = data;
    }

    @NotNull
    @Override
    public PasswordEntryViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        PasswordsListItemBinding binding = PasswordsListItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new PasswordEntryViewHolder(binding);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(PasswordEntryViewHolder holder,
                                 int position) {
        holder.bind(data.get(position));

    }


    class PasswordEntryViewHolder extends RecyclerView.ViewHolder {
        private final PasswordsListItemBinding binding;

        PasswordEntryViewHolder(PasswordsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PasswordEntry entry) {
            binding.setPasswordEntry(entry);
            binding.getRoot().setOnLongClickListener(v -> {
                itemLongClicked.postValue(entry);
                return true;
            });

        }
    }
}