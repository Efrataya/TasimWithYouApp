package com.example.tasimwithyouapp.fragments.auth;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tasimwithyouapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;


public class change_password extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText emailVerificationInput = view.findViewById(R.id.password_change_input);
        Button sendEmailButton = view.findViewById(R.id.password_change_button);

        sendEmailButton.setOnClickListener(v -> {
            ProgressDialog d = new ProgressDialog(getContext());
            d.setMessage("Sending email...");
            d.show();
            FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(emailVerificationInput.getText().toString())
                    .addOnSuccessListener(unused -> {
                        d.dismiss();
                        Toast.makeText(getContext(), "Email sent to " + emailVerificationInput.getText().toString(), Toast.LENGTH_SHORT).show();
                        emailVerificationInput.setText(""); // clear the input
                    }).addOnFailureListener(e -> {
                        d.dismiss();
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}