package com.example.tasimwithyouapp.datasource;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.models.Flight;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.FlightsViewHolder> {

    List<Flight> flightList;
    private  OnSuccessListener<Flight> onFlightSelected;

    public FlightsAdapter(HashMap<String, Flight> flights,
                          OnSuccessListener<Flight> onFlightSelected) {
        this.flightList = new ArrayList<>(flights.values());
        this.onFlightSelected = onFlightSelected;
    }

    @NonNull
    @Override
    public FlightsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_item, parent, false);
        return new FlightsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightsViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.flightNumber.setText("מספר טיסה: " + flight.getFlightNumber());
        holder.flightDate.setText("תאריך: " + flight.getFlightDate().split("T")[0] + " " + flight.getFlightDate().split("T")[1].substring(0, 5));
        holder.flightAirportCode.setText(flight.getFlightDestination() + ": שדה תעופה" );
        holder.flightChoose.setOnClickListener(v -> {
            onFlightSelected.onSuccess(flight);
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    class FlightsViewHolder extends RecyclerView.ViewHolder {


        TextView flightNumber;
        TextView flightDate;
        TextView flightAirportCode;

        Button flightChoose;
        public FlightsViewHolder(@NonNull View itemView) {
            super(itemView);
            flightNumber = itemView.findViewById(R.id.flight_number_item);
            flightDate = itemView.findViewById(R.id.date_item);
            flightAirportCode = itemView.findViewById(R.id.airportCode_item);
            flightChoose = itemView.findViewById(R.id.chooseFlightCurrent);
        }
    }
}
