package com.sally.customprogressbar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.sally.customprogressbar.R;

/**
 * Created by sally on 16/5/23.
 */
public class RoundProgressBarWithProgress extends HorizontalProgressBarWithProgress {

    private int radius = dp2px(20);

    private int mMaxPaintWidth;

    public RoundProgressBarWithProgress(Context context) {
        this(context, null);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        reachedHeight = reachedHeight / 2;
        unReachedHeight = reachedHeight;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWithProgress);
        radius = (int) typedArray.getDimension(R.styleable.RoundProgressBarWithProgress_radius, radius);
        typedArray.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(reachedHeight, unReachedHeight);
        int expect = radius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);
        int readWidth = Math.min(width, height);
        radius = (readWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth)/2;
        setMeasuredDimension(readWidth, readWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        int textHeight = (int) ((mPaint.descent() - mPaint.ascent())/2);

        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
        mPaint.setStyle(Paint.Style.STROKE);

        // draw unreached bar
        mPaint.setStrokeWidth(unReachedHeight);
        mPaint.setColor(unReachedColor);
        canvas.drawCircle(radius, radius, radius, mPaint);

        // draw reached bar
        mPaint.setColor(reachedColor);
        mPaint.setStrokeWidth(reachedHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, radius * 2, radius * 2), 0, sweepAngle, false, mPaint);

        // draw text
        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, radius-textWidth/2, radius+textHeight/2, mPaint);

        canvas.restore();
    }
}
