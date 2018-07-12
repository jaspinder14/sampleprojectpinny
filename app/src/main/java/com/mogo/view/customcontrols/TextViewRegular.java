package com.mogo.view.customcontrols;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mogo.controller.Fonts;

/**
 * Created by Mobile on 7/25/2017.
 */

public class TextViewRegular extends TextView {
    public TextViewRegular(Context context) {
        super(context);

        init();
    }

    public TextViewRegular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public TextViewRegular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
