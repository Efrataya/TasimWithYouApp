package com.example.tasimwithyouapp.fragments.home;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tasimwithyouapp.datasource.AppViewModel;
import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.MainActivity;
import com.example.tasimwithyouapp.models.CountryData;
import com.example.tasimwithyouapp.models.Flight;
import com.example.tasimwithyouapp.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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

    private float getTimeDiff(LocalDateTime time1, LocalDateTime time2) {
        return ChronoUnit.MILLIS.between(time1, time2);
    }

    private float getFractionWithRespectToTimeLeft(String departure, String arrival) {
        LocalDateTime departureTime = LocalDateTime.parse(departure);
        LocalDateTime arrivalTime = LocalDateTime.parse(arrival);
        float timeDiff = getTimeDiff(departureTime, arrivalTime);
        float timePassed = getTimeDiff(LocalDateTime.now(), departureTime);
        float frac = (float) Math.abs(timePassed / timeDiff);
        System.out.println(frac);
        return frac;
    }

    private void updatePlanePosition(View v, Flight flight) {
        ImageView imageView9 = v.findViewById(R.id.imageView9);
        float currFraction = getFractionWithRespectToTimeLeft(
                flight.getFlightDate(),
                flight.getArrivalDate());
        // 775 is the max width of the plane
        //  + 10 is the left margin
        float pos = 10 + (775 * currFraction);
        imageView9.setX(pos);
    }

    private Flight flight;

    private void startTextViewGreetingAnimation(View v, int direction, boolean firstTime) {
        v.animate()
                .translationX(direction * 1000)
                .setDuration((long) 10 * 1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        v.setTranslationX(direction * -1000);
                        startTextViewGreetingAnimation(v, direction, false);
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {
                    }
                })
                .start();
    }

    private void loadCountryData(Flight flight, ImageView imageView,TextView destTv) {
        CountryData.asyncFromJson(getActivity(),
                flight.getFlightDestination(),
                new OnSuccessListener<CountryData>() {
                    @Override
                    public void onSuccess(CountryData countryData) {
                        System.out.println("Country data loaded");
                        if (getActivity() != null)
                            getActivity().runOnUiThread(() -> {
                                destTv.setText(countryData.getName().getCommon());
                                Picasso.get().load(countryData.getFlags().getPng()).into(imageView);
                                System.out.println(countryData.getFlags().getPng());
                            });
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Country data failed to load");
                        System.out.println(e.getMessage());
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textViewGreet = view.findViewById(R.id.textView8);
        startTextViewGreetingAnimation(textViewGreet, -1, true);
        AppCompatTextView editTextFlightStatus = view.findViewById(R.id.textView7);
        AppCompatTextView editTextFlightDepartureTime = view.findViewById(R.id.textView14);
        AppCompatTextView editTextFlightArrivalTime = view.findViewById(R.id.textView15);

        ImageView imageView = view.findViewById(R.id.imageView8);
        AppViewModel appViewModel = ((MainActivity) getActivity()).getAppViewModel();
        appViewModel.currentUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (flight != null) return;
                flight = user.currentFlight;
                if (flight == null) return;
                loadCountryData(flight, imageView,(TextView) view.findViewById(R.id.textView13));
                editTextFlightArrivalTime.setText(
                        LocalDateTime.parse(flight.getArrivalDate())
                                .format(DateTimeFormatter.ofPattern("HH:mm")));
                FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("currentFlight")
                        .get()
                        .addOnSuccessListener(dataSnapshot -> {
                            flight = dataSnapshot.getValue(Flight.class);
                            if (flight == null)
                                return;

                            EditText editTextDays = view.findViewById(R.id.daysTv);
                            EditText editTextHours = view.findViewById(R.id.hoursTv);
                            EditText editTextMinutes = view.findViewById(R.id.minuteTv);
                            editTextFlightDepartureTime.setText(getHoursMinutesStringByLocale(Locale.forLanguageTag("IL"), LocalDateTime.parse(flight.getFlightDate())));
                            // format date
                            String dateStr = flight.getFlightDate();

                            String dateStrArrival = flight.getArrivalDate();
                            MainActivity act = (MainActivity) getActivity();
                            if (act == null) return;
                            AppViewModel vm = act.getAppViewModel();
                            if (dateStrArrival == null || dateStrArrival.isEmpty()) {
                                flight = vm.getFlight(flight.getFlightNumber());
                                dateStrArrival = flight.getArrivalDate();
                            }
                            LocalDateTime dateTime = LocalDateTime.parse(dateStr);
                            LocalDateTime arrivalTime = LocalDateTime.parse(dateStrArrival);
                            LocalDateTime today = LocalDateTime.now();

                            until_days = today.until(dateTime, ChronoUnit.DAYS);
                            until_hours = today.until(dateTime, ChronoUnit.HOURS);
                            until_minutes = today.until(dateTime, ChronoUnit.MINUTES);
                            until_hours %= 24;
                            until_minutes %= 60;


                            if (isPassed(until_days, until_hours, until_minutes)) {
                                until_days = today.until(arrivalTime, ChronoUnit.DAYS);
                                until_hours = today.until(arrivalTime, ChronoUnit.HOURS);
                                until_minutes = today.until(arrivalTime, ChronoUnit.MINUTES);
                                until_hours %= 24;
                                until_minutes %= 60;
                                editTextFlightStatus.setText("נחיתה בעוד:");
                                if (isPassed(until_days, until_hours, until_minutes)) {
                                    editTextFlightStatus.setText("טיסה הושלמה בהצלחה");
                                    view.findViewById(R.id.endFlight)
                                            .setVisibility(View.VISIBLE);
                                    editTextDays.setText("00");
                                    editTextHours.setText("00");
                                    editTextMinutes.setText("00");
                                    textViewGreet.setText("תודה שבחרתם אותנו");

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
                                    until_hours %= 24;
                                    until_minutes %= 60;

                                    if (isPassed(until_days, until_hours, until_minutes)) {
                                        until_days = today.until(arrivalTime, ChronoUnit.DAYS);
                                        until_hours = today.until(arrivalTime, ChronoUnit.HOURS);
                                        until_minutes = today.until(arrivalTime, ChronoUnit.MINUTES);
                                        until_hours %= 24;
                                        until_minutes %= 60;
                                        editTextFlightStatus.setText("נחיתה בעוד:");
                                        updatePlanePosition(view, flight);

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
        });

    }
}