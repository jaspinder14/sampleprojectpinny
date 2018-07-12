package com.mogo.model.todayeventsprop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayEventsProp {

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