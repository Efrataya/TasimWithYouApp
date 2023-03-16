package com.example.tasimwithyouapp.hadas;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.MainActivity;
import com.example.tasimwithyouapp.datasource.AppViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class station1 extends Fragment {
    private ArrayList<DataModel> dataSet;

    private RecyclerView recycleView;
    private LinearLayoutManager layoutManager;
    private CustomAdapter addapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_station1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CheckBox checkBoxStation1 = view.findViewById(R.id.checkBoxStation1);
        MainActivity act = (MainActivity) getActivity();
        if(act == null) return;
        AppViewModel model = act.getAppViewModel();
        if(model.isStationComplete(1L)) {
            checkBoxStation1.setChecked(true);
            checkBoxStation1.setEnabled(false);
        }
        checkBoxStation1.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setTitle("TasimWithYou")
                        .setMessage("תרצה לעבור לתחנה הבאה?")
                        .setPositiveButton("כן", (dialogInterface, i) -> {
                            model.markStationComplete(1L);
                            NavHostFragment.findNavController(this)
                                    .navigate(R.id.action_station1_to_station2);
                        }).setNegativeButton("לא", null)
                        .create();
                dialog.show();
            }
        });

        ImageButton button1 = view.findViewById(R.id.to_2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_station1_to_station2);
            }
        });

        TextView b =view.findViewById(R.id.gotomap);
        b.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });


        ImageButton button2 = view.findViewById(R.id.to_last);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_station1_to_station7);
            }
        });


        recycleView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recycleView.setLayoutManager(layoutManager);

        recycleView.setItemAnimator(new DefaultItemAnimator());

        dataSet = new ArrayList<>();

        for (int i = 0; i < MyData.nameArray.length; i++) {
            dataSet.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.id_[i],
                    MyData.drowoableArray[i],
                    null
            ));
        }

        dataSet.get(0).setOnDataModelClick(() -> {
            //32.0055° N, 34.8854° E
            String uri = String.format(Locale.ENGLISH, "%f,%f", 31.9966707, 34.8808808);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://maps.google.com/maps?geo="+uri));
            startActivity(intent);
        });

        dataSet.get(1).setOnDataModelClick(() -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0768605884"));
            startActivity(intent);
        });

        dataSet.get(2).setOnDataModelClick(() -> {
            String url = "https://www.rail.co.il/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        dataSet.get(3).setOnDataModelClick(() -> {
            String url = "https://moovitapp.com/israel-1/poi/he";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        addapter = new CustomAdapter(dataSet);
        recycleView.setAdapter(addapter);
    }
}
