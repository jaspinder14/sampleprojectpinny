package com.mogo.model.SearchEventProp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mogo.model.returnevent.Event;

import java.util.List;

public class Data {

    @SerializedName("events")
    @Expose
    private List<Event> events = null;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}