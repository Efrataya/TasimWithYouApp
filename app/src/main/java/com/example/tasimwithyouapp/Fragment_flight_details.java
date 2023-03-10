package com.example.tasimwithyouapp;

import static com.example.tasimwithyouapp.R.id.yesThisIsMine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_flight_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_flight_details extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_flight_details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_flight_details.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_flight_details newInstance(String param1, String param2) {
        Fragment_flight_details fragment = new Fragment_flight_details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_flight_details, container, false);
        /*Button button1=view.findViewById(R.id.yesThisIsMine);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/
        TextView tv=view.findViewById(R.id.linkToAddFlight);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragment_flight_details_to_fragmentHome2);
            }
        });
        if ((User.currentUser != null) && (Flight.temp != null)) {
            Flight flight = Flight.temp;
            TextView textView = view.findViewById(R.id.nameText);
            textView.setText(textView.getText().toString() + " " + flight.getFlightNumber());
        }

        return view;
    }
}