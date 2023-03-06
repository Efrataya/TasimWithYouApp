package com.example.tasimwithyouapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
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
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
       /* fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        FragmentSignInOrRegister fragmentSignInOrRegister=new FragmentSignInOrRegister();
        fragmentTransaction.add(R.id.signInButton, fragmentSignInOrRegister).commit();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);
                        Intent intent;
                        intent = new Intent
                                (MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
   /* public void loadHomePage(){
        fragmentManager.beginTransaction().replace(R.id.signInButton, new Fragmentmenu()).commit();
    }

    */

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
                                Toast.makeText(MainActivity.this, "You are in!", Toast.LENGTH_LONG).show();

                              //  writeUser();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {

                            Toast.makeText(MainActivity.this, "cannot register!`", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

/*
    public void addFlightFunc(View view)throws Exception {
        String id=createTransactionID();
        String userId = FirebaseAuth.getInstance().getUid();
        EditText flightNumberText = findViewById(R.id.flightNumberInput);
        String fn = flightNumberText.getText().toString();
        EditText flightPasswordText = findViewById(R.id.flightPasswordInput);
        String fp = flightPasswordText.getText().toString();
        // check if flight number exist in json (to be done)

        //check if password is correct
        if(fp.equals("0556790226")||fp.equals("208681197")){
            Flight f= new Flight(id,userId,fn,"24/03/2023 20:35","Russia","3");
          //  FirebaseManager.addUserFlight(f);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("flights").child(f.getUserId());
            myRef.setValue(f);
        }
        else
            Toast.makeText(MainActivity.this, "wrong password!`", Toast.LENGTH_LONG).show();



    }
    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    void writeUser() throws Exception {
        String id=createTransactionID();
        EditText nameText = findViewById(R.id.nameInput);
        String name = nameText.getText().toString();
        EditText addressText = findViewById(R.id.addressInput);
        String address = nameText.getText().toString();
        EditText emailText = findViewById(R.id.emailInput);
        String email = emailText.getText().toString();
        EditText passText = findViewById(R.id.passwordInput);
        String password = passText.getText().toString();

        User user= new User(id,name,address,email,password);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.id);
        myRef.setValue(user);
    }



   void read(){
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