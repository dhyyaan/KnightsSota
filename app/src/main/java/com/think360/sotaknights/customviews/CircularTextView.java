package com.think360.sotaknights.customviews;

/**
 * Created by think360 on 04/10/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.think360.sotaknights.R;


public class CircularTextView extends android.support.v7.widget.AppCompatTextView
{
    private float strokeWidth;
    int strokeColor,solidColor,textColor;

    public CircularTextView(Context context) {
        super(context);

    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,  attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,  attrs);
    }


    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        this.setTextColor(textColor);

        int  h = this.getHeight();
        int  w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter/2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2 , diameter / 2, radius, strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius-strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp)
    {
        float scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp*scale;

    }

    public void setStrokeColor(String color)
    {
        strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(String color)
    {
        solidColor = Color.parseColor(color);

    }
    public void setTextColor(String color)
    {
        textColor = Color.parseColor(color);

    }
    private void initViews(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomTextView, 0, 0);

        try {
            // get the text and colors specified using the names in attrs.xml
            strokeWidth = a.getFloat(R.styleable.CustomTextView_strokeWidth,1);
            strokeColor = a.getInt(R.styleable.CustomTextView_strokeColor,Color.WHITE);
            solidColor = a.getInt(R.styleable.CustomTextView_solidColor,Color.RED);
            textColor = a.getInt(R.styleable.CustomTextView_textColor,Color.WHITE);


        } finally {
            a.recycle();
        }

    }
}