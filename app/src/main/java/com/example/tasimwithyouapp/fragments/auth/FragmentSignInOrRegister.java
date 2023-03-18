package com.example.tasimwithyouapp.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tasimwithyouapp.R;


public class FragmentSignInOrRegister extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_in_or_register, container, false);
        /*Button button1=view.findViewById(R.id.signInButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_change_password_to_fragmentHome2);
            }
        });*/
        TextView tv=view.findViewById(R.id.linkToRegister);

        TextView tv1 = view.findViewById(R.id.forgotPass);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentSignInOrRegister_to_change_password);
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentSignInOrRegister_to_fragmentRegister);
            }
        });

        ///return inflater.inflate(R.layout.fragment_sign_in_or_register, container, false);
        return view;

    }


}