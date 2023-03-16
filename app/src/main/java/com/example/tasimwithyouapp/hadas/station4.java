
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


public class station4 extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_station4, container, false);

        ImageButton button1=view.findViewById(R.id.to_43);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_station4_to_station3);
            }
        });



        ImageButton button2=view.findViewById(R.id.to_45);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_station4_to_station5);
            }
        });

        TextView button3=view.findViewById(R.id.gotomap);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_station4_to_map2);
            }
        });
        CheckBox checkBoxStation1 = view.findViewById(R.id.checkBoxStation4);
        MainActivity act = (MainActivity) getActivity();
        if(act == null) return view;
        AppViewModel model = act.getAppViewModel();
        if(model.isStationComplete(4L)) {
            checkBoxStation1.setChecked(true);
            checkBoxStation1.setEnabled(false);
        }
        checkBoxStation1.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setTitle("TasimWithYou")
                        .setMessage("תרצה לעבור לתחנה הבאה?")
                        .setPositiveButton("כן", (dialogInterface, i) -> {
                            model.markStationComplete(4L);
                            NavHostFragment.findNavController(this)
                                    .navigate(R.id.action_station4_to_station5);
                        }).setNegativeButton("לא", null)
                        .create();
                dialog.show();
            }
        });


        return view;
    }
}