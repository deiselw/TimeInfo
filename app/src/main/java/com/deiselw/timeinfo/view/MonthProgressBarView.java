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

public class MonthProgressBarView extends ClockProgressBarView {
    private static final int mSectionSize = 7;

    private float mAngleStep;
    private int mSubsections;
    private int mFirstDayOffset;
    private String mFirstDayOfWeek;
    private String mFirstDayOfMonth;

    public MonthProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TimeUtils timeUtils = TimeUtils.getInstance();
        mSubsections = timeUtils.getMonthDays();
        mAngleStep = 2f*(float)Math.PI/(float)mSubsections;

        int daysUntilFirstDayOfWeek = timeUtils.getMonthDaysUntilFirstDayOfWeek();
        mFirstDayOffset = daysUntilFirstDayOfWeek - 1; // transform to [0-6]
        mFirstDayOfWeek = timeUtils.getFirstDayOfWeek().toUpperCase();
        mFirstDayOfMonth = timeUtils.getMonthFirstDayOfWeek().toUpperCase();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = canvas.getWidth();
        float r = w/2 - mOffset;

        float cx = w/2;
        float cy = cx - mTop;

        Rect labelRect = new Rect();
        for (int i = 0; i < mSubsections; i++) {
            float angle = (float)i*mAngleStep + mRotation;
            float cosAngle = (float)Math.cos(angle);
            float sinAngle = (float)Math.sin(angle);
            float sectionStartX = cx + cosAngle*r;
            float sectionStartY = cy + sinAngle*r;
            float sectionEndX = sectionStartX;
            float sectionEndY = sectionStartY;

            float labelOffsetX = cosAngle* mThumbRadius;
            float labelOffsetY = sinAngle* mThumbRadius;
            float labelAdjustX = cosAngle*0.5f;
            float labelAdjustY = 0.5f + 0.5f*sinAngle;

            boolean isSection = (i + mSectionSize - mFirstDayOffset)% mSectionSize == 0 || i == 0;
            if (isSection) {
                sectionEndX += cosAngle*mThumbWidth;
                sectionEndY += sinAngle*mThumbWidth;

                String labelText = i + 1 + " (";
                labelText += i == 0 ? mFirstDayOfMonth : mFirstDayOfWeek;
                labelText += ")";
                mLabelPaint.getTextBounds(labelText, 0, labelText.length(), labelRect);
                float labelX = sectionEndX + labelOffsetX + labelAdjustX*labelRect.width();
                float labelY = sectionEndY + labelOffsetY + labelAdjustY*labelRect.height();
                canvas.drawText(labelText, labelX, labelY, mLabelPaint);
            }else {
                sectionEndX += cosAngle*mThumbRadius;
                sectionEndY += sinAngle*mThumbRadius;
            }
            canvas.drawLine(sectionStartX, sectionStartY, sectionEndX, sectionEndY, mSectionPaint);
        }

        super.onDraw(canvas);
    }
}
