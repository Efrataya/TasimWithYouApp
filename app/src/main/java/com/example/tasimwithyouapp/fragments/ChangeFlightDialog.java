package com.example.tasimwithyouapp.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.BaseActivity;
import com.example.tasimwithyouapp.activities.MainActivity;
import com.example.tasimwithyouapp.datasource.AppViewModel;
import com.example.tasimwithyouapp.datasource.FlightsAdapter;
import com.example.tasimwithyouapp.models.Flight;
import com.example.tasimwithyouapp.models.User;
import com.google.android.gms.tasks.OnSuccessListener;

public class ChangeFlightDialog extends AlertDialog {

    public ChangeFlightDialog(
            @NonNull BaseActivity context,
            AppViewModel appViewModel) {
        super(context);

        User currentUser = appViewModel.getUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "אינך מחובר!", Toast.LENGTH_LONG).show();
            dismiss();
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.fragment_flight_adding, null);
        TextView title = view.findViewById(R.id.textView14);
        TextView flightTitle = view.findViewById(R.id.flightText3);
        flightTitle.setVisibility(View.VISIBLE);
        RecyclerView flightsRv = view.findViewById(R.id.flightsRv);
        flightsRv.setVisibility(View.VISIBLE);
        FlightsAdapter flightsAdapter = new FlightsAdapter(currentUser.getFlights(), new OnSuccessListener<Flight>() {
            @Override
            public void onSuccess(Flight flight) {
                appViewModel.changeCurrentFlight(flight.getFlightNumber());
                dismiss();
                context.finish();
                context.startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
        flightsRv.setAdapter(flightsAdapter);
        title.setText("הוספת טיסה");
        title.setPadding(20, 20, 20, 20);
        EditText flightNumber = view.findViewById(R.id.flightNumberInput);
        EditText flightPass = view.findViewById(R.id.flightPasswordInput);
        Button flightBtnSend = view.findViewById(R.id.addFlightButton);
        flightBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flightNumber.getText().toString().isEmpty()) {
                    flightNumber.requestFocus();
                    Toast.makeText(getContext(), "יש למלא מספר טיסה!", Toast.LENGTH_LONG).show();
                    return;
                } else if (flightPass.getText().toString().isEmpty()) {
                    flightPass.requestFocus();
                    Toast.makeText(getContext(), "יש למלא את שדה הסיסמא", Toast.LENGTH_LONG).show();
                    return;
                }
                if (appViewModel.passwordOK(flightPass.getText().toString())) {
                    Flight flight = appViewModel.getFlight(flightNumber.getText().toString());
                    if (flight == null) {
                        Toast.makeText(getContext(), "טיסה לא נמצאה!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    flight.setUserId(currentUser.id);
                    appViewModel.addFlight(flight);
                    context.finish();
                    context.startActivity(new Intent(getContext(), MainActivity.class));
                    Toast.makeText(getContext(), "טיסה נוספה בהצלחה", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "סיסמא שגויה!", Toast.LENGTH_LONG).show();
                }
            }
        });
        setView(view);

        setButton(BUTTON_POSITIVE, "סגור", (dialog, which) -> {

        });
    }

}
