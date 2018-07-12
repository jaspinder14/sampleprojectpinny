package com.mogo.controller;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Mobile on 7/24/2017.
 */

public class Fonts {

    public static Typeface setMotserratRegular(Context context) {

        return Typeface.createFromAsset(context.getAssets(), "Montserrat-Regular.otf");

    }

    public static Typeface setMotserratBold(Context context) {

        return Typeface.createFromAsset(context.getAssets(), "Montserrat-Bold.otf");
    }

}
