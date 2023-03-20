package com.example.tasimwithyouapp.fragments.notifications;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.BaseActivity;
import com.example.tasimwithyouapp.datasource.AppViewModel;
import com.example.tasimwithyouapp.datasource.FirebaseManager;
import com.example.tasimwithyouapp.models.Flight;
import com.example.tasimwithyouapp.models.ScheduelingType;
import com.example.tasimwithyouapp.models.ScheduledNotificationHandle;
import com.example.tasimwithyouapp.models.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ChooseScheduelingDialog extends AlertDialog {


    boolean isOther = false, isValidOtherDate = true;
    int year, month, day, hour, minute;


    private static boolean isBeforeFlightDate(int milies, Flight flight) {
        LocalDateTime flightDate = LocalDateTime.parse(flight.getFlightDate());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime date = now.plus(milies, ChronoUnit.MILLIS);
        return date.isBefore(flightDate);
    }

    private static boolean isBeforeFlightSelfChosenDate(int year, int month, int day, int hour, int minute, Flight flight) {
        String dateString = flight.getFlightDate();
        LocalDateTime flightDate = LocalDateTime.parse(dateString);
        LocalDateTime date = LocalDateTime.of(year, month, day, hour, minute);
        return date.isBefore(flightDate);
    }

    CustomScheduleRv adapter;

    public ChooseScheduelingDialog(BaseActivity context,
                                   AppViewModel viewModel,
                                   ScheduelingType scheduelingType,
                                   User user,
                                   Flight flight) {
        super(context);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_choose_time_notification, null, false);
        setView(view);
        CheckBox checkBox2d = view.findViewById(R.id.notification_2d);
        CheckBox checkBox3h = view.findViewById(R.id.notification_3h);
        CheckBox checkBox5h = view.findViewById(R.id.notification_5h);
        CheckBox checkBox24h = view.findViewById(R.id.notification_24h);
        TextView otherTimerHeader = view.findViewById(R.id.customNotificationHeader);

        if (user.hasCustomNotification(scheduelingType))
            otherTimerHeader.setVisibility(View.VISIBLE);
        RecyclerView otherTimesRecyclerView = view.findViewById(R.id.otherTimesRv);
        otherTimesRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        final int minute_milies = 60000;
        final int hour_milies = 3600000;
        final int day_milies = 86400000;
        final int two_day_milies = 2 * day_milies;
        List<Long> otherTimesScheduled = new ArrayList<>(user.getAlertTimes(scheduelingType));

        otherTimesScheduled.remove((long) two_day_milies);
        otherTimesScheduled.remove((long) 3 * hour_milies);
        otherTimesScheduled.remove((long) 5 * hour_milies);
        adapter = new CustomScheduleRv(otherTimesScheduled, (time, checked) -> {
            if (!checked) {
                user.removeAlertTime(scheduelingType, time);
            } else {
                user.addAlertTime(scheduelingType, time);
            }
        });

        otherTimesRecyclerView.setAdapter(adapter);

        checkBox2d.setChecked(user.hasAlertTime(scheduelingType, (long) two_day_milies));
        checkBox3h.setChecked(user.hasAlertTime(scheduelingType, (long) 3 * hour_milies));
        checkBox5h.setChecked(user.hasAlertTime(scheduelingType, (long) 5 * hour_milies));
        checkBox24h.setChecked(user.hasAlertTime(scheduelingType, (long) day_milies));


        checkBox2d.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                user.removeAlertTime(scheduelingType, two_day_milies);
                return;
            }
            if (!isBeforeFlightDate(two_day_milies, flight)) {
                Toast.makeText(context, "התאריך הנבחר להתראה מוכרח להיות לפני זמן המראת הטיסה", Toast.LENGTH_SHORT).show();
                checkBox2d.setChecked(false);
            }
        });

        checkBox3h.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                user.removeAlertTime(scheduelingType, 3 * hour_milies);
                return;
            }
            if (!isBeforeFlightDate(3 * hour_milies, flight)) {
                Toast.makeText(context, "התאריך הנבחר להתראה מוכרח להיות לפני זמן המראת הטיסה", Toast.LENGTH_SHORT).show();
                checkBox3h.setChecked(false);
            }
        });

        checkBox5h.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                user.removeAlertTime(scheduelingType, 5 * hour_milies);
                return;
            }
            if (!isBeforeFlightDate(5 * hour_milies, flight)) {
                Toast.makeText(context, "התאריך הנבחר להתראה מוכרח להיות לפני זמן המראת הטיסה", Toast.LENGTH_SHORT).show();
                checkBox5h.setChecked(false);
            }
        });

        checkBox24h.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                user.removeAlertTime(scheduelingType, day_milies);
                return;
            }
            if (!isBeforeFlightDate(day_milies, flight)) {
                Toast.makeText(context, "התאריך הנבחר להתראה מוכרח להיות לפני זמן המראת הטיסה", Toast.LENGTH_SHORT).show();
                checkBox24h.setChecked(false);
            }
        });

        Button otherButton = view.findViewById(R.id.notification_other);

        otherButton.setOnClickListener(view13 -> {

            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            Button chooseDate = new Button(getContext());
            chooseDate.setText("בחר תאריך");
            Button chooseTime = new Button(getContext());
            chooseTime.setText("בחר שעה");
            layout.addView(chooseDate);
            layout.addView(chooseTime);

            AlertDialog alertDialog = new Builder(getContext()).create();
            alertDialog.setView(layout);


            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                ChooseScheduelingDialog.this.hour = hour;
                ChooseScheduelingDialog.this.minute = minute;
            }, 0, 0, true);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                ChooseScheduelingDialog.this.year = year;
                ChooseScheduelingDialog.this.month = month;
                ChooseScheduelingDialog.this.day = day;
                isOther = true;
            });


            chooseDate.setOnClickListener(view12 -> datePickerDialog.show());
            chooseTime.setOnClickListener(view1 -> timePickerDialog.show());


            alertDialog.setButton(BUTTON_POSITIVE, "אשר מועד התראה מותאם", (dialogInterface, i) -> {
                if (!isBeforeFlightSelfChosenDate(year, month, day, hour, minute, flight)) {
                    isValidOtherDate = false;
                    Toast.makeText(context, "התאריך הנבחר להתראה מוכרח להיות לפני זמן המראת הטיסה", Toast.LENGTH_SHORT).show();
                } else {
                    otherTimesScheduled.add(getMilies(year, month, day, hour, minute));
                    adapter.notifyDataSetChanged();
                    isValidOtherDate = true;
                }
            });
            alertDialog.show();
        });
        setButton(BUTTON_POSITIVE, "שמור שינוים", (dialogInterface, i) -> {

            AlertDialog confirmDialog = new AlertDialog.Builder(getContext()).create();
            View confirmView = getLayoutInflater().inflate(R.layout.okay_alert, null);
            confirmDialog.setView(confirmView);
            TextView textViewTitle = confirmView.findViewById(R.id.confirm_alert_details);
            String selected = "";
            if (checkBox2d.isChecked()) {
                selected += "יומיים לפני המראת הטיסה\n";
            }
            if (checkBox3h.isChecked()) {
                selected += "3 שעות לפני המראת הטיסה\n";
            }
            if (checkBox5h.isChecked()) {
                selected += "5 שעות לפני המראת הטיסה\n";
            }
            if (checkBox24h.isChecked()) {
                selected += "24 שעות לפני המראת הטיסה\n";
            }
            if (isOther) {
                selected += "התראה מותאמת לתאריך ושעה שנבחרו";
                selected += "\n" + day + "/" + month + "/" + year + " " + (hour >= 10 ? hour : "0" + hour) + ":" + (minute >= 10 ? minute : "0" + minute);
            }
            textViewTitle.setText(selected);
            confirmDialog.setButton(BUTTON_POSITIVE, "אשר", (dialogInterface1, i1) -> {

                if (checkBox2d.isChecked()) {
                    user.addAlertTime(scheduelingType, (long) two_day_milies);
                }

                if (checkBox3h.isChecked()) {
                    user.addAlertTime(scheduelingType, (long) 3 * hour_milies);

                }

                if (checkBox5h.isChecked()) {
                    user.addAlertTime(scheduelingType, (long) 5 * hour_milies);

                }

                if (checkBox24h.isChecked()) {
                    user.addAlertTime(scheduelingType, (long) day_milies);
                }

                if (isOther) {
                    if (isValidOtherDate) {
                        // get milies diff from selected date and flight date
                        long diff = getMilies(year, month, day, hour, minute);
                        user.addAlertTime(scheduelingType, diff);
                    }
                }
                FirebaseManager.saveUserToDB(user); // long date, String message, ScheduelingType scheduelingType
                viewModel.scheduleNotifications(context,user);
                Toast.makeText(context, "הגדרות להתראה נקבעו בהצלחה", Toast.LENGTH_SHORT).show();
                confirmDialog.dismiss();
                ChooseScheduelingDialog.this.dismiss();
            });
            confirmDialog.setButton(BUTTON_NEGATIVE, "ביטול", (dialogInterface12, i12) -> {
            });
            confirmDialog.show();
        });


        setButton(BUTTON_NEGATIVE, "ביטול", (dialogInterface, i) -> {
            ChooseScheduelingDialog.this.dismiss();
        });

    }

    private long getMilies(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }
}
