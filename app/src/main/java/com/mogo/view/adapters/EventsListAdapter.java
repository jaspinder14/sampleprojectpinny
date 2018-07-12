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
import com.mogo.model.returnevent.Event;
import com.mogo.view.activities.EventDetail;
import com.mogo.view.customcontrols.TextViewBold;
import com.mogo.view.customcontrols.TextViewRegular;
import com.squareup.picasso.Picasso;

import java.util.List;


public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolderEventList> {

    private Context context;
    private List<Event> eventsList;

    public EventsListAdapter(Context context, List<Event> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
    }

    @Override
    public ViewHolderEventList onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolderEventList(layoutInflater.inflate(R.layout.eventslistcustom, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolderEventList holder, int position) {

        Picasso.with(context).load("http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + eventsList.get(position).getImage()).into(holder.eventIV);
        holder.eventNameTV.setText(eventsList.get(position).getName());
        holder.eventSpecificationRV.setText(eventsList.get(position).getCharityPurposeTag());
        holder.eventTimeTV.setText("From: " + eventsList.get(position).getFrom());
        holder.eventDateTV.setText(eventsList.get(position).getDate());
        holder.eventLocationTV.setSelected(true);
        holder.eventLocationTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.eventLocationTV.setSingleLine(true);
        holder.eventLocationTV.setText(eventsList.get(position).getLocation());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetail.class);
                intent.putExtra("eventId", eventsList.get(holder.getAdapterPosition()).getId());
                /*intent.putExtra("eventname", eventsList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("date", eventsList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("time", eventsList.get(holder.getAdapterPosition()).getTotalHours());
                intent.putExtra("frequency", eventsList.get(holder.getAdapterPosition()).getFrequency());
                intent.putExtra("charityUrl", eventsList.get(holder.getAdapterPosition()).getCharityUrl());
                intent.putExtra("charityPurposeTag", eventsList.get(holder.getAdapterPosition()).getCharityPurposeTag());
                intent.putExtra("location", eventsList.get(holder.getAdapterPosition()).getLocation());
                intent.putExtra("eventType", eventsList.get(holder.getAdapterPosition()).getEventType());
                intent.putExtra("benifitCharity", eventsList.get(holder.getAdapterPosition()).getBenifitCharity());
                intent.putExtra("thingsDescription", eventsList.get(holder.getAdapterPosition()).getThingsDescription());
                intent.putExtra("yelpurl", eventsList.get(holder.getAdapterPosition()).getYelpUrl());
                intent.putExtra("facebookurl", eventsList.get(holder.getAdapterPosition()).getFbUrl());
                intent.putExtra("eventImage", eventsList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("attending", eventsList.get(holder.getAdapterPosition()).getAttending());
                intent.putExtra("eventAttendies", eventsList.get(holder.getAdapterPosition()).getMogoing());
                intent.putExtra("startTime", eventsList.get(holder.getAdapterPosition()).getStartTime());
                intent.putExtra("endTime", eventsList.get(holder.getAdapterPosition()).getEndTime());
                intent.putExtra("endDate", eventsList.get(holder.getAdapterPosition()).getEndDate());*/
                intent.putExtra("eventUserId", eventsList.get(holder.getAdapterPosition()).getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    class ViewHolderEventList extends RecyclerView.ViewHolder {
        private final ImageView eventIV;
        private final TextViewBold eventNameTV;
        private final TextViewRegular eventSpecificationRV;
        private final TextViewRegular eventTimeTV;
        private final TextViewRegular eventDateTV;
        private final TextViewRegular eventLocationTV;

        ViewHolderEventList(View itemView) {
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
