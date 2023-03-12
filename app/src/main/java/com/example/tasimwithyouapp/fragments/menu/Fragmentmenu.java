package com.example.tasimwithyouapp.fragments.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tasimwithyouapp.fragments.flight.AirportDirections;
import com.example.tasimwithyouapp.R;


public class Fragmentmenu extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragmentmenu, container, false);
        Button button1=view.findViewById(R.id.fragmenuto1);
        button1.setOnClickListener(view12 -> Navigation.findNavController(view12).navigate(R.id.action_fragmentmenu_to_fragmentmenu1));

        Button buttonReminder =view.findViewById(R.id.fragmenuto2);
        buttonReminder.setOnClickListener(view13 -> Navigation.findNavController(view13).navigate(R.id.action_fragmentmenu_to_fragmentreminder));
        Button button2=view.findViewById(R.id.fragmenuto3);
        button2.setOnClickListener(view14 -> Navigation.findNavController(view14).navigate(R.id.action_fragmentmenu_to_fragmentmenu3));

        Button button3=view.findViewById(R.id.fragmenuto4);
        button3.setOnClickListener(view1 -> startActivity(new Intent(getContext(), AirportDirections.class)));


        return view;
    }

}