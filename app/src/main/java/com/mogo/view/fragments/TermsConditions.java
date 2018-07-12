package com.mogo.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mogo.R;

import butterknife.ButterKnife;


public class TermsConditions extends Fragment {

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_terms_conditions, container, false);

        ButterKnife.bind(this, view);

        return view;
    }


}
