package com.example.tasimwithyouapp.hadas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.tasimwithyouapp.R;
import com.example.tasimwithyouapp.activities.MainActivity;
import com.example.tasimwithyouapp.datasource.AppViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class map extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public map() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment map.
     */
    // TODO: Rename and change types and number of parameters
    public static map newInstance(String param1, String param2) {
        map fragment = new map();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private View.OnClickListener onStationComplete = view -> Toast.makeText(getContext(), "Station Complete", Toast.LENGTH_SHORT).show();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        MainActivity act = (MainActivity) getActivity();
        if (act == null) return view;
        AppViewModel model = act.getAppViewModel();

        ImageButton button1 = view.findViewById(R.id.st1);


        if (model.isStationComplete(1L)) {
            button1.setEnabled(false);
            button1.setImageResource(R.drawable.baseline_location_on_24);
            button1.setOnClickListener(onStationComplete);
        } else {
            button1.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.action_map2_to_station1));
        }


        ImageButton button2 = view.findViewById(R.id.st2);

        if (model.isStationComplete(2L)) {
            button2.setEnabled(false);
            button2.setImageResource(R.drawable.baseline_location_on_24);
            button2.setOnClickListener(onStationComplete);
        } else {
            button2.setOnClickListener(view12 -> Navigation.findNavController(view12).navigate(R.id.action_map2_to_station2));
        }


        ImageButton button3 = view.findViewById(R.id.st3);
        if (model.isStationComplete(3L)) {
            button3.setEnabled(false);
            button3.setImageResource(R.drawable.baseline_location_on_24);
            button3.setOnClickListener(onStationComplete);
        } else {
            button3.setOnClickListener(view13 -> Navigation.findNavController(view13).navigate(R.id.action_map2_to_station3));
        }


        ImageButton button4 = view.findViewById(R.id.st4);
        if (model.isStationComplete(4L)) {
            button4.setEnabled(false);
            button4.setImageResource(R.drawable.baseline_location_on_24);
            button4.setOnClickListener(onStationComplete);
        } else {
            button4.setOnClickListener(view14 -> Navigation.findNavController(view14).navigate(R.id.action_map2_to_station4));
        }

        ImageButton button5 = view.findViewById(R.id.st5);
        if (model.isStationComplete(5L)) {
            button5.setEnabled(false);
            button5.setImageResource(R.drawable.baseline_location_on_24);
            button5.setOnClickListener(onStationComplete);
        } else {
            button5.setOnClickListener(view15 -> Navigation.findNavController(view15).navigate(R.id.action_map2_to_station5));
        }


        ImageButton button6 = view.findViewById(R.id.st6);

        if (model.isStationComplete(6L)) {
            button6.setEnabled(false);
            button6.setImageResource(R.drawable.baseline_location_on_24);
            button6.setOnClickListener(onStationComplete);
        } else {
            button6.setOnClickListener(view16 -> Navigation.findNavController(view16).navigate(R.id.action_map2_to_station6));
        }

        ImageButton button7 = view.findViewById(R.id.st7);
        if (model.isStationComplete(7L)) {
            button7.setEnabled(false);
            button7.setImageResource(R.drawable.baseline_location_on_24);
            button7.setOnClickListener(onStationComplete);
        } else {
            button7.setOnClickListener(view17 -> Navigation.findNavController(view17).navigate(R.id.action_map2_to_station7));
        }
        return view;
    }
}