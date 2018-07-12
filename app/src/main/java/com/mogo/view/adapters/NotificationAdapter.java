package com.mogo.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.plus.model.people.Person;
import com.mogo.R;
import com.mogo.api.WebServiceResult;
import com.mogo.model.notificationprop.Notification;
import com.mogo.view.customcontrols.TextViewRegular;

import java.util.List;

/**
 * Created by Mobile on 8/18/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolderNotifications> {

    private Context context;
    private List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public ViewHolderNotifications onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolderNotifications(layoutInflater.inflate(R.layout.notificationscustom, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolderNotifications holder, int position) {


        if (notificationList.get(holder.getAdapterPosition()).getType().equalsIgnoreCase("nearby")) {

            holder.notificationTitleTV.setText("An Event named " + notificationList.get(holder.getAdapterPosition()).getEventName() + " has been created near your location.");

        } else if (notificationList.get(holder.getAdapterPosition()).getType().equalsIgnoreCase("approved")) {
            holder.notificationTitleTV.setText("Your Event named " + notificationList.get(holder.getAdapterPosition()).getEventName() + " has been approved by the admin.");

        } else if (notificationList.get(holder.getAdapterPosition()).getType().equalsIgnoreCase("decline")) {

            holder.notificationTitleTV.setText("Your Event named " + notificationList.get(holder.getAdapterPosition()).getEventName() + " has been declined by the admin " + "because of " + notificationList.get(holder.getAdapterPosition()).getMessage());

        } else if (notificationList.get(holder.getAdapterPosition()).getType().equalsIgnoreCase("broadcast")) {

            holder.notificationTitleTV.setText(notificationList.get(holder.getAdapterPosition()).getMessage());
        }


        holder.notificationTimeTV.setText(notificationList.get(holder.getAdapterPosition()).getDate());

        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Alert");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure want to delete the notification?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                WebServiceResult.deleteNotificationService(notificationList.get(holder.getAdapterPosition()).getId());

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolderNotifications extends RecyclerView.ViewHolder {
        private final TextViewRegular notificationTimeTV;
        private final TextViewRegular notificationTitleTV;
        private final ImageView deleteIV;

        public ViewHolderNotifications(View itemView) {
            super(itemView);
            
            notificationTitleTV = (TextViewRegular) itemView.findViewById(R.id.notificationTitleTV);
            notificationTimeTV = (TextViewRegular) itemView.findViewById(R.id.notificationTimeTV);
            deleteIV = (ImageView) itemView.findViewById(R.id.deleteIV);
        }
    }
}
