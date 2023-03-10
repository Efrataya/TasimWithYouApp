package com.example.tasimwithyouapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

interface ICustomSchedule {
    void onScheduleClicked(long time, boolean checked);
}
public class CustomScheduleRv extends RecyclerView.Adapter<CustomScheduleRv.ViewHolder> {

    private List<Long> times;

    private ICustomSchedule listener;
    public CustomScheduleRv(List<Long> times, ICustomSchedule listener) {
        this.times = times;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_time_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        long time = times.get(position);
        holder.scheduleCheckbox.setText(getDateTimeStringFromMilies(time));
        holder.scheduleCheckbox.setOnCheckedChangeListener((compoundButton, b) -> listener.onScheduleClicked(time, b));
    }

    public static String getDateTimeStringFromMilies(long milies) {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(milies), ZoneId.systemDefault());
        return time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox scheduleCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleCheckbox = itemView.findViewById(R.id.notification_custom);
        }
    }
}
