package com.example.myapplication;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;

import java.io.ByteArrayOutputStream;

public class BoxDetector extends Detector {
    private Detector mDelegate;
    public double WidthRatio;
    private double HeightRatio;
    private Activity activity;

    public BoxDetector(Detector delegate,Activity a) {
        mDelegate = delegate;
        activity = a;
    }

    public SparseArray detect(Frame frame) {

        Configuration newConfig = activity.getResources().getConfiguration();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            WidthRatio = MainActivity.mBoxWidth;
            HeightRatio = MainActivity.mBoxHeight;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            WidthRatio = MainActivity.mBoxHeight;
            HeightRatio = MainActivity.mBoxWidth;
        }
        int width = frame.getMetadata().getWidth();
        int height = frame.getMetadata().getHeight();
        int mBoxHeight = (int) (HeightRatio * height);
        int mBoxWidth = (int) (WidthRatio * width);

        int right = (width / 2) + (mBoxWidth / 2);
        int left = (width / 2) - (mBoxWidth / 2);
        int bottom = (height / 2) + (mBoxHeight / 2);
        int top = (height / 2) - (mBoxHeight / 2);

        YuvImage yuvImage = new YuvImage(frame.getGrayscaleImageData().array(), ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(left, top, right, bottom), 100, byteArrayOutputStream);
        byte[] jpegArray = byteArrayOutputStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.length);

        Frame croppedFrame =
                new Frame.Builder()
                        .setBitmap(bitmap)
                        .setRotation(frame.getMetadata().getRotation())
                        .build();

        return mDelegate.detect(croppedFrame);
    }

    public boolean isOperational() {
        return mDelegate.isOperational();
    }

    public boolean setFocus(int id) {
        return mDelegate.setFocus(id);
    }
}