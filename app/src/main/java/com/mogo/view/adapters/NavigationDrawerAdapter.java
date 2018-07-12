package com.mogo.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mogo.R;
import com.mogo.controller.SharedPref;
import com.mogo.view.activities.HomeActivity;
import com.mogo.view.activities.LoginActivity;
import com.mogo.view.fragments.Contact_Fragment;
import com.mogo.view.fragments.CreateNewEvent;
import com.mogo.view.fragments.HomeFragment;
import com.mogo.view.fragments.NotificationFragment;
import com.mogo.view.fragments.PrivacyPolicy;
import com.mogo.view.fragments.Settings;
import com.mogo.view.fragments.TermsConditions;
import com.mogo.view.fragments.TodayEventsFragment;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {
    private Activity activity;
    private String[] labels;
    // private int selectedposition;

    public NavigationDrawerAdapter(Activity activity, String[] labels) {
        this.activity = activity;
        this.labels = labels;
        //  this.selectedposition = selectedposition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.navigation_drawer_item_layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getAdapterPosition() == 0) {
                    HomeActivity.getinstance().setFragment(new HomeFragment());
                    HomeActivity.getinstance().setTitle(activity.getString(R.string.home));
                    HomeActivity.mapIV.setVisibility(View.VISIBLE);
                    HomeActivity.saveTV.setVisibility(View.GONE);
                    HomeActivity.searchIV.setVisibility(View.VISIBLE);
                    HomeActivity.filterSearchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchListIV.setVisibility(View.VISIBLE);
                }
                if (holder.getAdapterPosition() == 1) {
                    HomeActivity.getinstance().setFragment(new TodayEventsFragment());
                    HomeActivity.getinstance().setTitle(activity.getString(R.string.todayevent));
                    HomeActivity.mapIV.setVisibility(View.GONE);
                    HomeActivity.saveTV.setVisibility(View.GONE);
                    HomeActivity.searchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchIV.setVisibility(View.VISIBLE);
                    HomeActivity.filterSearchListIV.setVisibility(View.GONE);
                }
                if (holder.getAdapterPosition() == 2) {
                    HomeActivity.getinstance().setFragment(new CreateNewEvent());
                    HomeActivity.getinstance().setTitle(activity.getString(R.string.createnewevetn));
                    HomeActivity.mapIV.setVisibility(View.GONE);
                    HomeActivity.saveTV.setVisibility(View.GONE);
                    HomeActivity.searchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchListIV.setVisibility(View.GONE);
                }
                if ((holder.getAdapterPosition() == 3)) {
                    HomeActivity.getinstance().setFragment(new PrivacyPolicy());
                    HomeActivity.getinstance().setTitle(activity.getString(R.string.privacypolicy));
                    HomeActivity.mapIV.setVisibility(View.GONE);
                    HomeActivity.saveTV.setVisibility(View.GONE);
                    HomeActivity.searchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchListIV.setVisibility(View.GONE);
                }
                if (holder.getAdapterPosition() == 4) {
                    HomeActivity.getinstance().setFragment(new TermsConditions());
                    HomeActivity.getinstance().setTitle(activity.getString(R.string.termsconditions));
                    HomeActivity.mapIV.setVisibility(View.GONE);
                    HomeActivity.saveTV.setVisibility(View.GONE);
                    HomeActivity.searchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchListIV.setVisibility(View.GONE);
                }
                if (holder.getAdapterPosition() == 5) {
                    HomeActivity.getinstance().setFragment(new NotificationFragment());
                    HomeActivity.getinstance().setTitle(activity.getString(R.string.notifications));
                    HomeActivity.mapIV.setVisibility(View.GONE);
                    HomeActivity.saveTV.setVisibility(View.GONE);
                    HomeActivity.searchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchListIV.setVisibility(View.GONE);
                }
                if (holder.getAdapterPosition() == 6) {
                    HomeActivity.getinstance().setFragment(new Contact_Fragment());
                    HomeActivity.getinstance().setTitle(activity.getString(R.string.contact));
                    HomeActivity.mapIV.setVisibility(View.GONE);
                    HomeActivity.saveTV.setVisibility(View.GONE);
                    HomeActivity.searchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchListIV.setVisibility(View.GONE);
                }
                if (holder.getAdapterPosition() == 7) {
                    HomeActivity.getinstance().setFragment(new Settings());
                    HomeActivity.getinstance().setTitle(activity.getString(R.string.settings));
                    HomeActivity.mapIV.setVisibility(View.GONE);
                    HomeActivity.saveTV.setVisibility(View.VISIBLE);
                    HomeActivity.searchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchIV.setVisibility(View.GONE);
                    HomeActivity.filterSearchListIV.setVisibility(View.GONE);
                }
              /*  if (holder.getAdapterPosition() == 7) {
                    SharedPref.getInstance(activity).clear();
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                }*/
            }
        });
        holder.label.setText(labels[position]);
    }

    @Override
    public int getItemCount() {
        return labels.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView label;

        ViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textViewLabel);
        }
    }

}
