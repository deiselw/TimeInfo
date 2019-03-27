package com.deiselw.timeinfo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.deiselw.timeinfo.R;
import com.deiselw.timeinfo.TimeUtils;

import java.util.Vector;

/**
 * Created by Deise on 28/10/2017.
 */

public class YearProgressBarView extends ClockProgressBarView {
    private static final int mSubsections = 12;

    private Vector<Float> mAngles;
    private final String[] mMonths = TimeUtils.getInstance().getMonths();

    public YearProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int[] monthsDays = TimeUtils.getInstance().getMonthsDays();
        mAngles = new Vector<>(TimeUtils.YEAR_MONTHS);
        Vector<Integer> weights = new Vector<>(TimeUtils.YEAR_MONTHS);
        float totalWeights = 0f;
        for (int i = 0; i < TimeUtils.YEAR_MONTHS; i++) {
            int monthDays = monthsDays[i];
            weights.add(monthDays);
            totalWeights += (float)monthDays;
        }
        float angle = 2f*(float)Math.PI/totalWeights;
        mAngles.add(0, (float)weights.get(0)*angle);
        for (int i = 1; i < weights.size(); i++) {
            mAngles.add(i, mAngles.get(i - 1) + (float)weights.get(i)*angle);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = canvas.getWidth();
        float r = w/2 - mOffset;

        float cx = w/2;
        float cy = cx - mTop;

        Rect labelRect = new Rect();
        float angle = mRotation;
        for (int i = 0; i < mSubsections; i++) {
            float cosAngle = (float)Math.cos(angle);
            float sinAngle = (float)Math.sin(angle);
            float sectionStartX = cx + cosAngle*r;
            float sectionStartY = cy + sinAngle*r;
            float sectionEndX = sectionStartX + cosAngle* mThumbWidth;
            float sectionEndY = sectionStartY + sinAngle* mThumbWidth;
            canvas.drawLine(sectionStartX, sectionStartY, sectionEndX, sectionEndY, mSectionPaint);

            float labelOffsetX = cosAngle* mThumbRadius;
            float labelOffsetY = sinAngle* mThumbRadius;
            float labelAdjustX = cosAngle*0.5f;
            float labelAdjustY = 0.5f + 0.5f*sinAngle;

            String labelText = mMonths[i].toUpperCase();

            mLabelPaint.getTextBounds(labelText, 0, labelText.length(), labelRect);
            float labelX = sectionEndX + labelOffsetX + labelAdjustX*labelRect.width();
            float labelY = sectionEndY + labelOffsetY + labelAdjustY*labelRect.height();
            canvas.drawText(labelText, labelX, labelY, mLabelPaint);

            angle = mAngles.get(i) + mRotation;
        }

        super.onDraw(canvas);
    }
}
