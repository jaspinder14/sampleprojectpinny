package com.mogo.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.controller.Dialogs;
import com.mogo.controller.SharedPref;
import com.mogo.model.deletenotificationprop.NotificationDeleteProp;
import com.mogo.model.notificationprop.Notification;
import com.mogo.model.notificationprop.NotificationProp;
import com.mogo.view.activities.EventDetail;
import com.mogo.view.adapters.NotificationAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationFragment extends Fragment {


    public static NotificationFragment instance;
    View view;

    @BindView(R.id.notificationsRV)
    RecyclerView notificationsRV;

    NotificationAdapter notificationAdapter;
    List<Notification> notificationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        instance = this;
        ButterKnife.bind(this, view);

        WebServiceResult.returnNotifications(SharedPref.getInstance(getContext()).getString("userId"));

        return view;
    }

    public static NotificationFragment getInstance() {
        return instance;
    }


    public void returnNotifications(NotificationProp notificationProp) {

        if (notificationProp != null) {

            if (notificationProp.getResult().getStatus().equalsIgnoreCase("1")) {

                notificationList = notificationProp.getResult().getData().getNotifications();
                notificationAdapter = new NotificationAdapter(getContext(), notificationList);
                notificationsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                notificationsRV.setAdapter(notificationAdapter);

            } else {
                Dialogs.showToast(getContext(), notificationProp.getResult().getMessage());
            }
        } else {
            Dialogs.showToast(getContext(), getString(R.string.somethingwrong));
        }
    }

    public void deletNotification(NotificationDeleteProp notificationDeleteProp) {

        if (notificationDeleteProp != null) {

            if (notificationDeleteProp.getResult().getStatus().equalsIgnoreCase("1")) {
                Dialogs.showToast(getContext(), notificationDeleteProp.getResult().getMessage());
                WebServiceResult.returnNotifications(SharedPref.getInstance(getContext()).getString("userId"));
            } else {
                Dialogs.showToast(getContext(), notificationDeleteProp.getResult().getMessage());
            }
        } else {
            Dialogs.showToast(getContext(), getString(R.string.somethingwrong));
        }

    }
}
