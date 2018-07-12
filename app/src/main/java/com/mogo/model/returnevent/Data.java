package com.mogo.model.returnevent;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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