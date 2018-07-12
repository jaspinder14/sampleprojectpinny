package com.mogo.view.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.controller.SharedPref;
import com.mogo.model.radiusprop.RadiusProp;
import com.mogo.model.returnsettingsprop.ReturnSettingsProp;
import com.mogo.view.activities.EventDetail;
import com.mogo.view.activities.HomeActivity;
import com.mogo.view.activities.LoginActivity;
import com.mogo.view.customcontrols.TextViewRegular;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class Settings extends Fragment {

    View view;

    public static Settings instance;

    @BindView(R.id.logoutLL)
    RelativeLayout logoutLL;
    @BindView(R.id.raidusSeekBar)
    SeekBar raidusSeekBar;
    @BindView(R.id.raidusMeterTV)
    TextViewRegular raidusMeterTV;

    int progress = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_settings, container, false);

        instance = this;
        ButterKnife.bind(this, view);

        sharedPreferences = getContext().getApplicationContext().getSharedPreferences(
                "Mogo_Pref", MODE_PRIVATE);

        editor = sharedPreferences.edit();


        Dialogs.baseShowProgressDialog(getContext(), "Loading...");
        WebServiceResult.returnSettings(SharedPref.getInstance(getContext()).getString("userId"));


        // raidusMeterTV.setText("1m");
        raidusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i + 1;
                raidusMeterTV.setText(progress + "Mi");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        HomeActivity.saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogs.baseShowProgressDialog(getContext(), "");
                WebServiceResult.updateRadiusService(SharedPref.getInstance(getContext()).getString("userId"), progress + "");
            }
        });

        return view;
    }

    @OnClick(R.id.logoutLL)
    public void onLogoutClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("LOGOUT");
        builder.setMessage("Are you sure want to logout?");

        // add the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPref.getInstance(getContext()).clear();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static Settings getInstance() {
        return instance;
    }


    public void updateRadius(RadiusProp radiusProp) {
        if (radiusProp != null) {
            Dialogs.baseHideProgressDialog();
            if (radiusProp.getResult().getStatus().equalsIgnoreCase("1")) {
                Dialogs.showToast(getContext(), radiusProp.getResult().getMessage());
            } else {
                Dialogs.showToast(getContext(), radiusProp.getResult().getMessage());
            }
        } else {
            Dialogs.showToast(getContext(), getString(R.string.somethingwrong));
        }
    }

    @SuppressLint("SetTextI18n")
    public void returnSettings(ReturnSettingsProp returnSettingsProp) {
        if (returnSettingsProp != null) {
            Dialogs.baseHideProgressDialog();
            if (returnSettingsProp.getResult().getStatus().equalsIgnoreCase("1")) {
                if (returnSettingsProp.getResult().getData().getRadius().equalsIgnoreCase("")) {
                    raidusMeterTV.setText("0Mi");
                    raidusSeekBar.setProgress(0);
                } else {
                    raidusMeterTV.setText(returnSettingsProp.getResult().getData().getRadius() + "Mi");
                    raidusSeekBar.setProgress(Integer.parseInt(returnSettingsProp.getResult().getData().getRadius()) - 1);
                }

            } else {
                Dialogs.showToast(getContext(), returnSettingsProp.getResult().getMessage());
            }
        } else {
            Dialogs.showToast(getContext(), getString(R.string.somethingwrong));
        }
    }
}
