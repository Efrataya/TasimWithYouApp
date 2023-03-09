package com.example.tasimwithyouapp;



import androidx.annotation.NonNull;

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


    public static ValueEventListener getUsersFlights(
            OnSuccessListener<List<Flight>> onSuccess,
            OnFailureListener onFailure) {
        String userId = FirebaseAuth.getInstance().getUid();
        return FirebaseDatabase.getInstance()
                .getReference("flights")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Flight> flights = new ArrayList<>();
                        for (DataSnapshot flightSnapshot : snapshot.getChildren()) {
                            flights.add(flightSnapshot.getValue(Flight.class));
                        }
                        onSuccess.onSuccess(flights);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onFailure.onFailure(error.toException());
                    }
                });

    }

    public static void addUserFlight(Flight flight,
                                     OnSuccessListener<Void> onSuccess,
                                     OnFailureListener onFailure) {
        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("flights")
                .child(userId)
                .push();
        flight.setFlightNumber(reference.getKey());
        reference.setValue(flight)
                .addOnFailureListener(onFailure)
                .addOnSuccessListener(onSuccess);
    }

    public static void getFlightByNumber(
            String flightNumber,
            OnSuccessListener<Flight> onSuccess,
            OnFailureListener onFailure
    ) {
        FirebaseDatabase.getInstance()
                .getReference("flights")
                .child(flightNumber)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) {
                        // wrong flight number
                        onFailure.onFailure(new Exception("There is not flight under flight-number " + flightNumber));
                        return;
                    }
                    // a flight was found with flightNubmer
                    onSuccess.onSuccess(snapshot.getValue(Flight.class));
                })
                .addOnFailureListener(onFailure);
    }
}



