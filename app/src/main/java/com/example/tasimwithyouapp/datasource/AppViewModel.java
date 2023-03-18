package com.example.tasimwithyouapp.datasource;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.tasimwithyouapp.App;
import com.example.tasimwithyouapp.activities.BaseActivity;
import com.example.tasimwithyouapp.models.CachedFlights;
import com.example.tasimwithyouapp.models.Flight;
import com.example.tasimwithyouapp.models.Passwords;
import com.example.tasimwithyouapp.models.ScheduelingType;
import com.example.tasimwithyouapp.models.ScheduledNotificationHandle;
import com.example.tasimwithyouapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


// The MainActivity owns a copy of viewModel
// Other classes can also access this copy by calling getAppViewModel()
// on an activity that extends MainActivity
public class AppViewModel extends AndroidViewModel {

    private Observer<User> ob;
    // temporary screen variables
    private Flight tempRegisteredFlight;

    // flags to prevent multiple listeners from being attached
    private boolean notificationsScheduled = false;
    public boolean isListenerAttached;

    // Firebase listeners - to be removed on onCleared
    private FirebaseAuth.AuthStateListener authListener;

    private ValueEventListener passwordsListener;
    private ValueEventListener userListener;
    private ValueEventListener flightsListener;

    private ValueEventListener lastFlightNumberListener;
    //   --- -- ----------------- -- --- --

    // App Data - used to store observable data in memory
    private final MutableLiveData<Passwords> passwords = new MutableLiveData<>();
    private final MutableLiveData<List<Flight>> userFlights = new MutableLiveData<>();
    private final MutableLiveData<List<Flight>> allFlights = new MutableLiveData<>();
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Integer> lastFlightNumber = new MutableLiveData<>(0);
    private final MutableLiveData<Exception> exceptions = new MutableLiveData<>();


    public AppViewModel(Application application) {
        super(application);
        attachUserListener();
        readAllFlights();
        readLastFlightNumber();
        attachPasswordsListener();
    }



    public int getLastFlightNumber() {
        if (lastFlightNumber.getValue() != null) return lastFlightNumber.getValue();
        return 0;
    }

    public List<Flight> getAllFlights() {
        if (allFlights.getValue() == null)
            return new ArrayList<>();
        return allFlights.getValue();
    }

    public Flight getFlight(String flightNumber) {
        System.out.println("getFlight: " + flightNumber);
        System.out.println("getFlight: " + getAllFlights().size());
        for (Flight flight : getAllFlights()) {

            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }


    public boolean isStationComplete(Long stationId) {
        User user = currentUser.getValue();
        if (user == null)
            return false;
        return user.isStationComplete(String.valueOf(stationId));
    }

    public void markStationComplete(Long stationId) {
        User user = currentUser.getValue();
        if (user == null)
            return;
        user.markStationComplete("S" + stationId);
        saveUser(user);
    }

    public Flight getTempRegisteredFlight() {
        return tempRegisteredFlight;
    }

    public void setTempRegisteredFlight(Flight flight) {
        tempRegisteredFlight = flight;
    }

    public void saveUser(User user) {
        FirebaseManager.saveUserToDB(user);
        currentUser.postValue(user);
    }

    private void readAllFlights() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null)
            return;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database
                .getReference("flights")
                .child(userId);

