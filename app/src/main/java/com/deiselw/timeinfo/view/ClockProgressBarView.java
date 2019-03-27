package com.deiselw.timeinfo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.support.v4.content.ContextCompat;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.deiselw.timeinfo.MainActivity;
import com.deiselw.timeinfo.R;

/**
 * Created by Deise on 28/11/2017.
 */

public class ClockProgressBarView extends View {
    protected static final float mRad = (float)Math.PI*0.36f;
    protected static final float mRotation = 1.5f*(float)Math.PI;
    protected static final int mMax = 1000;
    protected static final float mAngle = 360f/1000f; // 1000f is max in float
    protected static final float mStartAngle = 270f;
    protected static float mTop;

    protected int mValue;
    protected Paint mLabelPaint;
    protected Paint mSectionPaint;
    protected Paint mBarPaint;
    protected Paint mThumbPaint;
    protected float mBarWidth;
    protected float mBarCenter;
    protected float mThumbWidth;
    protected float mThumbRadius;
    protected float mLabelFontHeight;
    protected float mOffset;

    public ClockProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mValue = 0;

        Resources res = context.getResources();
        mTop = res.getDimensionPixelSize(R.dimen.clock_top);
        mThumbWidth = res.getDimensionPixelSize(R.dimen.clock_thumb_width);//TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, res.getDimension(R.dimen.clock_track_width), displayMetrics);
        mThumbRadius = mThumbWidth/2f;
        mBarWidth = res.getDimensionPixelSize(R.dimen.clock_bar_width);//TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, res.getDimension(R.dimen.clock_bar_width), displayMetrics);
        mLabelFontHeight = getResources().getDimensionPixelSize(R.dimen.font_size_clock);
        mOffset = mThumbWidth + 4f*mLabelFontHeight + mThumbRadius;
        mBarCenter = mBarWidth/2f;

        init(context);
    }

    public void init(Context context) {
        mSectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSectionPaint.setColor(MainActivity.COLOR_LINE);
        mSectionPaint.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.clock_line_width));

        mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelPaint.setTextAlign(Paint.Align.CENTER);
        mLabelPaint.setTextSize(mLabelFontHeight);
        mLabelPaint.setColor(MainActivity.COLOR_LINE);
        mLabelPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        mBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarPaint.setStyle(Paint.Style.STROKE);
        mBarPaint.setStrokeWidth(mBarWidth);
        mBarPaint.setColor(ContextCompat.getColor(context, R.color.colorBar));

        mThumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThumbPaint.setStyle(Paint.Style.FILL);
        mThumbPaint.setColor(ContextCompat.getColor(context, R.color.colorAccent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = canvas.getWidth();
        float r = w/2 - mOffset - mBarCenter;

        float cx = w/2;
        float cy = cx - mTop;

        //float bgRadius = cx - mBarWidth/2;
        //mBgPaint.setShader(new SweepGradient(cx, cy, mBgColors, null));
        //canvas.drawCircle(cx, cy, bgRadius, mBgPaint);

        RectF barBounds = new RectF(cx - r, cy - r, cx + r, cy + r);
        canvas.drawArc(barBounds, mStartAngle, (float)mValue*mAngle, false, mBarPaint);

		//Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		//p.setColor(MainActivity.COLOR_DRAW);
		//p.setStyle(Paint.Style.STROKE);
		//p.setStrokeWidth(1);
		//canvas.drawRect(new RectF(0, 0, w, h), p);

        float valueRad = mRad*(float)mValue/180f;
        float thumbX = (float)Math.sin(valueRad)*r + cx;
        float thumbY = (float)-Math.cos(valueRad)*r + cy;
		//canvas.drawCircle(thumbX, thumbY, mThumbRadius, mThumbPaint);
    }

    public void setProgress(int v) {
        mValue = MathUtils.clamp(v, 0, mMax);
        invalidate();
    }
}
