package com.example.tasimwithyouapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tasimwithyouapp.datasource.AppViewModel;

public abstract class BaseActivity extends AppCompatActivity {
    protected AppViewModel appViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    public AppViewModel getAppViewModel() {
        return appViewModel;
    }
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
