package com.mogo.model.attendEventsProp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendEventsProp {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}