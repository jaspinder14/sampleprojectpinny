package com.mogo.view.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.controller.SharedPref;
import com.mogo.model.createEvent.CreateEventPrp;
import com.mogo.view.activities.GPSTracker;
import com.mogo.view.activities.HomeActivity;
import com.mogo.view.customcontrols.EditTextRegular;
import com.mogo.view.customcontrols.TextViewRegular;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class CreateNewEvent extends Fragment {


    private static final int TAKE_PICTURE = 1;
    private static final int GALLERY_PICK = 2;
    private static CreateNewEvent instance;
    private String imagepath;
    private File image;
    private String latitude;
    private String longitude;
    private String picturePath = "";

    @BindView(R.id.yelpUrlET)
    EditTextRegular yelpUrlET;
    @BindView(R.id.facebookEventPageUrlET)
    EditTextRegular facebookEventPageUrlET;
    @BindView(R.id.briefDescriptionET)
    EditTextRegular briefDescriptionET;
    String frequency = "0";
    String zipcode;
    String format;
    String charityURLPattern;
    String city;


    public static CreateNewEvent getInstance() {
        return instance;
    }

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11;

    private Location currentLocation;

    LocationManager locationManager;

    private static File file;

    private static File dir;

    View view;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;

    @BindView(R.id.addImage)
    ImageView addImage;

    @BindView(R.id.createEventBT)
    Button createEventBT;

    @BindView(R.id.etEvent)
    EditTextRegular etEvent;

    @BindView(R.id.dateEventTV)
    TextViewRegular dateEventTV;

    @BindView(R.id.enddateEventTV)
    TextViewRegular enddateEventTV;

    @BindView(R.id.tvLocation)
    TextViewRegular tvLocation;

    @BindView(R.id.etbenificiry)
    EditTextRegular etbenificiry;

    @BindView(R.id.tvTypeEvent)
    TextViewRegular tvTypeEvent;

    @BindView(R.id.tvFrequency)
    TextViewRegular tvFrequency;

    @BindView(R.id.tvCharityTag)
    EditTextRegular tvCharityTag;

    @BindView(R.id.startTimeTV)
    TextViewRegular startTimeTV;

    @BindView(R.id.endTimeTV)
    TextViewRegular endTimeTV;


    @BindView(R.id.etCharityUrl)
    EditTextRegular etCharityUrl;

    private String setCharity;
    private String setFreq;
    private String setType;

    @BindView(R.id.eventIV)
    ImageView eventIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_new_event, container, false);
        instance = this;
        ButterKnife.bind(this, view);
        getCurrentLocation();

        createDirIfNotExists("Mogo");

        return view;
    }

    private void openDatePickerstartDate() {

    }


    @OnClick(R.id.dateEventTV)
    public void openDatePicker() {

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
        new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.enddateEventTV)
    public void openDatePicker1() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel1();
            }
        };
        new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEventTV.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        enddateEventTV.setText(sdf.format(myCalendar.getTime()));
    }


    @OnClick(R.id.startTimeTV)
    public void setStartTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                Time t = Time.valueOf(selectedHour + ":" + "00" + ":" + "00");
                long l = t.getTime();

                Time t1 = Time.valueOf("00" + ":" + selectedMinute + ":" + "00");
                long l1 = t1.getTime();

                if (selectedHour == 0) {
                    selectedHour += 12;
                    format = "AM";
                } else if (selectedHour == 12) {
                    format = "PM";
                } else if (selectedHour > 12) {
                    selectedHour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }
                startTimeTV.setText((String.valueOf(selectedHour).length() == 1 ? "0" + String.valueOf(selectedHour) : String.valueOf(selectedHour)) + ":" + (String.valueOf(selectedMinute).length() == 1 ? "0" + String.valueOf(selectedMinute) : String.valueOf(selectedMinute)) + " " + format);
            }
        }, hour, minute, false);
        mTimePicker.show();
    }

    @OnClick(R.id.endTimeTV)
    public void setEndTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                Time t = Time.valueOf(selectedHour + ":" + "00" + ":" + "00");
                long l = t.getTime();

                Time t1 = Time.valueOf("00" + ":" + selectedMinute + ":" + "00");
                long l1 = t1.getTime();
                if (selectedHour == 0) {

                    selectedHour += 12;

                    format = "AM";
                } else if (selectedHour == 12) {

                    format = "PM";

                } else if (selectedHour > 12) {

                    selectedHour -= 12;

                    format = "PM";

                } else {

                    format = "AM";
                }
                endTimeTV.setText((String.valueOf(selectedHour).length() == 1 ? "0" + String.valueOf(selectedHour) : String.valueOf(selectedHour)) + ":" + (String.valueOf(selectedMinute).length() == 1 ? "0" + String.valueOf(selectedMinute) : String.valueOf(selectedMinute)) + " " + format);
            }
        }, hour, minute, false);
        mTimePicker.show();
    }


    @OnClick(R.id.tvLocation)
    public void getLocation() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setBoundsBias(toBounds(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 5000)).build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException ignored) {

        }
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        dir = new File(Environment.getExternalStorageDirectory(), path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e("Failed :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    public void galleryImagePick() {
        Intent takePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(takePictureIntent, 2);
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(dir, "EventImage_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
        Uri fileUri = Uri.fromFile(file);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            picturePath = Uri.fromFile(file).getPath();
            eventIV.setImageBitmap(getResizedBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), 500, 500));

        } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            try (Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null)) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            }
            eventIV.setImageBitmap(getResizedBitmap(BitmapFactory.decodeFile(picturePath), 500, 500));
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    latitude = place.getLatLng().latitude + "";
                    longitude = place.getLatLng().longitude + "";
                    tvLocation.setText(place.getAddress());

                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
