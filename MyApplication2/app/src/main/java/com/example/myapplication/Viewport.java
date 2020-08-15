package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.ViewGroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class Viewport extends ViewGroup {
    private double WidthRatio = MainActivity.mBoxWidth;
    private double HeightRatio = MainActivity.mBoxHeight;

    public Viewport(Context context) {
        super(context);
    }

    public Viewport(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Viewport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int viewportMargin = 32;
        int viewportCornerRadius = 8;
        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        int width = getWidth();
        int height = getHeight();

        int mBoxHeight = (int) (HeightRatio * height);
        int mBoxWidth = (int) (WidthRatio * width);

        int right = (width / 2) + (mBoxWidth / 2);
        int left = (width / 2) - (mBoxWidth / 2);
        int bottom = (height / 2) + (mBoxHeight / 2);
        int top = (height / 2) - (mBoxHeight / 2);

        RectF rect = new RectF((float)left, (float)top, right, bottom);
        //RectF frame = new RectF((float)left-2, (float)top-2, mBoxWidth+4, mBoxHeight+4);
        Path path = new Path();
        Paint stroke = new Paint();
        stroke.setAntiAlias(true);
        stroke.setStrokeWidth(4);
        stroke.setColor(Color.WHITE);
        stroke.setStyle(Paint.Style.STROKE);
        //path.addRoundRect(frame, (float) viewportCornerRadius, (float) viewportCornerRadius, Path.Direction.CW);
        canvas.drawPath(path, stroke);
        canvas.drawRoundRect(rect, (float) viewportCornerRadius, (float) viewportCornerRadius, eraser);
    }
}
