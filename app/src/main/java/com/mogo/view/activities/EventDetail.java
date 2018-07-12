package com.mogo.view.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mogo.R;
import com.mogo.api.WebServiceConnection;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.controller.SharedPref;
import com.mogo.model.EventDetailProp.EventDetailProp;
import com.mogo.model.attendEventsProp.AttendEventsProp;
import com.mogo.model.cancelEventProp.CancelEventProp;
import com.mogo.model.todayeventsprop.Event;
import com.mogo.view.adapters.Share_Adapter;
import com.mogo.view.customcontrols.TextViewRegular;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

public class EventDetail extends AppCompatActivity {

    /* @BindView(R.id.goingCB)
     CheckBox goingCB;
 */
    @BindView(R.id.tvTopName)
    TextViewRegular tvTopName;

    @BindView(R.id.backIV)
    ImageView backIV;

    @BindView(R.id.dateEventTV)
    TextViewRegular dateEventTV;

    @BindView(R.id.startTimeTV)
    TextViewRegular startTimeTV;

    @BindView(R.id.tvFrequency)
    TextViewRegular tvFrequency;

    @BindView(R.id.tvCharity)
    TextViewRegular tvCharity;

    @BindView(R.id.tvCharityTg)
    TextViewRegular tvCharityTg;


    @BindView(R.id.tvLocationEvent)
    TextViewRegular tvLocationEvent;

    @BindView(R.id.tvTypeEvent)
    TextViewRegular tvTypeEvent;

    @BindView(R.id.tvYelp)
    TextViewRegular tvYelp;

    @BindView(R.id.tvFacebookPage)
    TextViewRegular tvFacebookPage;

    @BindView(R.id.tvBeneficiary)
    TextViewRegular tvBeneficiary;

    @BindView(R.id.tvDescription)
    TextViewRegular tvDescription;

    public static String eventName;
    public static String date;
    String time;
    public static String charityUrl;
    String charityPurposeTag;
    String location;
    String eventType;
    String benifitCharity;
    String thingsDescription;

    @BindView(R.id.createEventBT)
    Button createEventBT;
    String eventId;

    @BindView(R.id.eventIV)
    ImageView eventIV;
    public static String eventImage;
    String attending;

    public static EventDetail instance;
    String frequency;
    AlertDialog.Builder alertDialog;

    @BindView(R.id.shareIV)
    ImageView shareIV;
    String link;
    public static String imageOfEvent;
    String yelpUrl, fbUrl;

    @BindView(R.id.eventAttendiesTV)
    TextViewRegular eventAttendiesTV;
    String eventAttendies;

    long calID = 1;
    String deviceName = android.os.Build.MODEL;
    String deviceMan = android.os.Build.MANUFACTURER;
    String startTime, endTime;
    public static String endDate;
    String count;
    String eventUserId;

    @BindView(R.id.cancelEventIV)
    ImageView cancelEventIV;
    ContentResolver cr;
    ContentValues values;
    Uri EVENTS_URI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        instance = this;
        ButterKnife.bind(this);
        //goingCB.setTypeface(Fonts.setMotserratRegular(this));


        eventId = getIntent().getStringExtra("eventId");

        WebServiceResult.retunEventDetail(eventId, SharedPref.getInstance(getApplicationContext()).getString("userId"));


