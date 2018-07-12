package com.mogo.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mogo.R;
import com.mogo.view.activities.SignUpActivity;
import com.mogo.view.fragments.CreateNewEvent;

public class Dialogs {


    Context context;

    static AlertDialog dialogProgress;
    public static Dialog alertDialogBuilder;
    private static ProgressDialog progressDialog;


    public static void baseShowProgressDialog(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_loading_customdialog, null);
        TextView textViewMessage = (TextView) view.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);
        alertDialogBuilder.setView(view);
        dialogProgress = alertDialogBuilder.create();
        dialogProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogProgress.show();

    }

    public static void baseHideProgressDialog() {
        dialogProgress.hide();
        dialogProgress = null;
    }

    public static void showToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }


    public static void profiledialog(final Activity activity) {

        View alet_view = null;
        alertDialogBuilder = new Dialog(activity);
        alertDialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.setCancelable(false);
        Window window = alertDialogBuilder.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        alet_view = mInflater.inflate(R.layout.profile_layout, null);
        alertDialogBuilder.setContentView(alet_view);
        final RelativeLayout relativeLayout = (RelativeLayout) alet_view.findViewById(R.id.relative_gallery);
        final RelativeLayout relativeLayout1 = (RelativeLayout) alet_view.findViewById(R.id.relative_camera);
        final RelativeLayout relativeLayout2 = (RelativeLayout) alet_view.findViewById(R.id.relative_cancel);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.getinstance().galleryImagePick();
            }
        });

        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignUpActivity.getinstance().dispatchTakePictureIntent();

            }
        });

        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.dismiss();

            }
        });
        alertDialogBuilder.show();
    }


    public static void dismissdialog() {
        alertDialogBuilder.dismiss();
    }


    public static void progressdialog(AppCompatActivity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }


    public static void removedialog() {
        progressDialog.dismiss();
    }


    public static void createEventdialog(final Activity activity) {

        View alet_view = null;
        alertDialogBuilder = new Dialog(activity);
        alertDialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.setCancelable(false);
        Window window = alertDialogBuilder.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        alet_view = mInflater.inflate(R.layout.event_layout, null);
        alertDialogBuilder.setContentView(alet_view);
        final RelativeLayout gellery = (RelativeLayout) alet_view.findViewById(R.id.galleryRl);
        final RelativeLayout camera = (RelativeLayout) alet_view.findViewById(R.id.cameraRl);
        final RelativeLayout cancel = (RelativeLayout) alet_view.findViewById(R.id.cancelRl);

        gellery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewEvent.getInstance().galleryImagePick();
                alertDialogBuilder.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewEvent.getInstance().dispatchTakePictureIntent();
                alertDialogBuilder.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.dismiss();

            }
        });
        alertDialogBuilder.show();
    }


}








