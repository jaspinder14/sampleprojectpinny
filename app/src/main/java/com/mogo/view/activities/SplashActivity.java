package com.mogo.view.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mogo.R;

import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String isloggedin;
    public static final int RequestPermissionCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);


        sharedPreferences = getApplicationContext().getSharedPreferences(
                "Mogo_Pref", MODE_PRIVATE);

        editor = sharedPreferences.edit();

        isloggedin = sharedPreferences.getString("loggedin", "");


        if (checkPermission()) {

            startHandler();

        } else {

            requestPermission();
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(SplashActivity.this, new String[]
                {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CALENDAR
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean FineLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean CoarseLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStoragePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean CalendarPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (FineLocation && CoarseLocation && CameraPermission && WriteExternalStoragePermission && CalendarPermission) {
                        // Toast.makeText(SplashActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                        startHandler();
                    } else {
                        //Toast.makeText(SplashActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CALENDAR);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    private void startHandler() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isloggedin.equalsIgnoreCase("isloggedin")) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
