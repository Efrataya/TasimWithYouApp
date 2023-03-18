package com.example.tasimwithyouapp.fragments.home;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.MainActivity;
import com.example.tasimwithyouapp.models.Flight;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button button1 = view.findViewById(R.id.letsGoButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentHome_to_fragmentSignInOrRegister);
            }
        });
        MainActivity act = (MainActivity) getActivity();

        if (act != null
                && act.getIntent().getBooleanExtra("navigation", false)) {
            String dest = act.getIntent()
                    .getStringExtra("navigation_dest");
            switch (dest) {
                case "menu":
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_fragmentHome_to_fragmentmenu);
                    break;
                case "update_details":
                    break;
            }
            return view;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading...");
            dialog.show();
            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("currentFlight")
                    .get()
                    .addOnSuccessListener(dataSnapshot -> {
                        if (!dataSnapshot.exists()) {
                            NavHostFragment.findNavController(FragmentHome.this)
                                    .navigate(R.id.action_fragmentHome_to_fragment_flight_adding);
                        } else {
                            Flight f = dataSnapshot.getValue(Flight.class);
                            if (f == null || LocalDateTime.parse(f.getArrivalDate()).plusDays(2).isBefore(LocalDateTime.now())) {
                                NavHostFragment.findNavController(FragmentHome.this)
                                        .navigate(R.id.action_fragmentHome_to_fragment_flight_adding);
                            } else {
                                NavHostFragment.findNavController(FragmentHome.this)
                                        .navigate(R.id.action_fragmentHome_to_fragmentHome2);
                            }
                        }
                        dialog.dismiss();
                    }).addOnFailureListener(e -> {
                        NavHostFragment.findNavController(FragmentHome.this)
                                .navigate(R.id.action_fragmentHome_to_fragment_flight_adding);
                        dialog.dismiss();
                    });

        }
        return view;
    }
}