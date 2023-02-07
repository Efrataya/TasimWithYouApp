package com.example.tasimwithyouapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

    }

    public void signFunc(View view) {

        EditText emailText = findViewById(R.id.emailInput);
        String email = emailText.getText().toString();
        EditText passText = findViewById(R.id.passwordInput);
        String password = passText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "You are signed in", Toast.LENGTH_LONG).show();

                        } else {


                            Toast.makeText(MainActivity.this, "Cannot sign you in!`", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void regFunc(View view) {
        EditText emailText = findViewById(R.id.emailInput);
        String email = emailText.getText().toString();
        EditText passText = findViewById(R.id.passwordInput);
        String password = passText.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                write();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MainActivity.this, "You are in!", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(MainActivity.this, "cannot register!`", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    void write() throws Exception {
        String id=createTransactionID();
        EditText nameText = findViewById(R.id.nameInput);
        String name = nameText.getText().toString();
        EditText addressText = findViewById(R.id.addressInput);
        String address = nameText.getText().toString();
        EditText emailText = findViewById(R.id.emailInput);
        String email = emailText.getText().toString();
        EditText passText = findViewById(R.id.passwordInput);
        String password = passText.getText().toString();
        EditText flightText = findViewById(R.id.flightInput);
        String flight = passText.getText().toString();
        User user= new User(id,name,address,email,password,flight);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.id);
        myRef.setValue(user);
    }
    /*void read(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child("1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.getValue(User.class);
                Toast.makeText(MainActivity.this, value.name,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }*/

}