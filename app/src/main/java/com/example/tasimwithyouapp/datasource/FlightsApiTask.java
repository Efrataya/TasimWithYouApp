package com.example.tasimwithyouapp.datasource;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.tasimwithyouapp.models.Flight;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlightsApiTask extends AsyncTask<String, String, String> {

    ProgressDialog pd;
    private MutableLiveData<List<Flight>> flightsListener;

    public FlightsApiTask(Context context, MutableLiveData<List<Flight>> flightsListener) {
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();
        this.flightsListener = flightsListener;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        try {
            if (result == null)
                return;
            JSONObject obj = new JSONObject(result);
            JSONArray arr = obj.getJSONArray("data");
            int len = arr.length();
            List<Flight> flights = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonObject = arr.getJSONObject(i).getJSONObject("flight");
                String flightNumber = jsonObject.getString("number");
                jsonObject = arr.getJSONObject(i).getJSONObject("airline");
                String airline = jsonObject.getString("name");
                jsonObject = arr.getJSONObject(i).getJSONObject("departure");
                String terminal = jsonObject.getString("terminal");
                String flightDate = jsonObject.getString("scheduledTime");
                jsonObject = arr.getJSONObject(i).getJSONObject("arrival");
                String flightDestination = jsonObject.getString("iataCode");
                String flightArrival = jsonObject.getString("scheduledTime");
                Flight flight = new Flight("", flightNumber, flightDate, flightArrival, flightDestination, terminal, airline);
                flights.add(flight);
            }
            flightsListener.postValue(flights);
            flightsListener = null;
            FirebaseManager.saveCachedFlightsToDB(flights);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}