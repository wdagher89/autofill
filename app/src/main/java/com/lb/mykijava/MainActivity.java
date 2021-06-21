package com.lb.mykijava;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.autofill.AutofillManager;

import com.lb.mykijava.databinding.MainActivityBinding;
import com.lb.mykijava.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
        enableService();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enableService() {
        AutofillManager mAutofillManager=getSystemService(AutofillManager.class);
        if (mAutofillManager != null && !mAutofillManager.hasEnabledAutofillServices()) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE);
            intent.setData(Uri.parse("package:com.example.android.autofill.service"));
            startActivityForResult(intent, 1234556);
        }
    }
}