package com.mogo.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.SharedPref;
import com.mogo.model.contactUsProp.ContactUsProp;
import com.mogo.view.customcontrols.EditTextRegular;
import com.mogo.view.customcontrols.TextViewRegular;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class Contact_Fragment extends Fragment {

    View view;
    public static Contact_Fragment instance;

    @BindView(R.id.userNameTV)
    EditTextRegular userNameTV;
    @BindView(R.id.emailAddressTV)
    EditTextRegular emailAddressTV;
    @BindView(R.id.addCommentET)
    EditTextRegular addCommentET;
    @BindView(R.id.createEventBT)
    Button createEventBT;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String UserName;
    private String UserEmail;
    private String UserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        instance = this;

        ButterKnife.bind(this, view);

        sharedPreferences = getActivity().getSharedPreferences(
                "Mogo_Pref", MODE_PRIVATE);

        editor = sharedPreferences.edit();

        getData();

        return view;
    }

    private void getData() {

        UserId = SharedPref.getInstance(getActivity()).getString("userId");
        UserName = sharedPreferences.getString("UserName", "");
        UserEmail = sharedPreferences.getString("UserEmail", "");


        userNameTV.setText(UserName);
        emailAddressTV.setText(UserEmail);

        if (!UserName.isEmpty() && !UserEmail.isEmpty()) {
            userNameTV.setFocusable(false);
            userNameTV.setClickable(false);

            emailAddressTV.setClickable(false);
            emailAddressTV.setFocusable(false);
        } else {
            userNameTV.setFocusable(true);
            userNameTV.setClickable(true);

            emailAddressTV.setClickable(true);
            emailAddressTV.setFocusable(true);
        }

    }

    @OnClick(R.id.createEventBT)
    public void onContactClick() {

        String Feedback = addCommentET.getText().toString().trim();

        if (userNameTV.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter a Name", Toast.LENGTH_SHORT).show();
        } else if (emailAddressTV.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter an EmailId", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddressTV.getText().toString()).matches()) {
            Toast.makeText(getActivity(), "Please Enter a valid EmailId", Toast.LENGTH_SHORT).show();
        } else if (Feedback.isEmpty()) {
            Toast.makeText(getActivity(), "Please Add a Comment", Toast.LENGTH_SHORT).show();
        } else {
            WebServiceResult.contactUsService(UserId, Feedback, UserEmail, UserName);
        }
    }

    public static Contact_Fragment getInstance() {
        return instance;
    }


    public void contactUs(ContactUsProp contactUsProp) {
        if (contactUsProp != null) {
            if (contactUsProp.getResult().getStatus().equalsIgnoreCase("1")) {
                Toast.makeText(getActivity(), "Message Sent Successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.somethingwrong), Toast.LENGTH_LONG).show();
        }
    }
}
