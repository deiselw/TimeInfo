package com.deiselw.timeinfo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.deiselw.timeinfo.R;

/**
 * Created by Deise on 28/10/2017.
 */

public class DayProgressBarView extends ClockProgressBarView {
    private static final int mSectionSize = 3;
    private static final int mSubsections = 24;
    private static final float mAngleStep = 2f*(float)Math.PI/(float)mSubsections;

    public DayProgressBarView(Context context, AttributeSet attrs) {
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
            float angle = (float)i*mAngleStep + mRotation;
            float cosAngle = (float)Math.cos(angle);
            float sinAngle = (float)Math.sin(angle);
            float sectionStartX = cx + cosAngle*r;
            float sectionStartY = cy + sinAngle*r;
            float sectionEndX = sectionStartX;
            float sectionEndY = sectionStartY;

            float labelOffsetX = cosAngle*mThumbRadius;
            float labelOffsetY = sinAngle*mThumbRadius;
            float labelAdjustX = cosAngle*0.5f;
            float labelAdjustY = 0.5f + 0.5f*sinAngle;

            boolean isSection = (i + mSectionSize)% mSectionSize == 0;
            if (isSection) {
                sectionEndX += cosAngle*mThumbWidth;
                sectionEndY += sinAngle*mThumbWidth;

                String labelText = Integer.toString(i);
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
