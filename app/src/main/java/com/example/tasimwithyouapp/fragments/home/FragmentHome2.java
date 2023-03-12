package com.example.tasimwithyouapp.fragments.home;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.tasimwithyouapp.datasource.AppViewModel;
import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.MainActivity;
import com.example.tasimwithyouapp.models.Flight;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class FragmentHome2 extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false);
    }


    Flight flight;
    long until_days, until_hours, until_minutes;

    private static String getNormalTimeString(String time) {
        if (time == null || time.isEmpty()) return "";
        if (time.length() < 2)
            return "0" + time;
        return time;
    }

    private static boolean isPassed(long days, long hours, long minutes) {
        return days <= 0 && hours <= 0 && minutes <= 0;
    }

    private static String getHoursMinutesStringByLocale(Locale locale, LocalDateTime time) {

        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm", locale);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return time.format(formatter);
        }
        return "";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatTextView editTextFlightStatus = view.findViewById(R.id.textView7);
        AppCompatTextView editTextFlightDepartureTime = view.findViewById(R.id.textView14);
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("currentFlight")
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    flight = dataSnapshot.getValue(Flight.class);
                    EditText editTextDays = view.findViewById(R.id.daysTv);
                    EditText editTextHours = view.findViewById(R.id.hoursTv);
                    EditText editTextMinutes = view.findViewById(R.id.minuteTv);
                    editTextFlightDepartureTime.setText(getHoursMinutesStringByLocale(Locale.forLanguageTag("IL"), LocalDateTime.parse(flight.getFlightDate())));
                    // format date
                    String dateStr = flight.getFlightDate();
                    String dateStrArrival = flight.getArrivalDate();
                    MainActivity act = (MainActivity) getActivity();
                    if(act == null) return;
                    AppViewModel vm = act.getAppViewModel();
                    if (dateStrArrival == null || dateStrArrival.isEmpty()) {
                        flight = vm.getFlight(flight.getFlightNumber());
                        dateStrArrival = flight.getArrivalDate();
                    }
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr);
                    LocalDateTime arrivalTime = LocalDateTime.parse(dateStrArrival);
                    System.out.println(arrivalTime);
                    LocalDateTime today = LocalDateTime.now();
                    until_days = today.until(dateTime, ChronoUnit.MILLIS);
                    until_hours = today.until(dateTime, ChronoUnit.MILLIS);
                    until_minutes = today.until(dateTime, ChronoUnit.MILLIS);

                    if (isPassed(until_days, until_hours, until_minutes)) {
                        until_days = today.until(arrivalTime, ChronoUnit.MILLIS);
                        until_hours = today.until(arrivalTime, ChronoUnit.MILLIS);
                        until_minutes = today.until(arrivalTime, ChronoUnit.MILLIS);
                        editTextFlightStatus.setText("נחיתה בעוד:");
                        if (isPassed(until_days, until_hours, until_minutes)) {
                            editTextFlightStatus.setText("טיסה הושלמה בהצלחה");
                            editTextDays.setText("00");
                            editTextHours.setText("00");
                            editTextMinutes.setText("00");
                            return;
                        }
                    }
                    editTextDays.setText(getNormalTimeString(until_days + ""));
                    editTextHours.setText(getNormalTimeString(until_hours + ""));
                    editTextMinutes.setText(getNormalTimeString(until_minutes + ""));


                    new Timer().scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            LocalDateTime today = LocalDateTime.now();
                            until_days = today.until(dateTime, ChronoUnit.DAYS);
                            until_hours = today.until(dateTime, ChronoUnit.HOURS);
                            until_minutes = today.until(dateTime, ChronoUnit.MINUTES);
                            if (isPassed(until_days, until_hours, until_minutes)) {
                                until_days = today.until(arrivalTime, ChronoUnit.DAYS);
                                until_hours = today.until(arrivalTime, ChronoUnit.HOURS);
                                until_minutes = today.until(arrivalTime, ChronoUnit.MINUTES);

                                editTextFlightStatus.setText("נחיתה בעוד:");
                                until_hours %= 24;
                                until_minutes %= 60;

                                if (isPassed(until_days, until_hours, until_minutes)) {
                                    editTextFlightStatus.setText("טיסה הושלמה בהצלחה");
                                    editTextDays.setText("00");
                                    editTextHours.setText("00");
                                    editTextMinutes.setText("00");
                                }
                            }

                            if (getActivity() == null) return;
                            getActivity().runOnUiThread(() -> {
                                editTextDays.setText(getNormalTimeString(until_days + ""));
                                editTextHours.setText(getNormalTimeString(until_hours + ""));
                                editTextMinutes.setText(getNormalTimeString(until_minutes + ""));
                            });
                        }
                    }, 0, 1000);
                });

    }
}