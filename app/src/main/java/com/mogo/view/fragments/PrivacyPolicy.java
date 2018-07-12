package com.mogo.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mogo.R;
import com.mogo.view.customcontrols.TextViewRegular;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PrivacyPolicy extends Fragment {

    View view;

    @BindView(R.id.privacyPolicyTV)
    TextViewRegular privacyPolicyTV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);


        ButterKnife.bind(this, view);


        return view;
    }

}
