package com.mogo.model.radiusprop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RadiusProp {

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