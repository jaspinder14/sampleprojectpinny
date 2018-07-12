package com.mogo.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.controller.SharedPref;
import com.mogo.model.todayeventsprop.Event;
import com.mogo.model.todayeventsprop.TodayEventsProp;
import com.mogo.view.activities.GPSTracker;
import com.mogo.view.activities.HomeActivity;
import com.mogo.view.adapters.TodayEventsAdapter;
import com.mogo.view.customcontrols.EditTextRegular;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TodayEventsFragment extends Fragment {

    View view;
    public static TodayEventsFragment instance;

    @BindView(R.id.todayEventsRV)
    RecyclerView todayEventsRV;
    @BindView(R.id.searchET)
    EditTextRegular searchET;


    TimeZone tz;
    String currentDate;
    GPSTracker traker;
    double latitude_service, longitude_service;
    String userId;
    List<Event> todaysEventList;
    TodayEventsAdapter todayEventsAdapter;
    String searchText;
    String filterType = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_today_events, container, false);

        instance = this;
        ButterKnife.bind(this, view);

        userId = SharedPref.getInstance(getContext()).getString("userId");

        tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(new Date());
        traker = new GPSTracker(getContext());

        latitude_service = traker.getLatitude();
        longitude_service = traker.getLongitude();


        //  Dialogs.baseShowProgressDialog(getContext(), "Loading...");
        WebServiceResult.returnTodayEventsService(currentDate, userId, latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));

        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText = searchET.getText().toString().trim();
                    if (searchText.isEmpty()) {
                        WebServiceResult.returnTodayEventsService(currentDate, userId, latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchET.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    } else {
                        WebServiceResult.searchTodayEventsService(currentDate, userId, latitude_service + "", longitude_service + "", searchText, filterType);
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchET.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                    return true;
                }
                return false;
            }
        });


        HomeActivity.filterSearchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                    builderSingle.setTitle("Search By:");
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);
                    arrayAdapter.add("Event Name");
                    arrayAdapter.add("Zip Code");
                    arrayAdapter.add("Event Type");
                    arrayAdapter.add("Tag");
                    arrayAdapter.add("City");

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);

                            if (strName != null) {
                                if (strName.equalsIgnoreCase("Event Name")) {
                                    filterType = "1";
                                } else if (strName.equalsIgnoreCase("Zip Code")) {
                                    filterType = "2";
                                } else if (strName.equalsIgnoreCase("Event Type")) {
                                    filterType = "3";
                                } else if (strName.equalsIgnoreCase("Tag")) {
                                    filterType = "4";
                                } else if (strName.equalsIgnoreCase("City")) {
                                    filterType = "5";
                                }
                            }

                            Toast.makeText(getContext(), "Search By: " + strName, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    builderSingle.show();
                }
            }
        });

        //    Dialogs.baseShowProgressDialog(getContext(), "Loading...");
        //   WebServiceResult.returnTodayEventsService(currentDate, userId, latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
        return view;
    }

    public static TodayEventsFragment getInstance() {
        return instance;
    }


    public void returnTodayEvents(TodayEventsProp todayEventsProp) {
        if (todayEventsProp != null) {
//            Dialogs.baseHideProgressDialog();
            if (todayEventsProp.getResult().getStatus().equalsIgnoreCase("1")) {
                if (todayEventsProp.getResult().getData().getEvents().size() == 0) {
                    Toast.makeText(getContext(), "No Events Found", Toast.LENGTH_LONG).show();
                    //  todayEventsRV.setAdapter(null);
                } else {
                    todaysEventList = todayEventsProp.getResult().getData().getEvents();
                    todayEventsAdapter = new TodayEventsAdapter(getContext(), todaysEventList);
                    todayEventsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    todayEventsRV.setAdapter(todayEventsAdapter);
                }
            } else {
                Toast.makeText(getContext(), todayEventsProp.getResult().getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), getString(R.string.somethingwrong), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WebServiceResult.returnTodayEventsService(currentDate, userId, latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
    }
}