// lat,lng, your current location
                    try {
                        List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                        if (addresses.size() > 0) {
                            if (addresses.get(0).getPostalCode() == null) {
                                zipcode = "";
                            } else {
                                zipcode = addresses.get(0).getPostalCode();
                            }
                            city = addresses.get(0).getLocality();
                        } else {
                            zipcode = "";
                            city = "";
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);

                } else if (resultCode == RESULT_CANCELED) {

                }
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }


    protected void getCurrentLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        currentLocation = new Location("dummyprovider");
        GPSTracker tracker = new GPSTracker(getActivity());
        currentLocation.setLatitude(tracker.getLatitude());
        currentLocation.setLongitude(tracker.getLongitude());
    }

    public void showDialog() {

        final Dialog alertDialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(inflater.inflate(R.layout.view_clarity, null));
        alertDialog.setCancelable(false);
        final WheelView editText = (WheelView) alertDialog.findViewById(R.id.slot1);
        final Button button = (Button) alertDialog.findViewById(R.id.btn);
        final Button cancel = (Button) alertDialog.findViewById(R.id.cancelBtn);
        final String[] charity = new String[]{"Cancer", "Breast Cancer", "Prostate Cancer", "Animals", "Literacy",
                "Volunteer", "Fundraising", "Blood donation", "Donation", "Education", "Health care", "Homeless", "Poor", "Adoption", "Disaster relief", "Religious", "Mental health", "Children", "AIDS", "Alzheimers", "Elderly"};

        ArrayWheelAdapter<String> arrayAdapterLatDir = new ArrayWheelAdapter(getActivity(), charity);
        editText.setViewAdapter(arrayAdapterLatDir);
        editText.setCurrentItem(3);
        setCharity = charity[3];

        editText.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                tvCharityTag.setText(charity[editText.getCurrentItem()]);
                setCharity = charity[editText.getCurrentItem()];

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                tvCharityTag.setText(setCharity);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCharityTag.setText("");
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void showFrequencyDialog() {

        final Dialog alertDialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(inflater.inflate(R.layout.view_fruency, null));
        alertDialog.setCancelable(false);
        final WheelView frequ = (WheelView) alertDialog.findViewById(R.id.slotFreq);
        final Button buttonok = (Button) alertDialog.findViewById(R.id.btnOk);
        final Button cancels = (Button) alertDialog.findViewById(R.id.btnCancel);
        final String[] freq = new String[]{"One Time", "Daily", "Weekly", "Monthly", "Annually"};


        ArrayWheelAdapter<String> arrayAdapterFreq = new ArrayWheelAdapter(getActivity(), freq);
        frequ.setViewAdapter(arrayAdapterFreq);

        frequ.setCurrentItem(0);
        setFreq = freq[0];

        frequ.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                tvFrequency.setText(freq[frequ.getCurrentItem()]);
                setFreq = freq[frequ.getCurrentItem()];
            }
        });

        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                tvFrequency.setText(setFreq);

                if (setFreq.equalsIgnoreCase("One Time")) {
                    frequency = "0";
                } else if (setFreq.equalsIgnoreCase("Daily")) {
                    frequency = "1";
                } else if (setFreq.equalsIgnoreCase("Weekly"))

                {
                    frequency = "2";
                } else if (setFreq.equalsIgnoreCase("Monthly"))

                {
                    frequency = "3";
                } else if (setFreq.equalsIgnoreCase("Annually"))

                {
                    frequency = "4";
                }

            }
        });
        cancels.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                tvFrequency.setText("");
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    public void showTypeEventDialog() {

        final Dialog alertDialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(inflater.inflate(R.layout.view_type, null));
        alertDialog.setCancelable(false);
        final WheelView type = (WheelView) alertDialog.findViewById(R.id.slotType);
        final Button buttonss = (Button) alertDialog.findViewById(R.id.btnOks);
        final Button cancelss = (Button) alertDialog.findViewById(R.id.btnCancels);
        final String[] types = new String[]{"Art", "Bake Sale", "Breakfast", "Brunch", "Concert", "Dance", "Dinner", "Donations", "Food drive", "Fund Raiser", "Gala", "Happy Hours", "Lunch", "Other", "Party", "Run", "Sale", "School Fundraiser", "Tasting", "Trivia Night", "Volunteers Needed", "Workout"};
        ArrayWheelAdapter<String> arrayAdapterFreq = new ArrayWheelAdapter(getActivity(), types);
        type.setViewAdapter(arrayAdapterFreq);
        type.setCurrentItem(2);
        setType = types[2];

        type.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                tvTypeEvent.setText(types[type.getCurrentItem()]);
                setType = types[type.getCurrentItem()];
            }
        });
        buttonss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                tvTypeEvent.setText(setType);
            }
        });
        cancelss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTypeEvent.setText("");
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @OnClick(R.id.tvFrequency)
    public void openFrequencyDilog() {
        showFrequencyDialog();
    }

    @OnClick(R.id.tvTypeEvent)
    public void openTypeEventDialog() {
        showTypeEventDialog();
    }


    @OnClick(R.id.createEventBT)
    public void submitCreateEvent() {
        String etEvts = etEvent.getText().toString().trim();
        String txDate = dateEventTV.getText().toString().trim();
        String endDate = enddateEventTV.getText().toString().trim();
        String starttime = startTimeTV.getText().toString().trim();
        String endtime = endTimeTV.getText().toString().trim();
        //  String freq = tvFrequency.getText().toString().trim();
        String charityUrl = etCharityUrl.getText().toString().trim();
        String charityTag = tvCharityTag.getText().toString().trim();
        String location = tvLocation.getText().toString().trim();
        String eventType = tvTypeEvent.getText().toString().trim();
        String beneficy = etbenificiry.getText().toString().trim();
        String yelpUrl = yelpUrlET.getText().toString().trim();
        String facebookUrl = facebookEventPageUrlET.getText().toString().trim();
        String breifDescription = briefDescriptionET.getText().toString().trim();

        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
            Date startTime = parseFormat.parse(starttime);
            Date endTime = parseFormat.parse(endtime);
            System.out.println(parseFormat.format(startTime) + " = " + displayFormat.format(startTime) + parseFormat.format(endTime) + " = " + displayFormat.format(endTime));

            if (etEvts.isEmpty() || txDate.isEmpty() || frequency.isEmpty() || location.isEmpty() || eventType.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all the mandatory input fields", Toast.LENGTH_SHORT).show();
            } else if (starttime.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter the Start Time", Toast.LENGTH_SHORT).show();
            } else if (endtime.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter the End Time", Toast.LENGTH_SHORT).show();
            } else if (charityUrl.isEmpty()) {
                Toast.makeText(getActivity(), "Charity URL can not be empty", Toast.LENGTH_SHORT).show();
            } else if (!charityUrl.contains(".")) {
                Toast.makeText(getActivity(), "Invalid Charity URL", Toast.LENGTH_SHORT).show();
            } else {
                if (picturePath.equalsIgnoreCase("")) {
                    Uri path = Uri.parse("android.resource://com.mogo/" + R.drawable.createeventimg);
                    // Uri otherPath = Uri.parse("android.resource://com.segf4ult.test/drawable/icon");

                    //   String path = path.toString();
                    //  String path = otherPath .toString();

                    Dialogs.baseShowProgressDialog(getContext(), "Loading...");
                    WebServiceResult.createNewEvent(SharedPref.getInstance(getActivity()).getString("userId"), etEvts, txDate, endDate, displayFormat.format(startTime), displayFormat.format(endTime), null, frequency, zipcode, charityUrl, charityTag, eventType, city, location, latitude, longitude, yelpUrl, facebookUrl, beneficy, breifDescription);


                } else {
                    Dialogs.baseShowProgressDialog(getContext(), "Loading...");
                    WebServiceResult.createNewEvent(SharedPref.getInstance(getActivity()).getString("userId"), etEvts, txDate, endDate, displayFormat.format(startTime), displayFormat.format(endTime), new File(picturePath), frequency, zipcode, charityUrl, charityTag, eventType, city, location, latitude, longitude, yelpUrl, facebookUrl, beneficy, breifDescription);
                }
            }
        } catch (Exception e) {
            //Handle exception here, most of the time you will just log it.
            e.printStackTrace();
        }
    }

    @OnClick(R.id.addImage)
    public void addImage() {
        Dialogs.createEventdialog(getActivity());
    }

    public void retreiveImage(File image) {
        this.image = image;
    }

    public void createEventInfo(CreateEventPrp createEventPrp) {
        if (createEventPrp != null) {
            Dialogs.baseHideProgressDialog();
            if (createEventPrp.getResult().getStatus().equalsIgnoreCase("1")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Event has been added successfully. Please wait for admin approval notification.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Dialogs.showToast(getContext(), createEventPrp.getResult().getMessage());
                                Intent intent = new Intent(getContext(), HomeActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else {
            Dialogs.showToast(getContext(), "");
        }
    }
}
