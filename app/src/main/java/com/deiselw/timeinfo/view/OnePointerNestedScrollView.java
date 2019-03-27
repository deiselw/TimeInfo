package com.deiselw.timeinfo.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Deise on 06/01/2018.
 */

public class OnePointerNestedScrollView extends NestedScrollView {

    public OnePointerNestedScrollView(Context context) {
        super(context);
    }

    public OnePointerNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnePointerNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
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
