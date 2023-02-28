package com.example.tasimwithyouapp;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseManager {

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
                    if(!snapshot.exists())  {
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
