package com.deiselw.timeinfo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.deiselw.timeinfo.R;
import com.deiselw.timeinfo.TimeUtils;

/**
 * Created by Deise on 28/10/2017.
 */

public class WeekProgressBarView extends ClockProgressBarView {
    private static final int mSubsections = 7;
    private static final float mAngleStep = 2f*(float)Math.PI/(float)mSubsections;

    private final String[] mWeekDays = TimeUtils.getInstance().getDaysOfWeek();

    public WeekProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = canvas.getWidth();
        float r = w/2 - mOffset;

        float cx = w/2;
        float cy = cx - mTop;

        Rect labelRect = new Rect();
        for (int i = 0; i < mSubsections; i++) {
            float angle = (float)i* mAngleStep + mRotation;
            float cosAngle = (float)Math.cos(angle);
            float sinAngle = (float)Math.sin(angle);
            float sectionStartX = cx + cosAngle*r;
            float sectionStartY = cy + sinAngle*r;
            float sectionEndX = sectionStartX + cosAngle*mThumbWidth;
            float sectionEndY = sectionStartY + sinAngle*mThumbWidth;
            canvas.drawLine(sectionStartX, sectionStartY, sectionEndX, sectionEndY, mSectionPaint);

            float labelOffsetX = cosAngle*mThumbRadius;
            float labelOffsetY = sinAngle*mThumbRadius;
            float labelAdjustX = cosAngle*0.5f;
            float labelAdjustY = 0.5f + 0.5f*sinAngle;

            String labelText = mWeekDays[i].toUpperCase();
            mLabelPaint.getTextBounds(labelText, 0, labelText.length(), labelRect);
            float labelX = sectionEndX + labelOffsetX + labelAdjustX*labelRect.width();
            float labelY = sectionEndY + labelOffsetY + labelAdjustY*labelRect.height();
            canvas.drawText(labelText, labelX, labelY, mLabelPaint);
        }

        super.onDraw(canvas);
    }
}
