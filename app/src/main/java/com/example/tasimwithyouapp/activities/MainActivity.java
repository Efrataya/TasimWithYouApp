package com.example.tasimwithyouapp.activities;

import androidx.navigation.Navigation;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.models.Flight;
import com.example.tasimwithyouapp.models.Passwords;
import com.example.tasimwithyouapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends BaseActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        appViewModel.readFlightsList(this);
        appViewModel.scheduleNotifications(this);

    }

    public void showMyFlights(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.show_flights_dialog);
        dialog.setTitle("Title...");
        ListView myNames = dialog.findViewById(R.id.myFlightsList);
        dialog.setCancelable(true);

        ArrayAdapter<Flight> itemsAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        appViewModel.getUserFlights());

        myNames.setAdapter(itemsAdapter);
        dialog.show();
    }


    public void signFunc(View view) {

        EditText emailText = findViewById(R.id.emailInput);
        String email = emailText.getText().toString();
        EditText passText = findViewById(R.id.passwordInput);
        String password = passText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful())
                        read(view);
                    else
                        showToast("Cannot sign you in!");
                });
    }

    public void regFunc(View view) {
        EditText emailText = findViewById(R.id.emailInput);
        String email = emailText.getText().toString();
        EditText passText = findViewById(R.id.passwordInput);
        String password = passText.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please fill all fields!");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        try {
                            writeUser();
                            showToast("You are in!");
                            Navigation.findNavController(view).navigate(R.id.action_fragmentRegister_to_fragment_flight_adding);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast("cannot register!");
                    }
                });
    }

    public void myFlightFunc(View view) {
        Flight flight = appViewModel.getTempRegisteredFlight();
        User currentUser = appViewModel.getUser();
        if (currentUser == null) {
            Toast.makeText(MainActivity.this, "You are not signed in!`", Toast.LENGTH_LONG).show();
            return;
        }
        int lastFlightNumber = appViewModel.getLastFlightNumber();
        flight.setUserId(currentUser.id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database
                .getReference("flights")
                .child(flight.getUserId())
                .child(String.valueOf(lastFlightNumber));
        myRef.setValue(flight);
        lastFlightNumber++;
        myRef = database.getReference("lastFlightNumber");
        myRef.setValue(lastFlightNumber);
        currentUser.currentFlight = flight;
        appViewModel.saveUser(currentUser);
        Navigation.findNavController(view)
                .navigate(R.id.action_fragment_flight_details_to_fragmentmenu);
    }

    public void addFlightFunc(View view) {
        EditText flightNumberText = findViewById(R.id.flightNumberInput);
        String fn = flightNumberText.getText().toString();
        EditText flightPasswordText = findViewById(R.id.flightPasswordInput);
        String fp = flightPasswordText.getText().toString();
        Flight flight = appViewModel.getFlight(fn);
        if (flight != null) {
            //check if password is correct
            if (appViewModel.passwordOK(fp)) {
                appViewModel.setTempRegisteredFlight(flight);
                Navigation.findNavController(view).navigate(R.id.action_fragment_flight_adding_to_fragment_flight_details);
            } else showToast("wrong password!");
        } else showToast("flight not found!");

    }

    void writeUser() {
        if (mAuth.getCurrentUser() == null)
            return;
        String id = mAuth.getCurrentUser().getUid();
        EditText nameText = findViewById(R.id.nameInput);
        String name = nameText.getText().toString();
        EditText addressText = findViewById(R.id.addressInput);
        String address = addressText.getText().toString();
        EditText emailText = findViewById(R.id.emailInput);
        String email = emailText.getText().toString();
        EditText passText = findViewById(R.id.passwordInput);
        String password = passText.getText().toString();
        User user = new User(id, name, address, email, password, new HashMap<>());
        appViewModel.saveUser(user);
    }


    void read(View view) {
        User user = appViewModel.getUser();
        if (user == null) {
            Navigation.findNavController(view).navigate(R.id.action_fragmentSignInOrRegister_to_fragment_flight_adding);
            return;
        }
        Toast.makeText(MainActivity.this, "Hello ," + user.name, Toast.LENGTH_LONG).show();
        if (user.currentFlight != null)
            Navigation.findNavController(view).navigate(R.id.action_fragmentSignInOrRegister_to_fragmentmenu);
        else
            Navigation.findNavController(view).navigate(R.id.action_fragmentSignInOrRegister_to_fragment_flight_adding);
    }


}