        flightsListener = myRef.addValueEventListener(
                FirebaseManager.flightsEventListener(userFlights, exceptions)
        );
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (authListener != null)
            FirebaseAuth.getInstance()
                    .removeAuthStateListener(authListener);
        if (FirebaseAuth.getInstance().getUid() != null
                && userListener != null)
            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .removeEventListener(userListener);
        if (flightsListener != null && FirebaseAuth.getInstance().getUid() != null)
            FirebaseDatabase.getInstance()
                    .getReference("flights")
                    .child(FirebaseAuth.getInstance().getUid())
                    .removeEventListener(flightsListener);
        if (lastFlightNumberListener != null)
            FirebaseDatabase.getInstance()
                    .getReference("lastFlightNumber")
                    .removeEventListener(lastFlightNumberListener);
        if (passwordsListener != null)
            FirebaseDatabase.getInstance()
                    .getReference("passwords")
                    .removeEventListener(passwordsListener);
        if (ob != null)
            currentUser.removeObserver(ob);
    }

    public List<Flight> getUserFlights() {
        if (userFlights.getValue() == null)
            return new ArrayList<>();
        return userFlights.getValue();
    }

    public User getUser() {
        return currentUser.getValue();
    }

    /*
     * Attach a listener to the last flight number's data in the database
     * This listener will be called whenever the user's data is changed
     */
    private void readLastFlightNumber() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lastFlightNumber");
        lastFlightNumberListener = myRef.addValueEventListener(
                FirebaseManager.lastLightNumberEventListener(lastFlightNumber, exceptions)
        );
    }

    /*



     */
    /*
     * Flights api task
     */
    private AsyncTask<String, String, String> executeFlightsTask(Activity activity) {
        return new FlightsApiTask(activity, allFlights)
                .execute("https://app.goflightlabs.com/advanced-flights-\n" +
                        "schedules?access_key=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianR" +
                        "pIjoiOWY1OTM1NzI5ZmI3YTQwZGM0ZTAwMTAwYTk1OGY0OTljNjBmZjMwOTA0YWVjOGE0MWU0NDRhMj" +
                        "FhYjcyMTY1Yjg2YmNhNjhhYzhmYWM4MWYiLCJpYXQiOjE2Nzg0NTM3NzcsIm5iZiI6MTY3ODQ1Mzc3Ny" +
                        "wiZXhwIjoxNzEwMDc2MTc3LCJzdWIiOiIyMDQxNSIsInNjb3BlcyI6W119.ThIidFb2fdho-xw7a6bLBvf2" +
                        "oz3SEKvtt6ooxLSx41Ppsmg5R5mSHv_h5JDRpfywOa95c9rNLuScKvlK9Yh9jA&iataCode=TLV&type=departure");
    }


    /*
     * This method is called when the app is opened.
     * It attaches a listener to the flights list.
     */
    public void readFlightsList(BaseActivity activity) {
        if (!App.isBeta) {
            executeFlightsTask(activity);
            return;
        }
        FirebaseDatabase.getInstance()
                .getReference("cachedFlights")
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (!dataSnapshot.exists()) executeFlightsTask(activity);
                    else {
                        CachedFlights cachedFlights = dataSnapshot.getValue(CachedFlights.class);
                        if (cachedFlights == null || cachedFlights.getCachedFlights() == null || cachedFlights.getCachedFlights().isEmpty()) {
                            executeFlightsTask(activity);
                            return;
                        }
                        allFlights.postValue(cachedFlights.getCachedFlights());
                    }
                }).addOnFailureListener(e -> executeFlightsTask(activity));
    }


    /*
     * This method is called when the app is opened.
     * It attaches a listener to the passwords list.
     */
    private void attachPasswordsListener() {
        passwordsListener = FirebaseManager.passwordsListener(passwords, exceptions);
        FirebaseDatabase.getInstance()
                .getReference("passwords")
                .addValueEventListener(passwordsListener);
    }



    /*
     * This method is called when the app is opened.
     * It checks if the user is logged in and if so, it attaches a listener to the user's data.
     */

    public void attachUserListener() {
        if (userListener != null && FirebaseAuth.getInstance().getUid() != null) {
            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .removeEventListener(userListener);
        }
        if (authListener != null)
            FirebaseAuth.getInstance()
                    .removeAuthStateListener(authListener);
        authListener = firebaseAuth -> {
            if (firebaseAuth.getUid() != null && !isListenerAttached) {
                isListenerAttached = true;
                userListener = FirebaseManager.userEventListener(currentUser, exceptions);
                FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(firebaseAuth.getUid())
                        .addValueEventListener(userListener);
            }
        };

        FirebaseAuth.getInstance().addAuthStateListener(authListener);
    }


    private String getScheduleTypeHebrew(String type) {
        switch (type) {
            case "departure":
                return "תזכורת לטיסה";
            case "passport":
                return "תזכורת לדרכון";
            case "currency_exchange":
                return "תזכורת להחלפת מטבע";
            case "insurance":
                return "תזכורת לביטוח";

            default:
                return "";
        }
    }

    /*
     * This method is called when the user is logged in and the app is opened.
     * It checks if the user has any notifications scheduled and if not, it schedules them.
     */

    public void scheduleNotifications(BaseActivity activity) {
        ob = user -> {
            if (user == null || notificationsScheduled) return;
            List<ScheduledNotificationHandle> handles = new ArrayList<>();
            for (Map.Entry<String, List<Long>> entry : user.getAlerts().entrySet()) {
                String someRandomMsg = getScheduleTypeHebrew(entry.getKey()) + "!";
                for (Long time : entry.getValue())
                    handles.add(
                            new ScheduledNotificationHandle(
                                    time,
                                    someRandomMsg,
                                    ScheduelingType.fromString(entry.getKey()))
                    );
            }
            NotificationHelper.scheduleNotifications(activity, user, handles);
            currentUser.removeObserver(ob);
            notificationsScheduled = true;
            ob = null;
        };
        currentUser.observe(activity, ob);
    }

    public boolean passwordOK(String password) {
        return (passwords.getValue() != null)
                && passwords.getValue().innerPasswords.contains(password);
    }

}
