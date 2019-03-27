package com.deiselw.timeinfo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Deise on 06/01/2018.
 */

public class OnePointerScrollView extends ScrollView {

    public OnePointerScrollView(Context context) {
        super(context);
    }

    public OnePointerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnePointerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e.getPointerCount() > 1) {
            return false;
        }
        return super.onInterceptTouchEvent(e);
    }
}
