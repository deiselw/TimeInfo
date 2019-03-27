package com.deiselw.timeinfo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.deiselw.timeinfo.MainActivity;
import com.deiselw.timeinfo.R;

/**
 * Created by Deise on 10/11/2017.
 */

public class TimeCalView extends View {
    Paint mFadedNumberPaint;
    Paint mFadedNumberSmallPaint;
    Paint mNumberPaint;
    Paint mNumberSmallPaint;
    Paint mCurrentNumberPaint;
    Paint mCurrentNumberSmallPaint;
    Paint mCirclePaint;
    Paint mShadowPaint;

    private Paint[] mPaints;
    private int mMaxWidth;
    private int mOffset = 0;
    private int mColCount = 0;
    private int mRowCount;
    private int mItemCount;
    private int mInterval;
    private int mCurrentItem;
    private int mItemSize;
    private int mItemCenter;
    private int mPadding;
    private int mLeft;
    private int mBottom;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;
    private float mBaseHeight;
    private boolean mValid = false;

    public TimeCalView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mMaxWidth = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
        mPadding = getPaddingLeft();

        mFadedNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFadedNumberPaint.setColor(MainActivity.COLOR_DISABLED);
        mFadedNumberPaint.setTextAlign(Paint.Align.CENTER);

        mFadedNumberSmallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFadedNumberSmallPaint.setColor(MainActivity.COLOR_DISABLED);
        mFadedNumberSmallPaint.setTextAlign(Paint.Align.CENTER);

        mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint.setColor(MainActivity.COLOR_LINE);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);

        mNumberSmallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberSmallPaint.setColor(MainActivity.COLOR_LINE);
        mNumberSmallPaint.setTextAlign(Paint.Align.CENTER);

        mCurrentNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentNumberPaint.setColor(Color.WHITE);
        mCurrentNumberPaint.setTextAlign(Paint.Align.CENTER);
        mCurrentNumberPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mCurrentNumberSmallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentNumberSmallPaint.setColor(Color.WHITE);
        mCurrentNumberSmallPaint.setTextAlign(Paint.Align.CENTER);
        mCurrentNumberSmallPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#FDD835"));

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setColor(ContextCompat.getColor(context, R.color.colorShadowLight));
        mShadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mValid) {
            return;
        }
        super.onDraw(canvas);

        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor, canvas.getWidth()/2, 0);

        int left = mLeft;
        int bottom = mBottom;
        int i = 0;
        for (int row = 0; row < mRowCount; row++) {
            //canvas.drawLine(canvas.getWidth() - mPadding - mItemSize, bottom - mItemSize, canvas.getWidth() - mPadding - mItemSize, bottom, mNumberPaint);
            //canvas.drawText(row + 1 + "", canvas.getWidth() - mPadding - mItemCenter, bottom, mNumberPaint);
            for (int col = 0; col < mColCount && i < mItemCount; col++) {
                i++;
                if (i == mCurrentItem) {
                    Rect textBounds = new Rect();
                    mPaints[i - 1].getTextBounds("0", 0, 1, textBounds);
                    canvas.drawCircle(left, bottom + textBounds.centerY() + 4f, mItemCenter, mShadowPaint);
                    canvas.drawCircle(left, bottom + textBounds.centerY(), mItemCenter, mCirclePaint);
                }
                canvas.drawText(i + "", left, bottom, mPaints[i - 1]);
                left += mItemSize;
            }
            //canvas.drawLine(left + mItemSize, bottom - mItemSize, left + mItemSize, bottom, mFadedNumberPaint);

            left = mLeft;
            bottom += mItemSize;
            if (mInterval > 0 && (row + 1)%mInterval == 0) {
                bottom += mPadding;
            }
        }

        canvas.restore();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        if (mValid) {
            int intervals = 0;
            if (mInterval > 0) {
                intervals = mRowCount/mInterval;
                if (mRowCount%mInterval == 0) {
                    intervals--;
                }
            }

            int maxWidth = Math.min(mMaxWidth, w);
            mOffset = (w - maxWidth)/2;
            mItemSize = (maxWidth - mPadding*2)/(mColCount/* + 1*/);
            mItemCenter = mItemSize/2;
            setFontSize(mItemSize);
            mLeft = mOffset + mPadding + mItemCenter;
            mBottom = mPadding + mItemSize - mItemCenter;

            mBaseHeight = (float) Math.ceil((double)(mItemSize*mRowCount + intervals*mPadding + mPadding*2));
            int minHeight = (int)(mBaseHeight*mScaleFactor);
            h = Math.max(h, minHeight);
        }

        setMeasuredDimension(w, h);
    }

    public static boolean isValid(int colCount, int itemCount, int currentItem) {
        return colCount >= 1 && itemCount >= 1 && currentItem >= 1;
    }

    public void init(int colCount, int itemCount, int currentItem) {
        if (!isValid(colCount, itemCount, currentItem)) {
            return;
        }
        if (itemCount < currentItem) {
            itemCount = currentItem; // itemCount always >= currentItem
        }
        if (colCount > itemCount) {
            colCount = itemCount; // colCount always <= itemCount
        }

        mColCount = colCount;
        mRowCount = itemCount/colCount + ((itemCount%colCount == 0) ? 0 : 1);
        mItemCount = itemCount;
        mCurrentItem = currentItem;

        int currentItemIndex = mCurrentItem - 1;
        mPaints = new Paint[itemCount];
        if (currentItemIndex > 1) {
            for (int i = 0; i < currentItemIndex; i++) {
                mPaints[i] = (i < 999) ? mFadedNumberPaint : mFadedNumberSmallPaint;
            }
        }

        mPaints[currentItemIndex] = (currentItemIndex < 999) ? mCurrentNumberPaint : mCurrentNumberSmallPaint;

        for (int i = mCurrentItem; i < itemCount; i++) {
            mPaints[i] = (i < 999) ? mNumberPaint : mNumberSmallPaint;
        }

        mValid = true;
    }

    public void init(int colCount, int itemCount, int currentItem, int interval) {
        init(colCount, itemCount, currentItem);
        mInterval = (interval > 0) ? interval : 0;
    }

    private void setFontSize(int itemSize) {
        int fontSize = Math.min(itemSize/2, getResources().getDimensionPixelSize(R.dimen.font_size_calendar));
        mFadedNumberPaint.setTextSize(fontSize);
        mNumberPaint.setTextSize(fontSize);
        mCurrentNumberPaint.setTextSize(fontSize);

        int fontSizeSmall = fontSize*3/4;
        mFadedNumberSmallPaint.setTextSize(fontSizeSmall);
        mNumberSmallPaint.setTextSize(fontSizeSmall);
        mCurrentNumberSmallPaint.setTextSize(fontSizeSmall);
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            int parentHeight = ((View)getParent()).getHeight();
            mScaleFactor = Math.max(Math.min(parentHeight/mBaseHeight, 1.0f), Math.min(mScaleFactor, 1.0f));

            requestLayout(); // correct scroll bar size
            invalidate(); // call draw
            return true;
        }
    }
}
/*
for (int row = 0; row < rowCount; row++) {
    for (int col = 0; col < colCount; col++) {
        left = col*itemSize;
        top = row*itemSize;
        right = left + itemSize - spacing;
        bottom = top + itemSize - spacing;

        itemCount++;
        if (itemCount <= mItemCount) {
            canvas.drawCircle((left + right)/2, (top + bottom)/2, (itemSize - spacing)/2, mItemFillPaint);
            //canvas.drawText(itemCount + "", left, bottom, mLabelPaint);
        }else {
            canvas.drawCircle((left + right)/2, (top + bottom)/2, (itemSize - spacing)/2, mItemStrokePaint);
            //canvas.drawText(itemCount + "", left, bottom, mLabelPaint2);
        }
    }
}*/

/*
int itemCount = 10;
int lineCount = itemCount + 1;
int itemSize = canvasMinSize/itemCount;
int lineLength = itemCount*itemSize;
int step = 0;
for (int i = 0; i < lineCount; i++) {
    step = i*itemSize;
    canvas.drawLine(0, step, lineLength, step, mSquarePaint);
    canvas.drawLine(step, 0, step, lineLength, mSquarePaint);
}*/
