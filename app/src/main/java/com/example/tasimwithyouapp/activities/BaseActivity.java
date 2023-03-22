package com.example.tasimwithyouapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.datasource.AppViewModel;
import com.example.tasimwithyouapp.fragments.ChangeFlightDialog;
import com.example.tasimwithyouapp.fragments.auth.FragmentUpateDetails;
import com.example.tasimwithyouapp.models.User;
import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {
    protected AppViewModel appViewModel;
    private Menu menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        appViewModel.currentUser.observe(this, user -> {
            if (menu != null && user != null) {
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        });
    }

    public AppViewModel getAppViewModel() {
        return appViewModel;
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.fragmentmenu_btn:
                finish();
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("navigation", true);
                i.putExtra("navigation_dest", "menu");
                startActivity(i);
                return true;
            case R.id.fragmentupdate_btn:
                i = new Intent(this, FragmentUpateDetails.class);
                startActivity(i);
                return true;
            case R.id.fragmentupdate_flight_btn:
                ChangeFlightDialog changeFlightDialog = new ChangeFlightDialog(
                        this,
                        appViewModel
                );
                changeFlightDialog.show();
                return true;
        }
        return true;
    }
}
