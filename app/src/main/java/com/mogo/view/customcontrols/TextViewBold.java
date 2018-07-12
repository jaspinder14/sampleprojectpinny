package com.mogo.view.customcontrols;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mogo.controller.Fonts;

/**
 * Created by Mobile on 7/26/2017.
 */

public class TextViewBold extends TextView {
    public TextViewBold(Context context) {
        super(context);

        init();
    }

    public TextViewBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public TextViewBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }


    private void init() {
        if (isInEditMode()) {
            return;
        }

        setTypeface(Fonts.setMotserratBold(getContext()));

    }

}
