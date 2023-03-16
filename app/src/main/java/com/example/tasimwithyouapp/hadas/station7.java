package com.example.tasimwithyouapp.hadas;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.MainActivity;
import com.example.tasimwithyouapp.datasource.AppViewModel;

public class station7 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_station7, container, false);

        ImageButton button1=view.findViewById(R.id.to_76);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_station7_to_station6);
            }
        });



        ImageButton button2=view.findViewById(R.id.to_71);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_station7_to_map2);
            }
        });

        TextView button3=view.findViewById(R.id.gotomap);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_station7_to_map2);
            }
        });
        CheckBox checkBoxStation1 = view.findViewById(R.id.checkBoxStation7);
        MainActivity act = (MainActivity) getActivity();
        if(act == null) return view;
        AppViewModel model = act.getAppViewModel();
        if(model.isStationComplete(7L)) {
            checkBoxStation1.setChecked(true);
            checkBoxStation1.setEnabled(false);
        }
        checkBoxStation1.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setTitle("TasimWithYou")
                        .setMessage("סיימת את המסלול בהצלחה, טיסה נעימה")
                        .setPositiveButton("כן", (dialogInterface, i) -> {
                            model.markStationComplete(7L);
                            NavHostFragment.findNavController(this)
                                    .navigate(R.id.action_station7_to_map2);
                        }).setNegativeButton("לא", null)
                        .create();
                dialog.show();
            }
        });



        return view;
    }
}