package com.lb.mykijava.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lb.mykijava.R;
import com.lb.mykijava.adapters.PasswordsListAdapter;
import com.lb.mykijava.data.models.PasswordEntry;
import com.lb.mykijava.databinding.AddPasswordEntryBinding;
import com.lb.mykijava.databinding.MainFragmentBinding;
import com.lb.mykijava.utils.PasswordManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

import static io.realm.Realm.getApplicationContext;

public class MainFragment extends Fragment {

    private MainViewModel viewModel;
    private MainFragmentBinding binding;
    private PasswordsListAdapter adapter;
    private final List<PasswordEntry> items = new ArrayList<>();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        adapter = new PasswordsListAdapter(items);
        binding.recyclerView.setAdapter(adapter);
        binding.setViewModel(viewModel);
        binding.fab.setOnClickListener(v -> showDialog());
        observeChanges();
        return binding.getRoot();
    }

    private void observeChanges() {
        viewModel.getItems().observe(getViewLifecycleOwner(), passwordEntries -> {
            items.clear();
            items.addAll(passwordEntries);
            adapter.notifyDataSetChanged();
        });

        adapter.itemLongClicked.observe(getViewLifecycleOwner(), entry -> {
            viewModel.delete(entry);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AddPasswordEntryBinding binding = AddPasswordEntryBinding
                .inflate(LayoutInflater.from(getActivity()));
        binding.password.setText(PasswordManager.generatePassword(PasswordManager.complexity.HIGH, 10));
        builder.setView(binding.getRoot())

                .setPositiveButton("save", (dialog, which) -> viewModel.add(new PasswordEntry(
                        binding.username.getText().toString(),
                        binding.password.getText().toString(),
                        binding.url.getText().toString()
                )))
                .setNegativeButton("cancel", (dialog, which) -> dialog.cancel());
        builder.create().show();

    }
}

