package com.example.tasimwithyouapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
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

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FragmentManager fragmentManager;
    FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        fragmentManager=getSupportFragmentManager();
        readOrCreatePasswords();
        ///readFlightsList();
        readAllFlights();
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

    private void readAllFlights() {
        String userId = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("flights");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Flight value = postSnapshot.getValue(Flight.class);
                    if (value.getUserId().equals(userId)) {
                        ////
                    }
                }
                /*Object obj = dataSnapshot.getValue(HashMap<String, Flight>.class);
                Flight value = dataSnapshot.getValue(Flight.class);
                value = null;*/
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    JSONObject obj;
    private void readFlightsList() {
        new JsonTask().execute("https://earthquake.usgs.gov/fdsnws/event/1/application.json");
        // tRsFQGsMQrR2
        // https://earthquake.usgs.gov/fdsnws/event/1/[METHOD[?PARAMETERS]]
        // https://earthquake.usgs.gov/fdsnws/event/1/application.json
    }
    private class JsonTask extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            try {
                obj = new JSONObject(result);
                JSONObject arr = obj.getJSONObject("catalogs");
                obj = (JSONObject) obj.get("catalogs");
                obj = null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

   /* public void loadHomePage(){
        fragmentManager.beginTransaction().replace(R.id.signInButton, new Fragmentmenu()).commit();
    }

    */

    /*public void register(View view) {
        fragmentContainerView.
    }*/
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
                            read(view);

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
                                writeUser();
                                Navigation.findNavController(view).navigate(R.id.action_fragmentRegister_to_fragment_flight_adding);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {

                            Toast.makeText(MainActivity.this, "cannot register!`", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void addFlightFunc(View view)throws Exception {
        ///String id=createTransactionID();
        String userId = FirebaseAuth.getInstance().getUid();
        EditText flightNumberText = findViewById(R.id.flightNumberInput);
        String fn = flightNumberText.getText().toString();
        EditText flightPasswordText = findViewById(R.id.flightPasswordInput);
        String fp = flightPasswordText.getText().toString();
        // check if flight number exist in json (to be done)

        //check if password is correct
        if(Passwords.passwordOK(fp)){
            Flight f= new Flight(userId,fn,"24/03/2023 20:35","Russia","3");
          //  FirebaseManager.addUserFlight(f);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("flights").child(f.getUserId());
            myRef.setValue(f);
            User.currentUser.currentFlight = f;
            writeUser(User.currentUser);
            // Navigation.findNavController(view).navigate(R.id.action_fragment_flight_adding_to_fragment_flight_details);
        }
        else
            Toast.makeText(MainActivity.this, "wrong password!`", Toast.LENGTH_LONG).show();
    }
    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    void writeUser() throws Exception {
        String id=mAuth.getCurrentUser().getUid();
        EditText nameText = findViewById(R.id.nameInput);
        String name = nameText.getText().toString();
        EditText addressText = findViewById(R.id.addressInput);
        String address = nameText.getText().toString();
        EditText emailText = findViewById(R.id.emailInput);
        String email = emailText.getText().toString();
        EditText passText = findViewById(R.id.passwordInput);
        String password = passText.getText().toString();

        User user= new User(id,name,address,email,password);
        User.currentUser = user;
        writeUser(user);
    }

    void writeUser(User user) throws Exception {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.id);
        myRef.setValue(user);
    }

   void read(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(mAuth.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.getValue(User.class);
                Toast.makeText(MainActivity.this, value.name,Toast.LENGTH_LONG).show();
                User.currentUser = value;
                if (User.currentUser.currentFlight != null)
                    Navigation.findNavController(view).navigate(R.id.action_fragmentSignInOrRegister_to_fragmentHomePage);
                else
                    Navigation.findNavController(view).navigate(R.id.action_fragmentSignInOrRegister_to_fragment_flight_adding);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    void readOrCreatePasswords(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("passwords");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Passwords value = dataSnapshot.getValue(Passwords.class);
                Passwords.passwords = value;
                //User.currentUser = value;
                if ((value == null) || value.innerPasswords.isEmpty()) {
                    Passwords.create();
                    myRef.setValue(Passwords.passwords);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    /*private void readOrCreatePasswords() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("passwords");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Passwords value = dataSnapshot.getValue(Passwords.class);
                Passwords.passwords = value;
                if ((value == null) || value.innerPasswords.isEmpty()) {
                    Passwords.create();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        }
    }*/
}