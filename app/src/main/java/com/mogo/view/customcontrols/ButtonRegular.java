package com.mogo.view.customcontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.mogo.controller.Fonts;

/**
 * Created by Mobile on 7/26/2017.
 */

public class ButtonRegular extends Button {
    public ButtonRegular(Context context) {
        super(context);

        init();
    }

    public ButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ButtonRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }

        setTypeface(Fonts.setMotserratRegular(getContext()));

    }

}
