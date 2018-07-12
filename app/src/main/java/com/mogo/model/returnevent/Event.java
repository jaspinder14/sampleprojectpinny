package com.mogo.model.returnevent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("charityUrl")
    @Expose
    private String charityUrl;
    @SerializedName("charityPurposeTag")
    @Expose
    private String charityPurposeTag;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("yelpUrl")
    @Expose
    private String yelpUrl;
    @SerializedName("fbUrl")
    @Expose
    private String fbUrl;
    @SerializedName("benifitCharity")
    @Expose
    private String benifitCharity;
    @SerializedName("thingsDescription")
    @Expose
    private String thingsDescription;
    @SerializedName("approvedDate")
    @Expose
    private String approvedDate;
    @SerializedName("approveStatus")
    @Expose
    private String approveStatus;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("totalHours")
    @Expose
    private String totalHours;
    @SerializedName("attending")
    @Expose
    private String attending;

    @SerializedName("zipCode")
    @Expose
    private String zipCode;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMogoing() {
        return mogoing;
    }

    public void setMogoing(String mogoing) {
        this.mogoing = mogoing;
    }

    @SerializedName("mogoing")
    @Expose
    private String mogoing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getCharityUrl() {
        return charityUrl;
    }

    public void setCharityUrl(String charityUrl) {
        this.charityUrl = charityUrl;
    }

    public String getCharityPurposeTag() {
        return charityPurposeTag;
    }

    public void setCharityPurposeTag(String charityPurposeTag) {
        this.charityPurposeTag = charityPurposeTag;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getYelpUrl() {
        return yelpUrl;
    }

    public void setYelpUrl(String yelpUrl) {
        this.yelpUrl = yelpUrl;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(String fbUrl) {
        this.fbUrl = fbUrl;
    }

    public String getBenifitCharity() {
        return benifitCharity;
    }

    public void setBenifitCharity(String benifitCharity) {
        this.benifitCharity = benifitCharity;
    }

    public String getThingsDescription() {
        return thingsDescription;
    }

    public void setThingsDescription(String thingsDescription) {
        this.thingsDescription = thingsDescription;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    public String getAttending() {
        return attending;
    }

    public void setAttending(String attending) {
        this.attending = attending;
    }

}