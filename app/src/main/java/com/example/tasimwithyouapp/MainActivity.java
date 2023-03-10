package com.example.tasimwithyouapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    int lastFlightNumber = 0;
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
        readLastFlightNumber();
        readOrCreatePasswords();
        readFlightsList();
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

    private void readLastFlightNumber() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lastFlightNumber");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    lastFlightNumber = dataSnapshot.getValue(Integer.class);
                }
                catch (Exception e) {
                    myRef.setValue(lastFlightNumber);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void readAllFlights() {
        String userId = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("flights").child(userId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User.currentUser.removeAllFlight();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Object v = postSnapshot.getValue();
                    Flight value = postSnapshot.getValue(Flight.class);
                    if (value.getUserId().equals(userId)) {
                        User.currentUser.add(value);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    JSONObject obj;
    private void readFlightsList() {
        ///new JsonTask().execute("https://earthquake.usgs.gov/fdsnws/event/1/application.json");
        new JsonTask().execute("https://app.goflightlabs.com/advanced-flights-\n" +
                "schedules?access_key=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiOWM4\n" +
                "YTY5YTc0ZmZmMDU0YTIxZGE3Y2JlMzhjM2ZhMjhlOTM1ODBlNjAzMjQ5Z\n" +
                "DA0MjhiYjg1NWFkYWU0ODIyMmJjNWJkNTRhNzJkZjZjZGMiLCJpYXQiOjE\n" +
                "2NzgyOTc1NTcsIm5iZiI6MTY3ODI5NzU1NywiZXhwIjoxNzA5OTE5OTU3LCJ\n" +
                "zdWIiOiIyMDM5MiIsInNjb3BlcyI6W119.HxPC3iXb6NYgJ3Xb6sBxIvMpRjg7Vf\n" +
                "8w44vgKNNJ8jZzKrWp5farZWwlSqHI0Wfi1YGGq5hlwHAdNhrFA3w38g&iataCode=TLV&type=departure");

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
                if (result == null)
                    return;
                obj = new JSONObject(result);
                JSONArray arr = obj.getJSONArray("data");
                int len = arr.length();
                for (int i = 0; i < arr.length(); i++)
                {
                    JSONObject jsonObject = arr.getJSONObject(i).getJSONObject("flight");
                    String flightNumber = jsonObject.getString("number");
                    jsonObject = arr.getJSONObject(i).getJSONObject("airline");
                    String airline = jsonObject.getString("name");
                    jsonObject = arr.getJSONObject(i).getJSONObject("departure");
                    String terminal = jsonObject.getString("terminal");
                    String flightDate = jsonObject.getString("scheduledTime");
                    jsonObject = arr.getJSONObject(i).getJSONObject("arrival");
                    String flightDestination = jsonObject.getString("iataCode");
                    Flight flight = new Flight("", flightNumber, flightDate, flightDestination, terminal, airline);
                    Flight.add(flight);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

   /* public void loadHomePage(){
        fragmentManager.beginTransaction().replace(R.id.signInButton, new Fragmentmenu()).commit();
    }

    */


    public void showMyFlights(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.show_flights_dialog);
        dialog.setTitle("Title...");
        ListView myNames = (ListView) dialog.findViewById(R.id.myFlightsList);
        dialog.setCancelable(true);
        ArrayAdapter<Flight> itemsAdapter =
                new ArrayAdapter<Flight>(this, android.R.layout.simple_list_item_1, User.currentUser.allFlights());
        myNames.setAdapter(itemsAdapter);
        dialog.show();
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

    public void myFlightFunc(View view)throws Exception {
        Flight flight = Flight.temp;
        flight.setUserId(User.currentUser.id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("flights").child(flight.getUserId()).child(String.valueOf(lastFlightNumber));
        myRef.setValue(flight);
        lastFlightNumber++;
        myRef = database.getReference("lastFlightNumber");
        myRef.setValue(lastFlightNumber);
        User.currentUser.currentFlight = flight;
        writeUser(User.currentUser);
        User.currentUser.add(flight);
        ///readAllFlights();
        Navigation.findNavController(view).navigate(R.id.action_fragment_flight_details_to_fragmentmenu);
    }
    public void addFlightFunc(View view)throws Exception {
        ///String id=createTransactionID();
        String userId = FirebaseAuth.getInstance().getUid();
        EditText flightNumberText = findViewById(R.id.flightNumberInput);
        String fn = flightNumberText.getText().toString();
        EditText flightPasswordText = findViewById(R.id.flightPasswordInput);
        String fp = flightPasswordText.getText().toString();
        // check if flight number exist in json (to be done)
        Flight flight = Flight.getFlight(fn);
        if (flight != null) {
            //check if password is correct
            if(Passwords.passwordOK(fp)){
                Flight.temp = flight;
                Navigation.findNavController(view).navigate(R.id.action_fragment_flight_adding_to_fragment_flight_details);
            }
            else
                Toast.makeText(MainActivity.this, "wrong password!`", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, "flight not found!`", Toast.LENGTH_LONG).show();
        }

    }
    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    void writeUser() throws Exception {
        String id=mAuth.getCurrentUser().getUid();
        EditText nameText = findViewById(R.id.nameInput);
        String name = nameText.getText().toString();
        EditText addressText = findViewById(R.id.addressInput);
        String address = addressText.getText().toString();
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
                readAllFlights();
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