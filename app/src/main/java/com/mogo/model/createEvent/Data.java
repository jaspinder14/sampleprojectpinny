package com.mogo.model.createEvent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("eventId")
@Expose
private String eventId;

public String getEventId() {
return eventId;
}

public void setEventId(String eventId) {
this.eventId = eventId;
}

}