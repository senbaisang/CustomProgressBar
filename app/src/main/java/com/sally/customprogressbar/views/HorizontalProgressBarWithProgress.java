package com.sally.customprogressbar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.sally.customprogressbar.R;

/**
 * drawText(text, x, y, paint)
 * - x : 默认为text字符左边在屏幕上的位置
 * - y : 指定该text字符baseline在屏幕上的位置
 * - 设置paint.setTextAlign(Paint.Align.CENTER):则xy为字符的中心
 *
 * Paint.FontMetrics
 * - ascent : 基线上方的距离
 * - descent: 基线下方的距离
 * - top
 * - bottom
 *
 * Created by sally on 16/5/23.
 */
public class HorizontalProgressBarWithProgress extends ProgressBar {
    public static final String TAG = "HorizontalProgressBarWi";

    private final int DEFAULT_UNREACHED_COLOR = 0xFFB6C1;
    private final int DEFAULT_UNREACHED_HEIGHT = dp2px(10); //dp
    private final int DEFAULT_REACHED_COLOR = 0xFF4040;
    private final int DEFAULT_REACHED_HEIGHT = dp2px(10);
    private final int DEFAULT_TEXT_COLOR = 0xFF4040;
    private final int DEFAULT_TEXT_SIZE = sp2px(16);
    private final int DEFAULT_TEXT_OFFSET = dp2px(10);


    /**
     * 自定义属性
     */
    protected int unReachedColor = DEFAULT_UNREACHED_COLOR;
    protected int unReachedHeight = DEFAULT_UNREACHED_HEIGHT;
    protected int reachedColor = DEFAULT_REACHED_COLOR;
    protected int reachedHeight = DEFAULT_REACHED_HEIGHT;
    protected int textColor = DEFAULT_TEXT_COLOR;
    protected int textSize = DEFAULT_TEXT_SIZE;
    protected int textOffset = DEFAULT_TEXT_OFFSET;

    /**
     * 画笔
     */
    protected Paint mPaint = new Paint();

    /**
     * 世纪绘制区域的宽度
     */
    private int realWidth;

    public HorizontalProgressBarWithProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBarWithProgress);
        unReachedColor = ta.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_color, unReachedColor);
        unReachedHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_unreach_height, unReachedHeight);
        reachedColor = ta.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_reach_color, reachedColor);
        reachedHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_reach_height, reachedHeight);
        textColor = ta.getColor(R.styleable.HorizontalProgressBarWithProgress_progress_text_color, textColor);
        textSize = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_size, textSize);
        textOffset = (int) ta.getDimension(R.styleable.HorizontalProgressBarWithProgress_progress_text_offset, textOffset);
        ta.recycle();

        mPaint.setTextSize(textSize);
    }

    /**
     * 测量控件的值
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
        realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight()/2);

        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);

        boolean noNeedDraw = false;

        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * realWidth;

        if(progressX + textWidth >= realWidth) {
            progressX = realWidth - textWidth;
            noNeedDraw = true;
        }

        // draw reached line
        float endX = progressX - textOffset / 2;

        if(endX > 0) {
            mPaint.setColor(reachedColor);
            mPaint.setStrokeWidth(reachedHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }


        // draw text
        mPaint.setColor(textColor);
        int y = (int) -((mPaint.descent() + mPaint.ascent())/2);
        canvas.drawText(text, progressX, y, mPaint);

        // draw unreached line
        if(!noNeedDraw) {
            float startX = progressX + textWidth + textOffset/2;
            mPaint.setColor(unReachedColor);
            mPaint.setStrokeWidth(unReachedHeight);
            canvas.drawLine(startX, 0, realWidth, 0, mPaint);
        }

        canvas.restore();
    }

    /**
     * 测量控件的高度
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int result;

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        // 精确值
        if(mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            // ascent()  Return the distance above (negative) the baseline (ascent) based on the current typeface and text size.
            // descent() Return the distance below (positive) the baseline (descent) based on the current typeface and text size.
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(unReachedHeight, reachedHeight), Math.abs(textHeight));
            // 设置的值，不能超过父控件给定的值
            if(mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
}
