package com.mogo.view.fragments;

import android.Manifest;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.controller.SharedPref;
import com.mogo.model.SearchEventProp.SearchEventProp;
import com.mogo.model.returnevent.ReturnEventPrp;
import com.mogo.view.activities.EventDetail;
import com.mogo.view.activities.EventsListActivity;
import com.mogo.view.activities.GPSTracker;
import com.mogo.view.activities.HomeActivity;
import com.mogo.view.customcontrols.EditTextRegular;
import com.mogo.view.customcontrols.TextViewRegular;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    View view;
    public static HomeFragment instance;
    GPSTracker traker;
    GoogleMap mMap;
    private View infoWindow;
    public static GoogleApiClient mGoogleApiClient;


    double latitude_service, longitude_service;
    private SupportMapFragment mapFragment;

    @BindView(R.id.toTV)
    TextViewRegular toTV;
    Calendar myCalendar;
    public static double longitude;
    public static double latitude;
    DatePickerDialog.OnDateSetListener date;
    private TextView infoTitle, infoSnippet;

    @BindView(R.id.fromTV)
    TextViewRegular fromTV;
    private TimeZone tz;
    private String currentDate;
    private LatLng newMarkerLatLong;
    private ReturnEventPrp returnEventPrp;
    private TextView textViewname;
    private CircleImageView ivEvent;
    private ImageView ivInfo;

    boolean not_first_time_showing_info_window;

    @BindView(R.id.searchET)
    EditTextRegular searchET;
    String searchText;
    String fromDate = "";
    String toDate = "";

    @BindView(R.id.refreshIV)
    ImageView refreshIV;

    @BindView(R.id.matchingEventsBT)
    Button matchingEventsBT;

    String filterType = "1";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        instance = this;
        ButterKnife.bind(this, view);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(new Date());
        traker = new GPSTracker(getActivity());
        if (traker.isGPSEnabled) {
            loadMapView();
        } else {
            traker.showSettingsAlert();
        }

        HomeActivity.filterSearchListIV.setVisibility(View.VISIBLE);

        HomeActivity.filterSearchListIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText = searchET.getText().toString().trim();
                    if (searchText.isEmpty()) {
                        mMap.clear();
                        WebServiceResult.returnEvents(currentDate, SharedPref.getInstance(getActivity()).getString("userId"), latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchET.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    } else {
                        mMap.clear();
                        WebServiceResult.searchEventsOnMap(currentDate, SharedPref.getInstance(getActivity()).getString("userId"), searchText, fromDate, toDate, filterType);

                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchET.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                    //   Dialogs.baseShowProgressDialog(getContext(), "Loading...");

                    return true;
                }
                return false;
            }
        });

        HomeActivity.searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchText = searchET.getText().toString().trim();

                mMap.clear();
                WebServiceResult.searchEventsOnMap(currentDate, SharedPref.getInstance(getActivity()).getString("userId"), searchText, fromDate, toDate, filterType);

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchET.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        });
        return view;
    }

    public static HomeFragment getInstance() {
        return instance;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_service, longitude_service), 10));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setPadding(0, 400, 5, 0);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        WebServiceResult.returnEvents(currentDate, SharedPref.getInstance(getActivity()).getString("userId"), latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
    }

    @OnClick(R.id.refreshIV)
    public void onRefreshClick() {
        mMap.clear();
        WebServiceResult.returnEvents(currentDate, SharedPref.getInstance(getActivity()).getString("userId"), latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
        searchET.setText("");
    }

    /*@OnClick(R.id.filterSearchIV)
    public void onFilterSearchClick() {
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
    }*/

    private void loadMapView() {
        traker = new GPSTracker(getContext());

        latitude_service = traker.getLatitude();
        longitude_service = traker.getLongitude();

        Log.d("latatiude", String.valueOf(latitude_service));
        Log.d("longitude", String.valueOf(longitude_service));
    }

    @OnClick(R.id.toTV)
    public void setToDate() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        // String myFormat = "MM-dd-yyyy";
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
        new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    public void returnEvents(final ReturnEventPrp returnEventPrp) {
        if (returnEventPrp != null) {
            if (returnEventPrp.getResult().getStatus().equals("1")) {
                for (int i = 0; i < returnEventPrp.getResult().getData().getEvents().size(); i++) {
                    try {
                        mMap.addMarker(new MarkerOptions().title(i + "").position(new LatLng(Float.parseFloat(returnEventPrp.getResult().getData().getEvents().get(i).getLatitude()), Float.parseFloat(returnEventPrp.getResult().getData().getEvents().get(i).getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.pink_pin)));

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                infoWindow = layoutInflater.inflate(R.layout.custom_info, null);
                                textViewname = (TextView) infoWindow.findViewById(R.id.textViewname);
                                infoTitle = (TextView) infoWindow.findViewById(R.id.textView1);
                                ivInfo = (ImageView) infoWindow.findViewById(R.id.ivInfo);
                                ivEvent = (CircleImageView) infoWindow.findViewById(R.id.ivEvent);
                                LinearLayout l = (LinearLayout) infoWindow.findViewById(R.id.linear);
                                infoTitle.setText("From:" + "\n" + returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getFrom());
                                textViewname.setText(returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getName());

                                if (not_first_time_showing_info_window) {
                                    Picasso.with(getContext()).load("http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getImage()).resize(50, 50).into(ivEvent);
                                } else {
                                    not_first_time_showing_info_window = true;
                                    Picasso.with(getContext()).load("http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getImage()).resize(50, 50).into(ivEvent, new InfoWindowRefresher(marker));
                                }
                                return infoWindow;
                            }
                        });

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Intent in = new Intent(getActivity(), EventDetail.class);
                                in.putExtra("eventId", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getId());
                               /* in.putExtra("eventname", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getName());
                                in.putExtra("date", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getDate());
                                in.putExtra("time", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getTotalHours());
                                in.putExtra("frequency", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getFrequency());
                                in.putExtra("charityUrl", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getCharityUrl());
                                in.putExtra("charityPurposeTag", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getCharityPurposeTag());
                                in.putExtra("location", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getLocation());
                                in.putExtra("eventType", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getEventType());
                                in.putExtra("benifitCharity", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getBenifitCharity());
                                in.putExtra("thingsDescription", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getThingsDescription());
                                in.putExtra("yelpurl", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getYelpUrl());
                                in.putExtra("facebookurl", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getFbUrl());
                                in.putExtra("eventImage", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getImage());
                                in.putExtra("attending", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getAttending());
                                in.putExtra("eventAttendies", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getMogoing());
                                in.putExtra("startTime", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getStartTime());
                                in.putExtra("endTime", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getEndTime());
                                in.putExtra("endDate", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getEndDate());*/
                                in.putExtra("eventUserId", returnEventPrp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getUserId());
                                startActivity(in);
                            }
                        });

                    } catch (NullPointerException e) {
//Error
                    }
                }
            } else {
                Toast.makeText(getActivity(), returnEventPrp.getResult().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void searchEventsonMap(final SearchEventProp searchEventProp) {

        if (searchEventProp != null) {
            if (searchEventProp.getResult().getStatus().equalsIgnoreCase("1")) {

                for (int j = 0; j < searchEventProp.getResult().getData().getEvents().size(); j++) {
                    try {

                        Double searchlatitude = Double.valueOf(searchEventProp.getResult().getData().getEvents().get(j).getLatitude());
                        Double searchlongitude = Double.valueOf(searchEventProp.getResult().getData().getEvents().get(j).getLongitude());
                        LatLng location = new LatLng(searchlatitude, searchlongitude);

                        if (mMap.getProjection().getVisibleRegion().latLngBounds.contains(location)) {

                            mMap.addMarker(new MarkerOptions().title(j + "").position(new LatLng(Float.parseFloat(searchEventProp.getResult().getData().getEvents().get(j).getLatitude()), Float.parseFloat(searchEventProp.getResult().getData().getEvents().get(j).getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.pink_pin)));

                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    infoWindow = layoutInflater.inflate(R.layout.custom_info, null);
                                    textViewname = (TextView) infoWindow.findViewById(R.id.textViewname);
                                    infoTitle = (TextView) infoWindow.findViewById(R.id.textView1);
                                    ivInfo = (ImageView) infoWindow.findViewById(R.id.ivInfo);
                                    ivEvent = (CircleImageView) infoWindow.findViewById(R.id.ivEvent);
                                    LinearLayout l = (LinearLayout) infoWindow.findViewById(R.id.linear);
                                    infoTitle.setText("From:" + "\n" + searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getFrom());
                                    textViewname.setText(searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getName());
                                    if (not_first_time_showing_info_window) {
                                        Picasso.with(getContext()).load("http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getImage()).resize(50, 50).into(ivEvent);
                                    } else {
                                        not_first_time_showing_info_window = true;
                                        Picasso.with(getContext()).load("http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getImage()).resize(50, 50).into(ivEvent, new InfoWindowRefresher(marker));
                                    }
                                    return infoWindow;
                                }
                            });

                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    searchET.setText("");
                                    Intent in = new Intent(getActivity(), EventDetail.class);
                                    in.putExtra("eventId", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getId());
                                   /* in.putExtra("eventname", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getName());
                                    in.putExtra("date", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getDate());
                                    in.putExtra("time", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getTotalHours());
                                    in.putExtra("frequency", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getFrequency());
                                    in.putExtra("charityUrl", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getCharityUrl());
                                    in.putExtra("charityPurposeTag", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getCharityPurposeTag());
                                    in.putExtra("location", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getLocation());
                                    in.putExtra("eventType", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getEventType());
                                    in.putExtra("benifitCharity", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getBenifitCharity());
                                    in.putExtra("thingsDescription", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getThingsDescription());
                                    in.putExtra("yelpurl", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getYelpUrl());
                                    in.putExtra("facebookurl", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getFbUrl());
                                    in.putExtra("eventImage", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getImage());
                                    in.putExtra("attending", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getAttending());
                                    in.putExtra("eventAttendies", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getMogoing());
                                    in.putExtra("startTime", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getStartTime());
                                    in.putExtra("endTime", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getEndTime());
                                    in.putExtra("endDate", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getEndDate());*/
                                    in.putExtra("eventUserId", searchEventProp.getResult().getData().getEvents().get(Integer.parseInt(marker.getTitle())).getUserId());
                                    startActivity(in);
                                }
                            });
                            matchingEventsBT.setVisibility(View.VISIBLE);
                            matchingEventsBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), EventsListActivity.class);
                                    intent.putExtra("searchText", searchText);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            matchingEventsBT.setVisibility(View.VISIBLE);
                            matchingEventsBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), EventsListActivity.class);
                                    intent.putExtra("searchText", searchText);
                                    startActivity(intent);
                                }
                            });

                        }
                    } catch (IndexOutOfBoundsException e) {
//
                    }
                }
            }
        }
    }

    private class InfoWindowRefresher implements Callback {
        private Marker markerToRefresh;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            markerToRefresh.showInfoWindow();
        }

        @Override
        public void onError() {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mMap.clear();
        WebServiceResult.returnEvents(currentDate, SharedPref.getInstance(getActivity()).getString("userId"), latitude_service + "", longitude_service + "", tz.getDisplayName(false, TimeZone.SHORT).substring(3));
    }

    @Override
    public void onPause() {
        super.onPause();
        fromTV.setText(getString(R.string.from));
        toTV.setText(getString(R.string.to));
        mMap.clear();
    }
}
