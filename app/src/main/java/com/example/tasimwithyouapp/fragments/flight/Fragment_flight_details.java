package com.example.tasimwithyouapp.fragments.flight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tasimwithyouapp.datasource.AppViewModel;
import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.MainActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Fragment_flight_details extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight_details, container, false);

        TextView tv = view.findViewById(R.id.linkToAddFlight);
        tv.setOnClickListener(view1 -> Navigation.findNavController(view1)
                .navigate(R.id.action_fragment_flight_details_to_fragment_flight_adding));
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) return view;
        AppViewModel vm = activity.getAppViewModel();
        if (vm.getTempRegisteredFlight() != null) {
            TextView textView = view.findViewById(R.id.nameText);
            String flightNumber = textView.getText().toString()
                    + " " + vm.getTempRegisteredFlight().getFlightNumber();
            textView.setText(flightNumber);

            TextView destination = view.findViewById(R.id.destText);
            destination.setText(destination.getText().toString() + " " +  vm.getTempRegisteredFlight().getFlightDestination());
            TextView date = view.findViewById(R.id.datelText);
            date.setText(date.getText().toString() + " " +  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                    .format(LocalDateTime.parse(vm.getTempRegisteredFlight().getFlightDate())));
            TextView terminal = view.findViewById(R.id.terminalText);
            terminal.setText(terminal.getText().toString() + " " + vm.getTempRegisteredFlight().getTerminal());
        }
        return view;
    }
}