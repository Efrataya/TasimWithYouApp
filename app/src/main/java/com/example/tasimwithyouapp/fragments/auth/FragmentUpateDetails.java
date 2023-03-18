package com.example.tasimwithyouapp.fragments.auth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tasimwithyouapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentUpateDetails extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_details);
        EditText nameInput = findViewById(R.id.nameInput_edit);
        EditText addressInput = findViewById(R.id.addressInput_edit);
        Button saveChangesButton = findViewById(R.id.updateButton);
        ProgressDialog d = new ProgressDialog(this);
        d.setMessage("Loading...");
        d.show();
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (!dataSnapshot.exists())
                    {
                        d.dismiss();
                        return;
                    }
                    d.dismiss();
                    nameInput.setText(dataSnapshot.child("name").getValue().toString());
                    addressInput.setText(dataSnapshot.child("address").getValue().toString());
                }).addOnFailureListener(e -> {
                    d.dismiss();
                });

        saveChangesButton.setOnClickListener(v -> {
            if (nameInput.getText().toString().isEmpty() || addressInput.getText().toString().isEmpty()) {
                Toast.makeText(FragmentUpateDetails.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            d.setMessage("Saving changes...");
            d.show();
            FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("name")
                    .setValue(nameInput.getText().toString())
                    .addOnSuccessListener(unused -> FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child("address")
                            .setValue(addressInput.getText().toString())
                            .addOnSuccessListener(unused1 -> {
                                d.dismiss();
                                Toast.makeText(FragmentUpateDetails.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                d.dismiss();
                                Toast.makeText(FragmentUpateDetails.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }))
                    .addOnFailureListener(e -> {
                        d.dismiss();
                        Toast.makeText(FragmentUpateDetails.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId() == R.id.second_menu_item_1) {
           finish();
           return true;
       }
       return super.onOptionsItemSelected(item);
    }
}
