package com.example.tasimwithyouapp.fragments.notifications;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tasimwithyouapp.activities.BaseActivity;
import com.example.tasimwithyouapp.datasource.AppViewModel;
import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.MainActivity;
import com.example.tasimwithyouapp.models.ScheduelingType;
import com.example.tasimwithyouapp.models.User;

public class Choose_notitication extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_notitication, container, false);
    }


    public void openSchedule(ScheduelingType type) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) return;
        AppViewModel vm = activity.getAppViewModel();
        User copy = new User(vm.getUser());
        ChooseScheduelingDialog dialog = new ChooseScheduelingDialog(
                (BaseActivity)getActivity(),
                vm,
                type,
                copy,
                copy.currentFlight);
        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button passportSchedule = view.findViewById(R.id.buttonPassportReminder);
        Button currencySchedule = view.findViewById(R.id.buttonCurrencyReminder);
        Button insuranceSchedule = view.findViewById(R.id.buttonInsuranceReminder);
        Button departure = view.findViewById(R.id.buttonDepartureReminder);

        passportSchedule.setOnClickListener(v -> openSchedule(ScheduelingType.PASSPORT));
        currencySchedule.setOnClickListener(v -> openSchedule(ScheduelingType.CURRENCY_CONVERSION));
        insuranceSchedule.setOnClickListener(v -> openSchedule(ScheduelingType.INSURANCE));
        departure.setOnClickListener(v -> openSchedule(ScheduelingType.DEPARTURE));
    }
}