        createEventBT.setTransformationMethod(null);
    }


    public static EventDetail getInstance() {
        return instance;
    }

    @SuppressLint("SetTextI18n")
    private void setgetValue() {

//        Log.d("eventid", eventId);


        //  eventName = getIntent().getStringExtra("eventname");
        //  date = getIntent().getStringExtra("date");
        //  endDate = getIntent().getStringExtra("endDate");

        String str_date = date;
        String str_date1 = endDate;

        SimpleDateFormat readFormat = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat writeFormat = new SimpleDateFormat("MMM dd, yyyy");

        Date startdate, date1;

        try {
            startdate = readFormat.parse(str_date);
            date1 = readFormat.parse(str_date1);

            dateEventTV.setText(writeFormat.format(startdate) + " to " + writeFormat.format(date1));

            //   Log.v("Changed date", "" + writeFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        //  time = getIntent().getStringExtra("time");
        //  charityUrl = getIntent().getStringExtra("charityUrl");
        //  charityPurposeTag = getIntent().getStringExtra("charityPurposeTag");
        // location = getIntent().getStringExtra("location");
        // eventType = getIntent().getStringExtra("eventType");
        //   benifitCharity = getIntent().getStringExtra("benifitCharity");
        // thingsDescription = getIntent().getStringExtra("thingsDescription");
        //eventImage = getIntent().getStringExtra("eventImage");
        // attending = getIntent().getStringExtra("attending");
        // yelpUrl = getIntent().getStringExtra("yelpurl");
        //    fbUrl = getIntent().getStringExtra("facebookurl");
        // eventAttendies = getIntent().getStringExtra("eventAttendies");

        //startTime = getIntent().getStringExtra("startTime");
        // endTime = getIntent().getStringExtra("endTime");

        // frequency = getIntent().getStringExtra("frequency");

        if (frequency.equalsIgnoreCase("0")) {
            tvFrequency.setText("One Time");
        } else if (frequency.equalsIgnoreCase("1")) {
            tvFrequency.setText("Event will repeat everyday");
        } else if (frequency.equalsIgnoreCase("2")) {
            tvFrequency.setText("Event will repeat same day next Week");
        } else if (frequency.equalsIgnoreCase("3")) {
            tvFrequency.setText("Event will repeat same day next Month");
        } else if (frequency.equalsIgnoreCase("4")) {
            tvFrequency.setText("Event will repeat same day next Year");
        }
        tvTopName.setText(eventName);
        startTimeTV.setText(time);
        tvDescription.setText(thingsDescription);
        tvBeneficiary.setText(benifitCharity);
        tvTypeEvent.setText(eventType);
        tvLocationEvent.setSelected(true);
        tvLocationEvent.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvLocationEvent.setSingleLine(true);
        tvLocationEvent.setText(location);
        tvCharityTg.setText(charityPurposeTag);
        tvCharity.setText(charityUrl);
        tvYelp.setText(yelpUrl);
        tvFacebookPage.setText(fbUrl);
        eventAttendiesTV.setText("Event Attendies:" + " " + eventAttendies);
        Picasso.with(EventDetail.this).load("http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + eventImage).into(eventIV);

        //   imageOfEvent = "http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + eventImage;

        WebServiceResult.updateEventTokenService(SharedPref.getInstance(EventDetail.this).getString("userId"), String.valueOf(calID), eventId);

        if (attending.equalsIgnoreCase("0") && eventUserId.equalsIgnoreCase(SharedPref.getInstance(EventDetail.this).getString("userId"))) {
            createEventBT.setText("Thanks for using SoshiFy to create your event");
            createEventBT.setEnabled(false);
            createEventBT.setTextSize(14);
            cancelEventIV.setVisibility(View.VISIBLE);
            cancelEventIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialogs.baseShowProgressDialog(EventDetail.this, getString(R.string.loading));
                    WebServiceResult.cancelEventService(eventId);
                }
            });

        } else if (attending.equalsIgnoreCase("0") && !eventUserId.equalsIgnoreCase(SharedPref.getInstance(EventDetail.this).getString("userId"))) {
            createEventBT.setText("Are you going?");
            createEventBT.setEnabled(true);
            createEventBT.setTextSize(18);
            cancelEventIV.setVisibility(View.GONE);
            createEventBT.setOnClickListener(new View.OnClickListener() {
                AlertDialog dialog;

                @Override
                public void onClick(View view) {
                    if (deviceMan.equalsIgnoreCase("samsung")) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                            calID = 3;
                        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                            calID = 2;
                        } else {
                            calID = 1;
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            calID = 3;
                        } else {
                            calID = 1;
                        }
                    }
                    String[] items1 = date.split("-");
                    String m1 = items1[0];
                    String d1 = items1[1];
                    String y1 = items1[2];
                    int m = Integer.parseInt(m1);
                    int d = Integer.parseInt(d1);
                    int y = Integer.parseInt(y1);

                    long startMillis = 0;
                    long endMillis = 0;
                    Calendar beginTime = Calendar.getInstance();
                    beginTime.set(y, m, d);
                    startMillis = beginTime.getTimeInMillis();
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(y, m, d);
                    endMillis = endTime.getTimeInMillis();

                    cr = getContentResolver();
                    values = new ContentValues();
                    values.put(CalendarContract.Events.DTSTART, startMillis);
                    values.put(CalendarContract.Events.DTEND, endMillis);
                    values.put(CalendarContract.Events.TITLE, eventName);
                    values.put(CalendarContract.Events.CALENDAR_ID, calID);
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Calendars.CALENDAR_TIME_ZONE);

                    EVENTS_URI = Uri.parse(CalendarContract.Events.CONTENT_URI.toString());
                    cr.insert(EVENTS_URI, values);

                    Dialogs.baseShowProgressDialog(EventDetail.this, "Loading...");
                    WebServiceResult.attendEventService(SharedPref.getInstance(EventDetail.this).getString("userId"), eventId);

                    WebServiceResult.updateEventTokenService(SharedPref.getInstance(EventDetail.this).getString("userId"), String.valueOf(calID), eventId);
                }
            });
        } else if (attending.equalsIgnoreCase("1") && !eventUserId.equalsIgnoreCase(SharedPref.getInstance(EventDetail.this).getString("userId"))) {
            createEventBT.setEnabled(true);
            createEventBT.setText("Thanks for registering for a great event");
            createEventBT.setTextSize(18);
            createEventBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialogs.baseShowProgressDialog(EventDetail.this, "Loading...");
                    WebServiceResult.attendEventService(SharedPref.getInstance(EventDetail.this).getString("userId"), eventId);

                    WebServiceResult.updateEventTokenService(SharedPref.getInstance(EventDetail.this).getString("userId"), String.valueOf(calID), eventId);
                }
            });
        }

        shareIV.setOnClickListener(new View.OnClickListener() {
            AlertDialog dialog;

            @Override
            public void onClick(View v) {

                alertDialog = new AlertDialog.Builder(EventDetail.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View convertView = (View) inflater.inflate(R.layout.dialog_layout, null);
                alertDialog.setView(convertView);
                alertDialog.setCancelable(false);
                Button button = (Button) convertView.findViewById(R.id.btnCancel);
                RecyclerView lv = (RecyclerView) convertView.findViewById(R.id.RecycleshareItems);
                lv.setLayoutManager(new LinearLayoutManager(EventDetail.this, LinearLayoutManager.VERTICAL, false));
                lv.setAdapter(new Share_Adapter(EventDetail.this, getResources().getStringArray(R.array.appnames), getResources().obtainTypedArray(R.array.images)));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog = alertDialog.create();
                dialog.show();
            }
        });

        eventIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(EventDetail.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_image);
                ImageView imageView12 = dialog.findViewById(R.id.image_DialogBox);
                Picasso.with(dialog.getContext()).load("http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + eventImage).resize(700, 800).into(imageView12);
                dialog.show();
            }
        });
    }

    @OnClick(R.id.backIV)
    public void onBackClick() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void attendEvent(AttendEventsProp attendEventsProp) {

        if (attendEventsProp != null) {
            Dialogs.baseHideProgressDialog();
            if (attendEventsProp.getResult().getMessage().equalsIgnoreCase("You are attending this event")) {
                createEventBT.setText("Thanks for registering for a great event");
                createEventBT.setEnabled(true);
                eventAttendies = attendEventsProp.getResult().getData().getCount();
                eventAttendiesTV.setText("Event Attendies:" + " " + eventAttendies);
            } else {
                createEventBT.setText("Are you going?");
                createEventBT.setEnabled(true);
                eventAttendies = attendEventsProp.getResult().getData().getCount();
                eventAttendiesTV.setText("Event Attendies:" + " " + eventAttendies);
            }
        } else {
            Dialogs.showToast(EventDetail.this, "");
        }
    }

    public void cancelEvent(CancelEventProp cancelEventProp) {
        if (cancelEventProp != null) {
            Dialogs.baseHideProgressDialog();
            if (cancelEventProp.getResult().getStatus().equalsIgnoreCase("1")) {
                Dialogs.showToast(EventDetail.this, cancelEventProp.getResult().getMessage());
                Intent intent = new Intent(EventDetail.this, HomeActivity.class);
                startActivity(intent);
                finish();

                if (calID != 1) {
                    EVENTS_URI = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(eventId));
                    cr.delete(EVENTS_URI, null, null);

                    //  Toast.makeText(this, "Event deleted", Toast.LENGTH_LONG).show();
                }


            } else {
                Dialogs.showToast(EventDetail.this, cancelEventProp.getResult().getMessage());
            }
        } else {
            Dialogs.showToast(EventDetail.this, getString(R.string.somethingwrong));
        }
    }

    public void updateEventToken(ResponseBody body) {

        Log.d("tokenupdated", "Token Updated");
    }

    public void returnEventDetail(EventDetailProp eventDetailProp) {

        if (eventDetailProp != null) {
            if (eventDetailProp.getResult().getStatus().equalsIgnoreCase("1")) {
                eventUserId = eventDetailProp.getResult().getData().getUserId();
                eventName = eventDetailProp.getResult().getData().getName();
                date = eventDetailProp.getResult().getData().getDate();
                endDate = eventDetailProp.getResult().getData().getEndDate();
                time = eventDetailProp.getResult().getData().getTotalHours();
                charityUrl = eventDetailProp.getResult().getData().getCharityUrl();
                charityPurposeTag = eventDetailProp.getResult().getData().getCharityPurposeTag();
                location = eventDetailProp.getResult().getData().getLocation();
                eventType = eventDetailProp.getResult().getData().getEventType();
                benifitCharity = eventDetailProp.getResult().getData().getBenifitCharity();
                thingsDescription = eventDetailProp.getResult().getData().getThingsDescription();
                eventImage = eventDetailProp.getResult().getData().getImage();
                attending = eventDetailProp.getResult().getData().getAttending();
                yelpUrl = eventDetailProp.getResult().getData().getYelpUrl();
                fbUrl = eventDetailProp.getResult().getData().getFbUrl();
                eventAttendies = eventDetailProp.getResult().getData().getMogoing();
                startTime = eventDetailProp.getResult().getData().getStartTime();
                endTime = eventDetailProp.getResult().getData().getEndTime();
                frequency = eventDetailProp.getResult().getData().getFrequency();

                setgetValue();
            }
        }
    }
}
