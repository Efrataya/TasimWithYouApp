package com.example.tasimwithyouapp.datasource;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.tasimwithyouapp.models.CachedFlights;
import com.example.tasimwithyouapp.models.Flight;
import com.example.tasimwithyouapp.models.Passwords;
import com.example.tasimwithyouapp.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FirebaseManager {




    public static void saveCachedFlightsToDB(List<Flight> flights) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("cachedFlights")
                .setValue(new CachedFlights(flights));
    }
    public static void saveUserToDB(User user) {
        if(FirebaseAuth.getInstance().getUid() == null) return;
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(user);
    }


    public static ValueEventListener passwordsListener(
            MutableLiveData<Passwords> passwords,
            MutableLiveData<Exception> exception
    ) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Passwords value = dataSnapshot.getValue(Passwords.class);
                if ((value == null) || value.innerPasswords.isEmpty()) {
                    value = new Passwords();
                    Passwords.create(value);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("passwords");
                    myRef.setValue(value);
                } else passwords.postValue(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                exception.postValue(error.toException());
            }
        };
    }
    public static ValueEventListener flightsEventListener(
            MutableLiveData<List<Flight>> userFlights,
            MutableLiveData<Exception> exception
    ) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Flight> flights = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Flight value = postSnapshot.getValue(Flight.class);
                    flights.add(value);
                }
                userFlights.postValue(flights);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                exception.postValue(error.toException());
            }
        };
    }

    public static ValueEventListener userEventListener(
            MutableLiveData<User> currentUser,
            MutableLiveData<Exception> exception
    ) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    currentUser.postValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                exception.postValue(error.toException());
            }
        };
    }
    public static ValueEventListener lastLightNumberEventListener(
            MutableLiveData<Integer> lastFlightNumber,
            MutableLiveData<Exception> exception
    ) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Integer number = dataSnapshot.getValue(Integer.class);
                    lastFlightNumber.postValue(number);
                } catch (Exception e) {
                    dataSnapshot.getRef().setValue(lastFlightNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                exception.postValue(error.toException());
            }
        };
    }

}



