package com.mogo.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mogo.R;
import com.mogo.model.todayeventsprop.Event;
import com.mogo.view.activities.EventDetail;
import com.mogo.view.customcontrols.TextViewBold;
import com.mogo.view.customcontrols.TextViewRegular;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TodayEventsAdapter extends RecyclerView.Adapter<TodayEventsAdapter.ViewHolderTodayEvents> {

    private Context context;
    private List<Event> todaysEventList;

    public TodayEventsAdapter(Context context, List<Event> todaysEventList) {
        this.context = context;
        this.todaysEventList = todaysEventList;
    }

    @Override
    public ViewHolderTodayEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TodayEventsAdapter.ViewHolderTodayEvents viewHolderEventList = new TodayEventsAdapter.ViewHolderTodayEvents(layoutInflater.inflate(R.layout.eventslistcustom, parent, false));
        return viewHolderEventList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolderTodayEvents holder, int position) {

        Picasso.with(context).load("http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + todaysEventList.get(position).getImage()).into(holder.eventIV);
        holder.eventNameTV.setText(todaysEventList.get(position).getName());
        holder.eventSpecificationRV.setText(todaysEventList.get(position).getCharityPurposeTag());
        holder.eventTimeTV.setText("From: " + todaysEventList.get(position).getFrom());
        holder.eventDateTV.setText(todaysEventList.get(position).getDate());
        holder.eventLocationTV.setSelected(true);
        holder.eventLocationTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.eventLocationTV.setSingleLine(true);
        holder.eventLocationTV.setText(todaysEventList.get(position).getLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetail.class);
                intent.putExtra("eventId", todaysEventList.get(holder.getAdapterPosition()).getId());
               /* intent.putExtra("eventname", todaysEventList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("date", todaysEventList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("time", todaysEventList.get(holder.getAdapterPosition()).getTotalHours());
                intent.putExtra("frequency", todaysEventList.get(holder.getAdapterPosition()).getFrequency());
                intent.putExtra("charityUrl", todaysEventList.get(holder.getAdapterPosition()).getCharityUrl());
                intent.putExtra("charityPurposeTag", todaysEventList.get(holder.getAdapterPosition()).getCharityPurposeTag());
                intent.putExtra("location", todaysEventList.get(holder.getAdapterPosition()).getLocation());
                intent.putExtra("eventType", todaysEventList.get(holder.getAdapterPosition()).getEventType());
                intent.putExtra("benifitCharity", todaysEventList.get(holder.getAdapterPosition()).getBenifitCharity());
                intent.putExtra("thingsDescription", todaysEventList.get(holder.getAdapterPosition()).getThingsDescription());
                intent.putExtra("yelpurl", todaysEventList.get(holder.getAdapterPosition()).getYelpUrl());
                intent.putExtra("facebookurl", todaysEventList.get(holder.getAdapterPosition()).getFbUrl());
                intent.putExtra("eventImage", todaysEventList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("attending", todaysEventList.get(holder.getAdapterPosition()).getAttending());
                intent.putExtra("eventAttendies", todaysEventList.get(holder.getAdapterPosition()).getMogoing());
                intent.putExtra("startTime", todaysEventList.get(holder.getAdapterPosition()).getStartTime());
                intent.putExtra("endTime", todaysEventList.get(holder.getAdapterPosition()).getEndTime());
                intent.putExtra("endDate", todaysEventList.get(holder.getAdapterPosition()).getEndDate());*/
                intent.putExtra("eventUserId", todaysEventList.get(holder.getAdapterPosition()).getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todaysEventList.size();
    }

    class ViewHolderTodayEvents extends RecyclerView.ViewHolder {
        private final ImageView eventIV;
        private final TextViewBold eventNameTV;
        private final TextViewRegular eventSpecificationRV;
        private final TextViewRegular eventTimeTV;
        private final TextViewRegular eventDateTV;
        private final TextViewRegular eventLocationTV;

        ViewHolderTodayEvents(View itemView) {
            super(itemView);

            eventIV = (ImageView) itemView.findViewById(R.id.eventIV);
            eventNameTV = (TextViewBold) itemView.findViewById(R.id.eventNameTV);
            eventSpecificationRV = (TextViewRegular) itemView.findViewById(R.id.eventSpecificationRV);
            eventTimeTV = (TextViewRegular) itemView.findViewById(R.id.eventTimeTV);
            eventDateTV = (TextViewRegular) itemView.findViewById(R.id.eventDateTV);
            eventLocationTV = (TextViewRegular) itemView.findViewById(R.id.eventLocationTV);
        }
    }
}
