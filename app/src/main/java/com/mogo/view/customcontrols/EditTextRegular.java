package com.mogo.view.customcontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mogo.controller.Fonts;

/**
 * Created by Mobile on 7/24/2017.
 */

public class EditTextRegular extends EditText {
    public EditTextRegular(Context context) {
        super(context);

        init();

    }

    public EditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public EditTextRegular(Context context, AttributeSet attrs, int defStyleAttr) {
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
