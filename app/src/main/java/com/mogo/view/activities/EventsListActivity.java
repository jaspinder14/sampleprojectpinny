package com.mogo.view.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.controller.SharedPref;
import com.mogo.model.SearchEventProp.SearchEventProp;
import com.mogo.model.returnevent.Event;
import com.mogo.model.returnevent.ReturnEventPrp;
import com.mogo.view.adapters.EventsListAdapter;
import com.mogo.view.customcontrols.EditTextRegular;
import com.mogo.view.customcontrols.TextViewRegular;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventsListActivity extends AppCompatActivity {


    public static EventsListActivity instance;

    @BindView(R.id.mapIV)
    ImageView mapIV;
    @BindView(R.id.titleIV)
    TextViewRegular titleIV;

    @BindView(R.id.eventsListRV)
    RecyclerView eventsListRV;

    EventsListAdapter eventsListAdapter;

    @BindView(R.id.toTV)
    TextViewRegular toTV;
    @BindView(R.id.fromTV)
    TextViewRegular fromTV;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    GPSTracker traker;
    double latitude_service, longitude_service;

    private String currentDate;
    private TimeZone tz;
    List<Event> eventsList;

    @BindView(R.id.searchET)
    EditTextRegular searchET;
    String searchText = "";
    String userId;

    String fromDate = "";
    String toDate = "";

    @BindView(R.id.menu_iconIV)
    ImageView menu_iconIV;
    @BindView(R.id.searchIV)
    ImageView searchIV;
    @BindView(R.id.filterSearchListIV)
    ImageView filterSearchListIV;

    String filterType = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        ButterKnife.bind(this);
        instance = this;
        menu_iconIV.setVisibility(View.GONE);
        filterSearchListIV.setVisibility(View.VISIBLE);
        mapIV.setImageResource(R.drawable.locationtop);
        titleIV.setText(getString(R.string.home));

        userId = SharedPref.getInstance(this).getString("userId");
        searchText = getIntent().getStringExtra("searchText");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(new Date());
        tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT));

        traker = new GPSTracker(EventsListActivity.this);
        if (traker.isGPSEnabled) {
            loadMapView();
        } else {
            traker.showSettingsAlert();
        }

        // Dialogs.baseShowProgressDialog(EventsListActivity.this, "Loading...");
        if (searchText == null) {
            WebServiceResult.returnListEvents(currentDate, SharedPref.getInstance(this).getString("userId"), latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
        } else {
            WebServiceResult.searchEventService(currentDate, userId, searchText, fromDate, toDate, filterType);
        }

        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText = searchET.getText().toString().trim();
                    if (searchText.isEmpty()) {
                        WebServiceResult.returnListEvents(currentDate, SharedPref.getInstance(EventsListActivity.this).getString("userId"), latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchET.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    } else {
                        //Dialogs.baseShowProgressDialog(EventsListActivity.this, "Loading...");
                        WebServiceResult.searchEventService(currentDate, userId, searchText, fromDate, toDate, filterType);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchET.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }

                    return true;
                }
                return false;
            }
        });

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = searchET.getText().toString().trim();
                if (fromDate.equalsIgnoreCase("") && toDate.equalsIgnoreCase("")) {
                    Dialogs.showToast(EventsListActivity.this, "Please select the start and the end date to search the events");
                } else {
                    WebServiceResult.searchEventService(currentDate, userId, searchText, fromDate, toDate, filterType);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchET.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
            }
        });

        filterSearchListIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    {
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(EventsListActivity.this);
                        builderSingle.setTitle("Search By:");
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EventsListActivity.this, android.R.layout.select_dialog_item);
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

                                Toast.makeText(EventsListActivity.this, "Search By: " + strName, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        builderSingle.show();
                    }
                }
            }
        });
    }

    public void searchEvents(SearchEventProp searchEventProp) {
        if (searchEventProp != null) {
            //   Dialogs.baseHideProgressDialog();
            if (searchEventProp.getResult().getStatus().equalsIgnoreCase("1")) {
                eventsList = searchEventProp.getResult().getData().getEvents();
                eventsListAdapter = new EventsListAdapter(this, eventsList);
                eventsListRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                eventsListRV.setAdapter(eventsListAdapter);
            } else {
                Dialogs.showToast(this, searchEventProp.getResult().getMessage());
            }
        } else {
            Dialogs.showToast(this, "");
        }
    }

    private void loadMapView() {
        traker = new GPSTracker(EventsListActivity.this);

        latitude_service = traker.getLatitude();
        longitude_service = traker.getLongitude();

        Log.d("latatiude", String.valueOf(latitude_service));
        Log.d("longitude", String.valueOf(longitude_service));
    }

    public static EventsListActivity getInstance() {
        return instance;
    }

    @OnClick(R.id.mapIV)
    public void onLocationClick() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.toTV)
    public void setToDate() {

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        String formatOne = "MM-dd-yyyy";

        SimpleDateFormat sdfNew = new SimpleDateFormat(formatOne, Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        toTV.setText(sdfNew.format(myCalendar.getTime()));
        toTV.setTextColor(Color.BLACK);
        toDate = sdf.format(myCalendar.getTime());
    }

    private void updateLabelnew() {
        String myFormat = "yyyy-MM-dd";
        String formatOne = "MM-dd-yyyy";
        SimpleDateFormat sdfNew = new SimpleDateFormat(formatOne, Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromTV.setText(sdfNew.format(myCalendar.getTime()));
        fromTV.setTextColor(Color.BLACK);
        fromDate = sdf.format(myCalendar.getTime());
    }

    @OnClick(R.id.fromTV)
    public void setFromDate() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelnew();
            }
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void returnEvents(ReturnEventPrp returnEventPrp) {
        if (returnEventPrp != null) {
            //  Dialogs.baseHideProgressDialog();
            if (returnEventPrp.getResult().getStatus().equalsIgnoreCase("1")) {
                eventsList = returnEventPrp.getResult().getData().getEvents();
                eventsListAdapter = new EventsListAdapter(this, eventsList);
                eventsListRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                eventsListRV.setAdapter(eventsListAdapter);
            } else {
                Toast.makeText(this, returnEventPrp.getResult().getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Dialogs.showToast(this, getString(R.string.somethingwrong));
        }
    }

    @Override
    protected void onResume() {
        if (searchText == null) {
            WebServiceResult.returnListEvents(currentDate, SharedPref.getInstance(this).getString("userId"), latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
        } else {
            WebServiceResult.searchEventService(currentDate, userId, searchText, fromDate, toDate, filterType);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fromTV.setText(getString(R.string.from));
        toTV.setText(getString(R.string.to));
    }
}